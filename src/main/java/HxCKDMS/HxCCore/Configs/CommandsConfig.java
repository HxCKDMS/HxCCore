package HxCKDMS.HxCCore.Configs;

import HxCKDMS.HxCCore.api.Configuration.Config;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

@Config
public class CommandsConfig {
    public static LinkedHashMap<String, Integer> CommandPermissions = new LinkedHashMap<>();

    @Config.retainOriginalValues
    public static LinkedHashMap<String, Boolean> EnabledCommands = new LinkedHashMap<>();

    @Config.comment("Add commands to this list to prevent their execution entirely... 0 means exact command, 1 means commands that begins with")
    public static LinkedHashMap<String, Integer> BannedCommands = new LinkedHashMap<>();

    public static List<String> IgnoredCommands = Arrays.asList("help", "?");

    public static List<String> ReportedCommands = Arrays.asList("HxC god", "HxC fly");
}