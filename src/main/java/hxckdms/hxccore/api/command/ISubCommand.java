package hxckdms.hxccore.api.command;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

import java.util.LinkedList;
import java.util.List;

interface ISubCommand<T extends IMultiCommand> {
    String getCommandName();
    void execute(ICommandSender sender, LinkedList<String> args) throws CommandException;
    List<String> addTabCompletionOptions(ICommandSender sender, LinkedList<String> args);

    Class<T> getParentCommand();
    int getPermissionLevel();
    CommandState getCommandState();

    void setPermissionLevel(int level);
    void setCommandState(CommandState state);
}
