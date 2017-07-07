package hxckdms.hxccore.commands;

import hxckdms.hxccore.api.command.AbstractSubCommand;
import hxckdms.hxccore.api.command.HxCCommand;
import hxckdms.hxccore.libraries.GlobalVariables;
import hxckdms.hxccore.utilities.ServerTranslationHelper;
import net.minecraft.command.CommandBase;
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
public class CommandRepairAll extends AbstractSubCommand<CommandHxC> {
    {
        permissionLevel = 4;
    }

    @Override
    public String getName() {
        return "repairAll";
    }

    @Override
    public void execute(ICommandSender sender, LinkedList<String> args) throws CommandException {
        switch (args.size()) {
            case 0:
                if (sender instanceof EntityPlayerMP) {
                    EntityPlayerMP player = (EntityPlayerMP) sender;
                    for (ItemStack[] subInventory : player.inventory.allInventories)
                        for (ItemStack itemStack : subInventory)
                            if (itemStack != null && itemStack.isItemStackDamageable() && itemStack.isItemDamaged())
                                itemStack.setItemDamage(0);

                    sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.repairAll.successful.self").setStyle(new Style().setColor(TextFormatting.GREEN)));
                }
                break;
            case 1:
                EntityPlayerMP target = CommandBase.getPlayer(GlobalVariables.server, sender, args.get(0));
                for (ItemStack[] subInventory : target.inventory.allInventories)
                    for (ItemStack itemStack : subInventory)
                        if (itemStack != null && itemStack.isItemStackDamageable() && itemStack.isItemDamaged())
                            itemStack.setItemDamage(0);

                sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.repairAll.successful.other.sender").setStyle(new Style().setColor(TextFormatting.GREEN)));
                sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.repairAll.successful.other.target").setStyle(new Style().setColor(TextFormatting.GRAY)));
                break;
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, LinkedList<String> args, @Nullable BlockPos pos) {
        return args.size() == 1 ? CommandBase.getListOfStringsMatchingLastWord(args.toArray(new String[args.size()]), GlobalVariables.server.getAllUsernames()) : Collections.emptyList();
    }
}
