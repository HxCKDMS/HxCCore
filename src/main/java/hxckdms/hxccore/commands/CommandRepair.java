package hxckdms.hxccore.commands;

import hxckdms.hxccore.api.command.AbstractMultiCommand;
import hxckdms.hxccore.api.command.AbstractSubCommand;
import hxckdms.hxccore.api.command.HxCCommand;
import hxckdms.hxccore.api.command.TranslatedCommandException;
import hxckdms.hxccore.utilities.ServerTranslationHelper;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@HxCCommand
public class CommandRepair extends AbstractSubCommand {
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
            ItemStack itemStack = player.getHeldItemMainhand();
            if (itemStack == null) throw new TranslatedCommandException(sender, "commands.error.repair.noItem");
            if (!itemStack.isItemStackDamageable()) throw new TranslatedCommandException(sender, "commands.error.repair.unrepairable");
            if (!itemStack.isItemDamaged()) throw new TranslatedCommandException(sender, "commands.error.repair.undamaged");
            itemStack.setItemDamage(0);
            sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.repair.successful", sender.getDisplayName()));
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, LinkedList<String> args, @Nullable BlockPos pos) {
        return Collections.emptyList();
    }

    @Override
    public Class<? extends AbstractMultiCommand> getParentCommand() {
        return CommandHxC.class;
    }
}
