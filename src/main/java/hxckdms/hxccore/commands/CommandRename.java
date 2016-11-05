package hxckdms.hxccore.commands;

import hxckdms.hxccore.api.command.AbstractSubCommand;
import hxckdms.hxccore.api.command.HxCCommand;
import hxckdms.hxccore.api.command.TranslatedCommandException;
import hxckdms.hxccore.utilities.ColorHelper;
import hxckdms.hxccore.utilities.ServerTranslationHelper;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@HxCCommand
public class CommandRename extends AbstractSubCommand<CommandHxC> {
    {
        permissionLevel = 1;
    }

    @Override
    public String getCommandName() {
        return "rename";
    }

    @Override
    public void execute(ICommandSender sender, LinkedList<String> args) throws CommandException {
        if (sender instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) sender;

            ChatComponentTranslation name = ColorHelper.handleMessage(args.stream().collect(Collectors.joining(" ")), 'f');
            ItemStack itemStack = player.inventory.getCurrentItem();
            if (itemStack == null) throw new TranslatedCommandException(sender, "commands.error.noItem");
            itemStack.setStackDisplayName(name.getFormattedText());
            player.inventory.setInventorySlotContents(player.inventory.currentItem, itemStack);

            sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.rename.successful", new ChatComponentTranslation(itemStack.getUnlocalizedName() + ".name"), itemStack.getDisplayName()).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GREEN)));
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, LinkedList<String> args) {
        return Collections.emptyList();
    }
}
