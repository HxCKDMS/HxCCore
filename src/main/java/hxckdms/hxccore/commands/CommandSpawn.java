package hxckdms.hxccore.commands;

import hxckdms.hxccore.api.command.AbstractSubCommand;
import hxckdms.hxccore.api.command.HxCCommand;
import hxckdms.hxccore.libraries.GlobalVariables;
import hxckdms.hxccore.utilities.ColorHelper;
import hxckdms.hxccore.utilities.ServerTranslationHelper;
import hxckdms.hxccore.utilities.TeleportHelper;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.EnumChatFormatting;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@HxCCommand
public class CommandSpawn extends AbstractSubCommand<CommandHxC> {
    {
        permissionLevel = 1;
    }

    @Override
    public String getCommandName() {
        return "spawn";
    }

    @Override
    public void execute(ICommandSender sender, LinkedList<String> args) throws CommandException {
        switch (args.size()) {
            case 0:
                if (sender instanceof EntityPlayerMP) {
                    EntityPlayerMP player = (EntityPlayerMP) sender;

                    ChunkCoordinates coordinates = player.getEntityWorld().getSpawnPoint();
                    TeleportHelper.teleportEntityToDimension(player, coordinates, 0);

                    sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.spawn.self", ColorHelper.handleNick((EntityPlayer) sender, false)).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GREEN)));
                }
                break;
            case 1:
                EntityPlayerMP target = CommandBase.getPlayer(sender, args.get(0));

                ChunkCoordinates coordinates = target.getEntityWorld().getSpawnPoint();
                TeleportHelper.teleportEntityToDimension(target, coordinates, 0);

                sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.spawn.other.sender", sender.getCommandSenderName()).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GRAY)));
                sender.addChatMessage(ServerTranslationHelper.getTranslation(target, "commands.spawn.other.target", target.getCommandSenderName()).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GOLD)));
                break;
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, LinkedList<String> args) {
        return args.size() == 1 ? CommandBase.getListOfStringsMatchingLastWord(args.toArray(new String[args.size()]), GlobalVariables.server.getAllUsernames()) : Collections.emptyList();
    }
}
