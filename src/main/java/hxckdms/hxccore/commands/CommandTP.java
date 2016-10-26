package hxckdms.hxccore.commands;

import hxckdms.hxccore.api.command.AbstractMultiCommand;
import hxckdms.hxccore.api.command.AbstractSubCommand;
import hxckdms.hxccore.api.command.HxCCommand;
import hxckdms.hxccore.libraries.GlobalVariables;
import hxckdms.hxccore.utilities.ServerTranslationHelper;
import hxckdms.hxccore.utilities.TeleportHelper;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@HxCCommand
public class CommandTP extends AbstractSubCommand {
    {
        permissionLevel = 3;
    }

    @Override
    public String getCommandName() {
        return "TP";
    }

    @Override
    public void execute(ICommandSender sender, LinkedList<String> args) throws CommandException {
        switch (args.size()) {
            case 1:
                if (sender instanceof EntityPlayerMP) {
                    EntityPlayerMP player = (EntityPlayerMP) sender;
                    EntityPlayerMP destination = CommandBase.getPlayer(GlobalVariables.server, sender, args.get(0));

                    TeleportHelper.teleportEntityToDimension(player, destination.posX, destination.posY, destination.posZ, destination.dimension);
                    sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.TP_HxC.player.self.player", destination.getName()).setStyle(new Style().setColor(TextFormatting.BLUE)));
                    destination.addChatMessage(ServerTranslationHelper.getTranslation(destination, "commands.TP_HxC.player.self.destination", sender.getName()).setStyle(new Style().setColor(TextFormatting.YELLOW)));
                }
                break;
            case 2:
                EntityPlayerMP target = CommandBase.getPlayer(GlobalVariables.server, sender, args.get(0));
                EntityPlayerMP destination = CommandBase.getPlayer(GlobalVariables.server, sender, args.get(1));

                TeleportHelper.teleportEntityToDimension(target, destination.posX, destination.posY, destination.posZ, destination.dimension);
                sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.TP_HxC.player.other.sender", target.getName(), destination.getName()).setStyle(new Style().setColor(TextFormatting.GRAY)));
                target.addChatMessage(ServerTranslationHelper.getTranslation(target, "commands.TP_HxC.player.other.target", sender.getName(), destination.getName()).setStyle(new Style().setColor(TextFormatting.YELLOW)));
                destination.addChatMessage(ServerTranslationHelper.getTranslation(destination, "commands.TP_HxC.player.other.destination", sender.getName(), target.getName()).setStyle(new Style().setColor(TextFormatting.YELLOW)));
                break;
            case 4:
                if (sender instanceof EntityPlayerMP) {
                    EntityPlayerMP player = (EntityPlayerMP) sender;
                    double x = CommandBase.parseCoordinate(player.posX, args.get(0), true).getAmount();
                    double y = CommandBase.parseCoordinate(player.posY, args.get(1), false).getAmount();
                    double z = CommandBase.parseCoordinate(player.posZ, args.get(2), true).getAmount();
                    int dimension = CommandBase.parseInt(args.get(3));

                    TeleportHelper.teleportEntityToDimension(player, x, y, z, dimension);
                    sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.TP_HxC.coordinates.self", x, y, z, dimension).setStyle(new Style().setColor(TextFormatting.BLUE)));
                }
                break;
            case 5:
                target = CommandBase.getPlayer(GlobalVariables.server, sender, args.get(0));
                double x = CommandBase.parseCoordinate(target.posX, args.get(1), true).getAmount();
                double y = CommandBase.parseCoordinate(target.posY, args.get(2), false).getAmount();
                double z = CommandBase.parseCoordinate(target.posZ, args.get(3), true).getAmount();
                int dimension = CommandBase.parseInt(args.get(4));

                TeleportHelper.teleportEntityToDimension(target, x, y, z, dimension);
                sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.TP_HxC.coordinates.other.sender", target.getName(), x, y, z, dimension).setStyle(new Style().setColor(TextFormatting.GRAY)));
                target.addChatMessage(ServerTranslationHelper.getTranslation(target, "commands.TP_HxC.coordinates.other.target", sender.getName(), x, y, z, dimension).setStyle(new Style().setColor(TextFormatting.YELLOW)));
                break;
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, LinkedList<String> args, @Nullable BlockPos pos) {
        if (args.size() == 1 || args.size() == 2) return CommandBase.getListOfStringsMatchingLastWord(args.toArray(new String[args.size()]), GlobalVariables.server.getAllUsernames());
        else return Collections.emptyList();
    }

    @Override
    public Class<? extends AbstractMultiCommand> getParentCommand() {
        return CommandHxC.class;
    }
}
