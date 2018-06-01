package hxckdms.hxccore.api.command;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.List;

public interface ISubCommand<T extends IMultiCommand> {
    String getName();
    void execute(MinecraftServer server, ICommandSender sender, LinkedList<String> args) throws CommandException;
    List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, LinkedList<String> args, @Nullable BlockPos targetPos);

    Class<T> getParentCommand();
    int getPermissionLevel();
    CommandState getCommandState();

    void setPermissionLevel(int level);
    void setCommandState(CommandState state);
}
