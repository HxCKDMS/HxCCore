package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Configs.CommandsConfig;
import HxCKDMS.HxCCore.Events.EventProtection;
import HxCKDMS.HxCCore.Handlers.CommandsHandler;
import HxCKDMS.HxCCore.Handlers.PermissionsHandler;
import HxCKDMS.HxCCore.HxCCore;
import HxCKDMS.HxCCore.api.Command.HxCCommand;
import HxCKDMS.HxCCore.api.Command.ISubCommand;
import HxCKDMS.HxCCore.lib.References;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.List;

@SuppressWarnings({"unchecked", "unused"})
@HxCCommand(defaultPermission = 5, mainCommand = CommandsHandler.class, isEnabled = true)
public class CommandOverride implements ISubCommand {
    private static CommandOverride instance = new CommandOverride();

    @Override
    public String getCommandName() {
        return "Override";
    }

    @Override
    public int[] getCommandRequiredParams() {
        return new int[]{0, -1, -1};
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] args, boolean isPlayer) throws WrongUsageException {
        if (isPlayer) {
            EntityPlayerMP player = (EntityPlayerMP)sender;
            boolean CanSend = PermissionsHandler.canUseCommand(CommandsConfig.CommandPermissions.get(getCommandName()), player);
            if (CanSend) {
                if (!EventProtection.override.contains(player))EventProtection.override.add(player);
                else EventProtection.override.remove(player);
            } else throw new WrongUsageException(HxCCore.util.readLangOnServer(References.MOD_ID, "commands.exception.permission"));
        } else throw new WrongUsageException(HxCCore.util.readLangOnServer(References.MOD_ID, "commands.exception.playersonly"));
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        return null;
    }
}
