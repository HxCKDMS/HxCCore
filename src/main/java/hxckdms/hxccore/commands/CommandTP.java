package hxckdms.hxccore.commands;

import hxckdms.hxccore.api.command.AbstractSubCommand;
import hxckdms.hxccore.api.command.HxCCommand;
import hxckdms.hxccore.utilities.ServerTranslationHelper;
import hxckdms.hxccore.utilities.TeleportHelper;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@HxCCommand
public class CommandTP extends AbstractSubCommand<CommandHxC> {
    private static DecimalFormat posFormat = new DecimalFormat("#.###");

    {
        permissionLevel = 3;
    }

    @Override
    public String getName() {
        return "TP";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, LinkedList<String> args) throws CommandException {
        switch (args.size()) {
            case 1:
                if (sender instanceof EntityPlayerMP) {
                    EntityPlayerMP player = (EntityPlayerMP) sender;
                    EntityPlayerMP destination = CommandBase.getPlayer(server, sender, args.get(0));

                    TeleportHelper.teleportEntityToDimension(player, destination.posX, destination.posY, destination.posZ, destination.dimension);
                    sender.sendMessage(ServerTranslationHelper.getTranslation(sender, "commands.TP_HxC.player.self.player", destination.getName()).setStyle(new Style().setColor(TextFormatting.BLUE)));
                    destination.sendMessage(ServerTranslationHelper.getTranslation(destination, "commands.TP_HxC.player.self.destination", sender.getName()).setStyle(new Style().setColor(TextFormatting.YELLOW)));
                }
                break;
            case 2:
                EntityPlayerMP target = CommandBase.getPlayer(server, sender, args.get(0));
                EntityPlayerMP destination = CommandBase.getPlayer(server, sender, args.get(1));

                TeleportHelper.teleportEntityToDimension(target, destination.posX, destination.posY, destination.posZ, destination.dimension);
                sender.sendMessage(ServerTranslationHelper.getTranslation(sender, "commands.TP_HxC.player.other.sender", target.getName(), destination.getName()).setStyle(new Style().setColor(TextFormatting.GRAY)));
                target.sendMessage(ServerTranslationHelper.getTranslation(target, "commands.TP_HxC.player.other.target", sender.getName(), destination.getName()).setStyle(new Style().setColor(TextFormatting.YELLOW)));
                destination.sendMessage(ServerTranslationHelper.getTranslation(destination, "commands.TP_HxC.player.other.destination", sender.getName(), target.getName()).setStyle(new Style().setColor(TextFormatting.YELLOW)));
                break;
            case 4:
                if (sender instanceof EntityPlayerMP) {
                    EntityPlayerMP player = (EntityPlayerMP) sender;
                    double x = CommandBase.parseCoordinate(player.posX, args.get(0), true).getResult();
                    double y = CommandBase.parseCoordinate(player.posY, args.get(1), false).getResult();
                    double z = CommandBase.parseCoordinate(player.posZ, args.get(2), true).getResult();
                    int dimension = CommandBase.parseInt(args.get(3));

                    TeleportHelper.teleportEntityToDimension(player, x, y, z, dimension);
                    sender.sendMessage(ServerTranslationHelper.getTranslation(sender, "commands.TP_HxC.coordinates.self", posFormat.format(x), posFormat.format(y), posFormat.format(z), dimension).setStyle(new Style().setColor(TextFormatting.BLUE)));
                }
                break;
            case 5:
                target = CommandBase.getPlayer(server, sender, args.get(0));
                double x = CommandBase.parseCoordinate(target.posX, args.get(1), true).getResult();
                double y = CommandBase.parseCoordinate(target.posY, args.get(2), false).getResult();
                double z = CommandBase.parseCoordinate(target.posZ, args.get(3), true).getResult();
                int dimension = CommandBase.parseInt(args.get(4));

                TeleportHelper.teleportEntityToDimension(target, x, y, z, dimension);
                sender.sendMessage(ServerTranslationHelper.getTranslation(sender, "commands.TP_HxC.coordinates.other.sender", target.getName(), posFormat.format(x), posFormat.format(y), posFormat.format(z), dimension).setStyle(new Style().setColor(TextFormatting.GRAY)));
                target.sendMessage(ServerTranslationHelper.getTranslation(target, "commands.TP_HxC.coordinates.other.target", sender.getName(), posFormat.format(x), posFormat.format(y), posFormat.format(z), dimension).setStyle(new Style().setColor(TextFormatting.YELLOW)));
                break;
        }
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, LinkedList<String> args, @Nullable BlockPos targetPos) {
        if (args.size() == 1 || args.size() == 2) return CommandBase.getListOfStringsMatchingLastWord(args.toArray(new String[0]), server.getOnlinePlayerNames());
        else return Collections.emptyList();
    }
}
