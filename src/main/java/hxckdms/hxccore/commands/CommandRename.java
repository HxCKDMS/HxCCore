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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentBase;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
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
    public String getName() {
        return "rename";
    }

    @Override
    public void execute(ICommandSender sender, LinkedList<String> args) throws CommandException {
        if (sender instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) sender;

            TextComponentBase name = ColorHelper.handleMessage(args.stream().collect(Collectors.joining(" ")), 'f');
            ItemStack itemStack = player.inventory.getCurrentItem();
            if (itemStack == null) throw new TranslatedCommandException(sender, "commands.error.noItem");
            itemStack.setStackDisplayName(name.getFormattedText());
            player.inventory.setInventorySlotContents(player.inventory.currentItem, itemStack);

            sender.sendMessage(ServerTranslationHelper.getTranslation(sender, "commands.rename.successful", new TextComponentTranslation(itemStack.getUnlocalizedName() + ".name"), itemStack.getDisplayName()).setStyle(new Style().setColor(TextFormatting.GREEN)));
        }
    }

    @Override
    public List<String> addTabCompletions(ICommandSender sender, LinkedList<String> args, @Nullable BlockPos pos) {
        return Collections.emptyList();
    }
}
