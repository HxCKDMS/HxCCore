package HxCKDMS.HxCCore.api.Command;

import net.minecraft.command.ICommandSender;

import java.util.List;

public interface ISubCommand {
    String getCommandName();
    int[] getCommandRequiredParams();
    void handleCommand(ICommandSender sender, String[] args, boolean isPlayer);
    List<String> addTabCompletionOptions(ICommandSender sender, String[] args);
}
