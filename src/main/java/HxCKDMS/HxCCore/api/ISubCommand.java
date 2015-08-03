
package HxCKDMS.HxCCore.api;

import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.WrongUsageException;

import java.util.List;

public interface ISubCommand {
    String getCommandName();

    void handleCommand(ICommandSender sender, String[] args) throws WrongUsageException, PlayerNotFoundException, NumberInvalidException;

    List addTabCompletionOptions(ICommandSender sender, String[] args);
}