package hxckdms.hxccore.commands;

import hxckdms.hxccore.api.command.AbstractSubCommand;
import hxckdms.hxccore.api.command.HxCCommand;
import hxckdms.hxccore.libraries.GlobalVariables;
import hxckdms.hxccore.utilities.ServerTranslationHelper;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@HxCCommand
public class CommandMute extends AbstractSubCommand<CommandHxC> {
    {
        permissionLevel = 3;
    }

    @Override
    public String getCommandName() {
        return "mute";
    }

    @Override
    public void execute(ICommandSender sender, LinkedList<String> args) throws CommandException {
        NBTTagCompound mutedPlayers = GlobalVariables.customWorldData.getTagCompound("mutedPlayers", new NBTTagCompound());
        EntityPlayerMP target = CommandBase.getPlayer(sender, args.get(0));
        mutedPlayers.setBoolean(target.getUniqueID().toString(), !mutedPlayers.getBoolean(target.getUniqueID().toString()));
        GlobalVariables.customWorldData.setTagCompound("mutedPlayers", mutedPlayers);
        sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.mute.other.sender." + (mutedPlayers.getBoolean(target.getUniqueID().toString()) ? "true" : "false"), target.getDisplayName()).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GRAY)));
        target.addChatMessage(ServerTranslationHelper.getTranslation(target, "commands.mute.other.target." + (mutedPlayers.getBoolean(target.getUniqueID().toString()) ? "true" : "false"), sender.getCommandSenderName()).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, LinkedList<String> args) {
        return args.size() == 1 ? CommandBase.getListOfStringsMatchingLastWord(args.toArray(new String[args.size()]), GlobalVariables.server.getAllUsernames()) : Collections.emptyList();
    }
}
