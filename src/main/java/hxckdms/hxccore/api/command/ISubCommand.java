package hxckdms.hxccore.api.command;

import net.minecraft.command.ICommandSender;

import java.util.LinkedList;
import java.util.List;

public interface ISubCommand {
    String getCommandName();
    int[] getCommandRequiredParams();
    void handleCommand(ICommandSender sender, LinkedList<String> args, boolean isPlayer);
    List<String> addTabCompletionOptions(ICommandSender sender, String[] args);
}
