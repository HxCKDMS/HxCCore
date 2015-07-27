package HxCKDMS.HxCCore.Configs;

import HxCKDMS.HxCCore.api.Configuration.Config;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unused")
public class Configurations {
//    config.addCustomCategoryComment("Chat", "These configuration settings are chat specific.");
//    config.addCustomCategoryComment("Commands", "These command options specify specific values for commands.");
//    config.addCustomCategoryComment("Permissions", "These Permissions names are up to you what you want them called!");
//    config.addCustomCategoryComment("Limits", "Any limitations are applied to HxCSkills as well");
//    config.addCustomCategoryComment("Features", "Any features are not required for anything else to work.");

    @Config.Boolean(description = "Debug Mode Enable? Can cause lag and console spam!")
    public static boolean DebugMode;

    @Config.Boolean(description = "True means you can absorb more XP per second, Aprox. 2x as much")
    public static boolean CoolDownDisable = true;
    @Config.Boolean(description = "Do you want to enable XP Buffs?")
    public static boolean XPBuffs = true;

    @Config.Boolean(description = "Enable all HxCCommands. (Disable if you don't want any new commands)")
    public static boolean commands = true;
    @Config.Integer(description = "Don't Exceed 100 without Tinkers or a mod that changes Health Bar.")
    public static int HPMax = 100;
    @Config.Integer(description = "The higher the number the more Max Damage!")
    public static int DMMax = 100;
    @Config.Integer(description = "Sets the amount of ticks it takes for a tpa request to time out.")
    public static Integer TpaTimeout = 100;

    @Config.List(description = "Permission level Names 0, 1, 2, 3, 4, 5")
    public static List<String> PermLevelName = Arrays.asList("Default", "Friend", "Helper", "Moderator", "Admin", "Owner");
    @Config.List(description = "Permission level Colours 0, 1, 2, 3, 4, 5")
    public static List<String> PermLevelColor = Arrays.asList("f", "2", "e", "9", "6", "4");

    private static String meh = System.lineSeparator();

    @Config.List(description = "Permission levels for commands \n # \"Burn\", \"Color\", \"Extinguish\", \"Feed\", \"Fly\", \"God\", \"Heal\", \"Home\", \"Kill\", \"Nick\", \"Repair\", \"RepairAll\", \"SetHome\", \"SetWarp\", \"Smite\", \"Warp\", \"ServerInfo\", \"Spawn\", \"TPA\", \"DrawSphere\", \"ClientInfo\"")
    public static List<Integer> PermLevels = Arrays.asList(3, 1, 2, 2, 1, 3, 2, 0, 5, 1, 3, 4, 0, 4, 3, 0, 4, 0, 0, 5, 4);
//"Burn", "Color", "Extinguish", "Feed", "Fly", "God", "Heal", "Home", "Kill", "Nick", "Repair", "RepairAll", "SetHome", "SetWarp", "Smite", "Warp", "ServerInfo", "Spawn", "TPA", "DrawSphere", "ClientInfo"

    @Config.String(description = "Chat format %1$s: username %2$s: message.")
    public static String ChatFormat = "<%1$s> %2$s";
    @Config.String(description = "Group format %g: group name")
    public static String GroupFormat = "&f[%1$s&f]";
    @Config.String(description = "Group format %g: tag")
    public static String TagFormat = "&f[%1$s&f]";

    @Config.Boolean(description = "Show group in chat [*****]name : message")
    public static boolean GroupInChat = true;
    @Config.Boolean(description = "Show tag in chat [HxC***][Admin]name : message")
    public static boolean TagInChat = true;
    @Config.Boolean(description = "Change this to false to disable automatic crash reporter when HxCKDMS Core is Possibly involved.")
    public static boolean autoCrashReporterEnabled = true;
/*
    static {
        test6.put("test1", 1);
        test6.put("test2", 2);
        test6.put("test3", 3);
        test6.put("test4", 4);
        test6.put("test5", 5);
        test6.put("test6", 6);
    }*/
}
