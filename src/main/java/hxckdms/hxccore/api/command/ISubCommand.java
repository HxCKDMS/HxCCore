package hxckdms.hxccore.api.command;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.List;

interface ISubCommand<T extends IMultiCommand> {
    String getName();
    void execute(ICommandSender sender, LinkedList<String> args) throws CommandException;
    List<String> addTabCompletions(ICommandSender sender, LinkedList<String> args, @Nullable BlockPos pos);

    Class<T> getParent();
    int getPermissionLevel();
    CommandState getState();

    void setPermissionLevel(int level);
    void setState(CommandState state);
}
