package HxCKDMS.HxCCore.Configs;

import HxCKDMS.HxCCore.HxCCore;
import HxCKDMS.HxCCore.api.Configuration.Category;
import HxCKDMS.HxCCore.api.Configuration.Config;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

@SuppressWarnings("unused")
public class Configurations {
    @Config.Boolean(description = "Debug Mode Enable? Can cause lag and console spam!")
    public static boolean DebugMode;

    @Config.Boolean(description = "True means you can absorb more XP per second, Aprox. 2x as much", category = "Features")
    public static boolean CoolDownDisable = true;
    @Config.Boolean(description = "Do you want to enable XP Buffs?", category = "Features")
    public static boolean XPBuffs = true;
    @Config.Boolean(description = "Enable invisibility, god, slowness, weakness for afk players?", category = "Features")
    public static boolean afkExtras = true;

    @Config.Boolean(description = "Enable all HxCCommands. (Disable if you don't want any new commands)", category = "Features")
    public static boolean enableCommands = true;
    @Config.Integer(description = "Don't Exceed 100 without Tinkers or a mod that changes Health Bar.", category = "Features")
    public static int HPMax = 40;
    @Config.Integer(description = "The higher the number the more Max Damage!", category = "Features")
    public static int DMMax = 40;
    @Config.Integer(description = "Sets the amount of ticks it takes for a tpa request to time out.", category = "Features")
    public static Integer TpaTimeout = 600;

    @Config.List
    public static List<Character> bannedColorCharacters = Arrays.asList('k', 'm', '4');

    //"Burn", "Color", "Extinguish", "Feed", "Fly", "God", "Heal", "Home", "Kill", "Nick", "Repair", "RepairAll", "SetHome", "SetWarp", "Smite", "Warp", "ServerInfo", "Spawn", "TPA", "DrawSphere", "ClientInfo"
    @Config.Boolean(description = "Change this to false to disable automatic crash reporter when HxCKDMS Core is Possibly involved.", category = "Features")
    public static boolean autoCrashReporterEnabled = true;

    @Config.Map(category = "Permissions", description = "You can rename these... and the second part is colour. the third is number of homes (-1 = infinite) AND you can add more..")
    public static LinkedHashMap<String, String> Permissions = new LinkedHashMap<>();
    @Config.Map(description = "%1$s: username %2$s: message. %g Group Tag")
    public static LinkedHashMap<String, String> formats = new LinkedHashMap<>();

    @Config.Boolean
    public static boolean EnableGroupTagInChat = true, EnableHxCTagInChat = true, EnableColourInChat = true;

    @Config.String(description = "This is the file name of the last crash reported so the same crash-report doesn't get reported multiple times.", category = "DNT", forceReset = true)
    public static String lastCheckedCrash = "";

    static {
        formats.put("ChatFormat", "<%1$s> %2$s");
        formats.put("HxCTag", "&f[%1$s&f]");
        formats.put("GroupTag", "&f[%1$s&f]");
        formats.put("BroadcastVariable", "[&6SERVER&f] <SENDER> &4MESSAGE");
    }

    public static void updatePerms() {
        if (Permissions.isEmpty()) {
            Permissions.put("Default", "f 3");
            Permissions.put("Friend", "2 5");
            Permissions.put("Helper", "e 8");
            Permissions.put("Moderator", "9 10");
            Permissions.put("Admin", "6 16");
            Permissions.put("Owner", "4 -1");
        }
    }

    public static void preInitConfigs() {
        HxCCore.hxCConfig.registerCategory(new Category("General"));
        HxCCore.hxCConfig.registerCategory(new Category("Features"));
        HxCCore.hxCConfig.registerCategory(new Category("Permissions", "Do not add a permission level requirement for a command if the permission level doesn't exist!"));
        HxCCore.hxCConfig.registerCategory(new Category("DNT", "DO NOT TOUCH!!!!!!!!!"));

        HxCCore.hxCConfig.handleConfig(Configurations.class, HxCCore.HxCConfigFile);

        HxCCore.commandCFG.registerCategory(new Category("General"));
        HxCCore.commandCFG.handleConfig(CommandsConfig.class, HxCCore.commandCFGFile);

        HxCCore.kits.registerCategory(new Category("General"));
        HxCCore.kits.handleConfig(Kits.class, HxCCore.kitsFile);

        updatePerms();
        HxCCore.hxCConfig.handleConfig(Configurations.class, HxCCore.HxCConfigFile);
    }
}