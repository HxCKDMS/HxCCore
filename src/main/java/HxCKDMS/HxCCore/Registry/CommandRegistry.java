package HxCKDMS.HxCCore.Registry;

import HxCKDMS.HxCCore.Configs.CommandsConfig;
import HxCKDMS.HxCCore.Configs.Configurations;
import HxCKDMS.HxCCore.HxCCore;
import HxCKDMS.HxCCore.api.Command.AbstractCommandMain;
import HxCKDMS.HxCCore.api.Command.HxCCommand;
import HxCKDMS.HxCCore.api.Command.ISubCommand;
import cpw.mods.fml.common.discovery.ASMDataTable;

import java.util.Set;

@SuppressWarnings("unchecked")
public class CommandRegistry {
    public static void registerCommands(AbstractCommandMain commandMain, Set<ASMDataTable.ASMData> allData) {
        if (allData != null && !allData.isEmpty()) {
            for (ASMDataTable.ASMData data : allData) {
                try {
                    String className = data.getClassName();
                    Class<ISubCommand> clazz = (Class<ISubCommand>) Class.forName(className);
                    int permissionLevel = clazz.getAnnotation(HxCCommand.class).defaultPermission();
                    boolean isEnabled = clazz.getAnnotation(HxCCommand.class).isEnabled();
                    Class<? extends AbstractCommandMain> mainClazz = clazz.getAnnotation(HxCCommand.class).mainCommand();
                    if (mainClazz == commandMain.getClass()) {
                        ISubCommand instance = clazz.newInstance();
                        CommandsConfig.CommandPermissions.putIfAbsent(instance.getCommandName(), permissionLevel);
                        CommandsConfig.EnabledCommands.putIfAbsent(instance.getCommandName(), isEnabled);

                        if (CommandsConfig.EnabledCommands.get(instance.getCommandName()))
                            commandMain.registerSubCommand(instance);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        HxCCore.commandCFG.handleConfig(Configurations.class, HxCCore.commandCFGFile);
    }
}
