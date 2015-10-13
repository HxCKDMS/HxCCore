package HxCKDMS.HxCCore.Configs;

import HxCKDMS.HxCCore.api.Configuration.Config;

import java.util.LinkedHashMap;

public class CommandsConfig {
    @Config.Map
    public static LinkedHashMap<String, Integer> CommandPermissions = new LinkedHashMap<>();

    @Config.Map
    public static LinkedHashMap<String, Boolean> EnabledCommands = new LinkedHashMap<>();

    @Config.Map
    public static LinkedHashMap<String, Integer> BannedCommands = new LinkedHashMap<>();
    /*
        TODO: Enabled ? : ; map or make custom config write/read from the config handler of which does,
         TODO: PermLevel, IsEnabled, Aliases?, optional values?, Logged (if true then log when command is used)?,
          TODO: Reported?(tells admins when command is used and by whom and on what)
     */
    static {
        BannedCommands.put("time set 0", 0);
    }
}