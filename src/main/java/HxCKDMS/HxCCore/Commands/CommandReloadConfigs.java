package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Configs.CommandsConfig;
import HxCKDMS.HxCCore.Configs.Configurations;
import HxCKDMS.HxCCore.Configs.Kits;
import HxCKDMS.HxCCore.Handlers.CommandsHandler;
import HxCKDMS.HxCCore.Handlers.PermissionsHandler;
import HxCKDMS.HxCCore.HxCCore;
import HxCKDMS.HxCCore.api.Command.HxCCommand;
import HxCKDMS.HxCCore.api.Command.ISubCommand;
import HxCKDMS.HxCCore.lib.References;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.StatCollector;

import java.util.List;

import static HxCKDMS.HxCCore.lib.References.HOMES;
import static HxCKDMS.HxCCore.lib.References.PERM_COLOURS;
import static HxCKDMS.HxCCore.lib.References.PERM_NAMES;

@HxCCommand(defaultPermission = 4, mainCommand = CommandsHandler.class, isEnabled = true)
public class CommandReloadConfigs implements ISubCommand {
    public static CommandReloadConfigs instance = new CommandReloadConfigs();

    @Override
    public String getCommandName() {
        return "ReloadConfigs";
    }

    @Override
    public int[] getCommandRequiredParams() {
        return new int[]{0, 0, -1};
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] args, boolean isPlayer) {
        if (isPlayer) {
            EntityPlayerMP player = (EntityPlayerMP) sender;
            boolean CanSend = PermissionsHandler.canUseCommand(CommandsConfig.CommandPermissions.get("ReloadConfigs"), player);
            if (CanSend) {
                HxCCore.commandCFG.handleConfig(CommandsConfig.class, HxCCore.commandCFGFile);
                HxCCore.hxCConfig.handleConfig(Configurations.class, HxCCore.HxCConfigFile);
                HxCCore.kits.handleConfig(Kits.class, HxCCore.kitsFile);
                PERM_NAMES =  new String[Configurations.Permissions.size()];
                PERM_COLOURS = new char[Configurations.Permissions.size()];
                HOMES = new int[Configurations.Permissions.size()];
                for (int i = 0; i < Configurations.Permissions.size(); i++) {
                    PERM_NAMES[i] = (String) Configurations.Permissions.keySet().toArray()[i];
                    PERM_COLOURS[i] = Configurations.Permissions.get(PERM_NAMES[i]).charAt(0);
                    HOMES[i] = Integer.parseInt(Configurations.Permissions.get(PERM_NAMES[i]).substring(1).trim());
                }
            } else throw new WrongUsageException(StatCollector.translateToLocal("command.exception.permission"));
        } else throw new WrongUsageException(StatCollector.translateToLocal("command.exception.playersonly"));
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        return References.COLOR_CHARS_STRING;
    }
}
