package hxckdms.hxccore.commands;

import hxckdms.hxccore.api.command.AbstractSubCommand;
import hxckdms.hxccore.api.command.HxCCommand;
import hxckdms.hxccore.api.command.TranslatedCommandException;
import hxckdms.hxccore.utilities.ServerTranslationHelper;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@HxCCommand
public class CommandRepair extends AbstractSubCommand<CommandHxC> {
    {
        permissionLevel = 3;
    }

    @Override
    public String getCommandName() {
        return "repair";
    }

    @Override
    public void execute(ICommandSender sender, LinkedList<String> args) throws CommandException {
        if (sender instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) sender;
            ItemStack itemStack = player.getCurrentEquippedItem();
            if (itemStack == null) throw new TranslatedCommandException(sender, "commands.error.noItem");
            if (!itemStack.isItemStackDamageable()) throw new TranslatedCommandException(sender, "commands.error.repair.unrepairable");
            if (!itemStack.isItemDamaged()) throw new TranslatedCommandException(sender, "commands.error.repair.undamaged");
            itemStack.setItemDamage(0);
            sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.repair.successful", itemStack.getDisplayName()).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GREEN)));
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, LinkedList<String> args) {
        return Collections.emptyList();
    }
}
