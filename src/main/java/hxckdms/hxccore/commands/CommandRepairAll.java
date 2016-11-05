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
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@HxCCommand
public class CommandRepairAll extends AbstractSubCommand<CommandHxC> {
    {
        permissionLevel = 4;
    }

    @Override
    public String getCommandName() {
        return "repairAll";
    }

    @Override
    public void execute(ICommandSender sender, LinkedList<String> args) throws CommandException {
        switch (args.size()) {
            case 0:
                if (sender instanceof EntityPlayerMP) {
                    EntityPlayerMP player = (EntityPlayerMP) sender;
                    Arrays.stream(player.inventory.armorInventory).filter(itemStack -> itemStack != null && itemStack.isItemStackDamageable() && itemStack.isItemDamaged()).forEach(itemStack -> itemStack.setItemDamage(0));
                    Arrays.stream(player.inventory.mainInventory).filter(itemStack -> itemStack != null && itemStack.isItemStackDamageable() && itemStack.isItemDamaged()).forEach(itemStack -> itemStack.setItemDamage(0));

                    sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.repairAll.successful.self").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GREEN)));
                }
                break;
            case 1:
                EntityPlayerMP target = CommandBase.getPlayer(sender, args.get(0));
                Arrays.stream(target.inventory.armorInventory).filter(itemStack -> itemStack != null && itemStack.isItemStackDamageable() && itemStack.isItemDamaged()).forEach(itemStack -> itemStack.setItemDamage(0));
                Arrays.stream(target.inventory.mainInventory).filter(itemStack -> itemStack != null && itemStack.isItemStackDamageable() && itemStack.isItemDamaged()).forEach(itemStack -> itemStack.setItemDamage(0));

                sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.repairAll.successful.other.sender").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GREEN)));
                sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.repairAll.successful.other.target").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GRAY)));
                break;
        }
    }

    private static void repairItems(ItemStack[] itemStacks) {
        Arrays.stream(itemStacks).filter(itemStack -> itemStack != null && itemStack.isItemStackDamageable() && itemStack.isItemDamaged()).forEach(itemStack -> itemStack.setItemDamage(0));
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, LinkedList<String> args) {
        return args.size() == 1 ? CommandBase.getListOfStringsMatchingLastWord(args.toArray(new String[args.size()]), GlobalVariables.server.getAllUsernames()) : Collections.emptyList();
    }
}
