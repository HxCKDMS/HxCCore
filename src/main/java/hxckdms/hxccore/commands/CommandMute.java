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
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@HxCCommand
public class CommandMute extends AbstractSubCommand<CommandHxC> {
    {
        permissionLevel = 3;
    }

    @Override
    public String getName() {
        return "mute";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, LinkedList<String> args) throws CommandException {
        NBTTagCompound mutedPlayers = GlobalVariables.customWorldData.getTagCompound("mutedPlayers", new NBTTagCompound());
        EntityPlayerMP target = CommandBase.getPlayer(server, sender, args.get(0));
        mutedPlayers.setBoolean(target.getUniqueID().toString(), !mutedPlayers.getBoolean(target.getUniqueID().toString()));
        GlobalVariables.customWorldData.setTagCompound("mutedPlayers", mutedPlayers);
        sender.sendMessage(ServerTranslationHelper.getTranslation(sender, "commands.mute.other.sender." + (mutedPlayers.getBoolean(target.getUniqueID().toString()) ? "true" : "false"), target.getDisplayName()).setStyle(new Style().setColor(TextFormatting.GRAY)));
        target.sendMessage(ServerTranslationHelper.getTranslation(target, "commands.mute.other.target." + (mutedPlayers.getBoolean(target.getUniqueID().toString()) ? "true" : "false"), sender.getName()).setStyle(new Style().setColor(TextFormatting.RED)));
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, LinkedList<String> args, @Nullable BlockPos targetPos) {
        return args.size() == 1 ? CommandBase.getListOfStringsMatchingLastWord(args.toArray(new String[0]), server.getOnlinePlayerNames()) : Collections.emptyList();
    }
}
