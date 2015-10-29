package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Configs.CommandsConfig;
import HxCKDMS.HxCCore.Configs.Configurations;
import HxCKDMS.HxCCore.Configs.Kits;
import HxCKDMS.HxCCore.Handlers.CommandsHandler;
import HxCKDMS.HxCCore.Handlers.NBTFileIO;
import HxCKDMS.HxCCore.Handlers.PermissionsHandler;
import HxCKDMS.HxCCore.HxCCore;
import HxCKDMS.HxCCore.api.Command.HxCCommand;
import HxCKDMS.HxCCore.api.Command.ISubCommand;
import HxCKDMS.HxCCore.lib.References;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.StatCollector;

import java.io.File;
import java.util.List;

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
            String UUID = player.getUniqueID().toString();
            File CustomPlayerData = new File(HxCCore.HxCCoreDir, "HxC-" + UUID + ".dat");
            boolean CanSend = PermissionsHandler.canUseCommand(CommandsConfig.CommandPermissions.get("ReloadConfigs"), player);
            if (CanSend) {
                HxCCore.commandCFG.handleConfig(CommandsConfig.class, HxCCore.commandCFGFile);
                HxCCore.hxCConfig.handleConfig(Configurations.class, HxCCore.HxCConfigFile);
                HxCCore.kits.handleConfig(Kits.class, HxCCore.kitsFile);
            } else throw new WrongUsageException(StatCollector.translateToLocal("command.exception.permission"));
        } else throw new WrongUsageException(StatCollector.translateToLocal("command.exception.playersonly"));
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        return References.COLOR_CHARS_STRING;
    }
}
