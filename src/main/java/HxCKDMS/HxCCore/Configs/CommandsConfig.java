package HxCKDMS.HxCCore.Configs;

import HxCKDMS.HxCCore.api.Configuration.Config;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

public class CommandsConfig {
    @Config.Map
    public static LinkedHashMap<String, String> CommandPermissions = new LinkedHashMap<>();

    @Config.Map
    public static LinkedHashMap<String, String> EnabledCommands = new LinkedHashMap<>();

    @Config.Map(description = "Add commands to this list to prevent their execution entirely... 0 means exact command, 1 means commands that begins with")
    public static LinkedHashMap<String, String> BannedCommands = new LinkedHashMap<>();

    @Config.List
    public static List<String> IgnoredCommands = Arrays.asList("help", "?");

    @Config.List
    public static List<String> ReportedCommands = Arrays.asList("HxC god", "HxC fly");
}