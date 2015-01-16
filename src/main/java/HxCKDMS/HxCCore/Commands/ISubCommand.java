package HxCKDMS.HxCCore.Commands;

import net.minecraft.command.ICommandSender;

import java.util.List;

public interface ISubCommand {
    public String getCommandName();

    public void handleCommand(ICommandSender sender, String[] args);

    public int getRequiredPermissionLevel();

    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args);
}
