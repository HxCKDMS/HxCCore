package HxCKDMS.HxCCore.api.Command;

import net.minecraft.command.ICommandSender;

import java.util.List;

public interface ISubCommand {
    String getCommandName();
    int[] getCommandRequiredParams();
    void handleCommandFromClient(ICommandSender sender, String[] args);
    void handleCommandFromServer(ICommandSender sender, String[] args);
    List<String> addTabCompletionOptions(ICommandSender sender, String[] args);
}
