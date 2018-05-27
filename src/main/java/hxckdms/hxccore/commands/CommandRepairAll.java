package hxckdms.hxccore.commands;

import hxckdms.hxccore.api.command.AbstractSubCommand;
import hxckdms.hxccore.api.command.HxCCommand;
import hxckdms.hxccore.utilities.ServerTranslationHelper;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
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
    public void execute(MinecraftServer server, ICommandSender sender, LinkedList<String> args) throws CommandException {
        switch (args.size()) {
            case 0:
                if (sender instanceof EntityPlayerMP) {
                    EntityPlayerMP player = (EntityPlayerMP) sender;
                    player.inventory.armorInventory.stream().filter(itemStack -> itemStack != null && itemStack.isItemStackDamageable() && itemStack.isItemDamaged()).forEach(itemStack -> itemStack.setItemDamage(0));
                    player.inventory.mainInventory.stream().filter(itemStack -> itemStack != null && itemStack.isItemStackDamageable() && itemStack.isItemDamaged()).forEach(itemStack -> itemStack.setItemDamage(0));

                    sender.sendMessage(ServerTranslationHelper.getTranslation(sender, "commands.repairAll.successful.self").setStyle(new Style().setColor(TextFormatting.GREEN)));
                }
                break;
            case 1:
                EntityPlayerMP target = CommandBase.getPlayer(server, sender, args.get(0));
                target.inventory.armorInventory.stream().filter(itemStack -> itemStack != null && itemStack.isItemStackDamageable() && itemStack.isItemDamaged()).forEach(itemStack -> itemStack.setItemDamage(0));
                target.inventory.mainInventory.stream().filter(itemStack -> itemStack != null && itemStack.isItemStackDamageable() && itemStack.isItemDamaged()).forEach(itemStack -> itemStack.setItemDamage(0));

                sender.sendMessage(ServerTranslationHelper.getTranslation(sender, "commands.repairAll.successful.other.sender").setStyle(new Style().setColor(TextFormatting.GREEN)));
                sender.sendMessage(ServerTranslationHelper.getTranslation(sender, "commands.repairAll.successful.other.target").setStyle(new Style().setColor(TextFormatting.GRAY)));
                break;
        }
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, LinkedList<String> args, @Nullable BlockPos targetPos) {
        return args.size() == 1 ? CommandBase.getListOfStringsMatchingLastWord(args.toArray(new String[0]), server.getOnlinePlayerNames()) : Collections.emptyList();
    }
}
