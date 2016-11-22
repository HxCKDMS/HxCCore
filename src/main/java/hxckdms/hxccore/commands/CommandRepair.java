package hxckdms.hxccore.commands;

import hxckdms.hxccore.api.command.AbstractSubCommand;
import hxckdms.hxccore.api.command.HxCCommand;
import hxckdms.hxccore.api.command.TranslatedCommandException;
import hxckdms.hxccore.utilities.ServerTranslationHelper;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@HxCCommand
public class CommandRepair extends AbstractSubCommand<CommandHxC> {
    {
        permissionLevel = 3;
    }

    @Override
    public String getName() {
        return "repair";
    }

    @Override
    public void execute(ICommandSender sender, LinkedList<String> args) throws CommandException {
        if (sender instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) sender;
            ItemStack itemStack = player.getHeldItemMainhand();
            if (itemStack == null) throw new TranslatedCommandException(sender, "commands.error.noItem");
            if (!itemStack.isItemStackDamageable()) throw new TranslatedCommandException(sender, "commands.error.repair.unrepairable");
            if (!itemStack.isItemDamaged()) throw new TranslatedCommandException(sender, "commands.error.repair.undamaged");
            itemStack.setItemDamage(0);
            sender.sendMessage(ServerTranslationHelper.getTranslation(sender, "commands.repair.successful", itemStack.getDisplayName()).setStyle(new Style().setColor(TextFormatting.GREEN)));
        }
    }

    @Override
    public List<String> addTabCompletions(ICommandSender sender, LinkedList<String> args, @Nullable BlockPos pos) {
        return Collections.emptyList();
    }
}
