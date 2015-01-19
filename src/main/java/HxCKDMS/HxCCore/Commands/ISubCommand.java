package HxCKDMS.HxCCore.Commands;

import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.WrongUsageException;

import java.util.List;

public interface ISubCommand {
    public String getCommandName();

    public void handleCommand(ICommandSender sender, String[] args) throws WrongUsageException, PlayerNotFoundException;

    public List addTabCompletionOptions(ICommandSender sender, String[] args);
}
