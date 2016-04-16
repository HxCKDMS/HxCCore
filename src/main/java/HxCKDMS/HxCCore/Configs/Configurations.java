package HxCKDMS.HxCCore.Configs;

import HxCKDMS.HxCCore.HxCCore;
import HxCKDMS.HxCCore.api.Configuration.Config;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import static HxCKDMS.HxCCore.api.Configuration.Flags.overwrite;

@Config
public class Configurations {
    @Config.comment("Debug Mode Enable? Can cause lag and console spam!")
    public static boolean DebugMode;

    @Config.category("Features")
    @Config.comment("Enable all HxCCommands. (Disable if you don't want any new commands)")
    public static boolean enableCommands = true;

    @Config.category("Features")
    @Config.comment("Don't Exceed 100 without Tinkers or a mod that changes Health Bar.")
    public static int MaxHealth = 20;

    @Config.category("Features")
    @Config.comment("The higher the number the more Max Damage!")
    public static int MaxDamage = 11;

    @Config.category("Features")
    @Config.comment("Sets the amount of ticks it takes for a tpa request to time out.")
    public static Integer TpaTimeout = 600;

    @Config.category("Features")
    @Config.comment("How many levels are required per increment of buffs.")
    public static Integer XPBuffPerLevels = 5;

    public static List<Character> bannedColorCharacters = Arrays.asList('k', 'm', '4');

    @Config.category("Features")
    @Config.comment("Change this to false to disable automatic crash reporter when HxCKDMS Core is Possibly involved.")
    public static boolean autoCrashReporterEnabled = true;

    @Config.category("Permissions")
    @Config.comment("You can rename these... and the second part is colour. the third is number of homes (-1 = infinite) AND you can add more..")
    public static LinkedHashMap<String, String> Permissions = new LinkedHashMap<>();

    @Config.comment("HxC is labels given to special people. Group is the Server rank Name is nickname")
    public static LinkedHashMap<String, String> formats = new LinkedHashMap<>();

    public static boolean EnableGroupTagInChat = true, EnableHxCTagInChat = true, EnableColourInChat = true, versionCheck = true;

    @Config.category("DNT")
    @Config.comment("This is the file name of the last crash reported so the same crash-report doesn't get reported multiple times.")
    @Config.flags(overwrite)
    public static String lastCheckedCrash = "";

    private static void putValues() {
        if (Permissions.isEmpty()) {
            Permissions.put("Default", "f, 3, 0");
            Permissions.put("Helper", "e, 5, 512");
            Permissions.put("Moderator", "9, 10, 4096");
            Permissions.put("Admin", "6, 16, 32768");
            Permissions.put("Owner", "4, -1, -1");
        }
        if (formats.isEmpty()) {
            formats.put("ChatFormat", "HEADER MESSAGE");
            formats.put("PlayerNametagFormat", "HXC GROUP NAME");
            formats.put("GroupFormat", "&r[%1$s&r]");
            formats.put("HxCFormat", "&r[%1$s&r]");
            formats.put("BroadcastFormat", "&f[&6SERVER&f] &f<SENDER&f> &4MESSAGE");
        }
    }

    public static void preInitConfigs() {
        putValues();

        HxCCore.config.setCategoryComment("Permission", "Do not add a permission level requirement for a command if the permission level doesn't exist!");
        HxCCore.config.setCategoryComment("DNT", "DO NOT TOUCH!!!!!!!!!");
        HxCCore.config.initConfiguration();
        HxCCore.commandConfig.initConfiguration();
        //HxCCore.kitConfig.initConfiguration();
    }
}
