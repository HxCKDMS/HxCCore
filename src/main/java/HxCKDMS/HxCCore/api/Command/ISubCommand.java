package HxCKDMS.HxCCore.api.Command;

import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.WrongUsageException;

import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.WrongUsageException;

import java.util.List;

public interface ISubCommand {
    String getCommandName();
    int[] getCommandRequiredParams();
    void handleCommand(ICommandSender sender, String[] args, boolean isPlayer) throws PlayerNotFoundException, WrongUsageException, WrongUsageException, WrongUsageException, PlayerNotFoundException;
    List<String> addTabCompletionOptions(ICommandSender sender, String[] args);
}
