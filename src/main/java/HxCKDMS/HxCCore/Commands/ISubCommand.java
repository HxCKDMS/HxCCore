package HxCKDMS.HxCCore.Commands;

import net.minecraft.command.ICommandSender;

import java.io.IOException;
import java.util.List;

public interface ISubCommand {
    public String getCommandName();

    public void handleCommand(ICommandSender sender, String[] args) throws IOException;

    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args);
}
