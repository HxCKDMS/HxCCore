package hxckdms.hxccore.commands;

import hxckdms.hxccore.api.command.AbstractSubCommand;
import hxckdms.hxccore.api.command.HxCCommand;
import hxckdms.hxccore.libraries.GlobalVariables;
import hxckdms.hxccore.utilities.ServerTranslationHelper;
import hxckdms.hxccore.utilities.TeleportHelper;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

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
    public String getCommandName() {
        return "TP";
    }

    @Override
    public void execute(ICommandSender sender, LinkedList<String> args) throws CommandException {
        switch (args.size()) {
            case 1:
                if (sender instanceof EntityPlayerMP) {
                    EntityPlayerMP player = (EntityPlayerMP) sender;
                    EntityPlayerMP destination = CommandBase.getPlayer(sender, args.get(0));

                    TeleportHelper.teleportEntityToDimension(player, destination.posX, destination.posY, destination.posZ, destination.dimension);
                    sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.TP_HxC.player.self.player", destination.getCommandSenderName()).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.BLUE)));
                    destination.addChatMessage(ServerTranslationHelper.getTranslation(destination, "commands.TP_HxC.player.self.destination", sender.getCommandSenderName()).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.YELLOW)));
                }
                break;
            case 2:
                EntityPlayerMP target = CommandBase.getPlayer(sender, args.get(0));
                EntityPlayerMP destination = CommandBase.getPlayer(sender, args.get(1));

                TeleportHelper.teleportEntityToDimension(target, destination.posX, destination.posY, destination.posZ, destination.dimension);
                sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.TP_HxC.player.other.sender", target.getCommandSenderName(), destination.getCommandSenderName()).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GRAY)));
                target.addChatMessage(ServerTranslationHelper.getTranslation(target, "commands.TP_HxC.player.other.target", sender.getCommandSenderName(), destination.getCommandSenderName()).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.YELLOW)));
                destination.addChatMessage(ServerTranslationHelper.getTranslation(destination, "commands.TP_HxC.player.other.destination", sender.getCommandSenderName(), target.getCommandSenderName()).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.YELLOW)));
                break;
            case 4:
                if (sender instanceof EntityPlayerMP) {
                    EntityPlayerMP player = (EntityPlayerMP) sender;
                    double x = CommandBase.func_110666_a(sender, player.posX, args.get(0));
                    double y = CommandBase.func_110666_a(sender, player.posY, args.get(1));
                    double z = CommandBase.func_110666_a(sender, player.posZ, args.get(2));
                    int dimension = CommandBase.parseInt(sender, args.get(3));

                    TeleportHelper.teleportEntityToDimension(player, x, y, z, dimension);
                    sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.TP_HxC.coordinates.self", posFormat.format(x), posFormat.format(y), posFormat.format(z), dimension).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.BLUE)));
                }
                break;
            case 5:
                target = CommandBase.getPlayer(sender, args.get(0));
                double x = CommandBase.func_110666_a(sender, target.posX, args.get(1));
                double y = CommandBase.func_110666_a(sender, target.posY, args.get(2));
                double z = CommandBase.func_110666_a(sender, target.posZ, args.get(3));
                int dimension = CommandBase.parseInt(sender, args.get(4));

                TeleportHelper.teleportEntityToDimension(target, x, y, z, dimension);
                sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.TP_HxC.coordinates.other.sender", target.getCommandSenderName(), posFormat.format(x), posFormat.format(y), posFormat.format(z), dimension).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GRAY)));
                target.addChatMessage(ServerTranslationHelper.getTranslation(target, "commands.TP_HxC.coordinates.other.target", sender.getCommandSenderName(), posFormat.format(x), posFormat.format(y), posFormat.format(z), dimension).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.YELLOW)));
                break;
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, LinkedList<String> args) {
        if (args.size() == 1 || args.size() == 2) return CommandBase.getListOfStringsMatchingLastWord(args.toArray(new String[args.size()]), GlobalVariables.server.getAllUsernames());
        else return Collections.emptyList();
    }
}
