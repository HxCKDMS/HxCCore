package hxckdms.hxccore.api.command;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.List;

public interface ISubCommand {
    String getCommandName();
    void execute(ICommandSender sender, LinkedList<String> args) throws CommandException;
    List<String> addTabCompletionOptions(ICommandSender sender, LinkedList<String> args, @Nullable BlockPos pos);
}
