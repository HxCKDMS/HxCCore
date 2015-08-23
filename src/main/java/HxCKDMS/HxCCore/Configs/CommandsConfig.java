package HxCKDMS.HxCCore.Configs;

import HxCKDMS.HxCCore.api.Configuration.Config;

import java.util.LinkedHashMap;

public class CommandsConfig {
    @Config.Map(category = "Commands")
    public static LinkedHashMap<String, Integer> commands = new LinkedHashMap<>();
    //TODO: Enabled ? : ; map or make custom config write/read from the config handler of which does, PermLevel, IsEnabled, Aliases?, optional values?, Logged (if true then log when command is used)?, Reported?(tells admins when command is used and by whom and on what)
}