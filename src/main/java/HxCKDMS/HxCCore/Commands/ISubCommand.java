package HxCKDMS.HxCCore.Commands;

import net.minecraft.command.ICommandSender;

import java.util.List;

public interface ISubCommand {
    String getCommandName();

    void handleCommand(ICommandSender sender, String[] args);

    List<String> addTabCompletionOptions(ICommandSender sender, String[] args);
}
