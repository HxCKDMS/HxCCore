package HxCKDMS.HxCCore.Configs;

import HxCKDMS.HxCCore.api.Configuration.Config;

import java.util.Arrays;
import java.util.HashMap;
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

    @Config.Boolean(description = "Enable all HxCCommands. (Disable if you don't want any new commands)", category = "Features")
    public static boolean enableCommands = true;
    @Config.Integer(description = "Don't Exceed 100 without Tinkers or a mod that changes Health Bar.", category = "Features")
    public static int HPMax = 100;
    @Config.Integer(description = "The higher the number the more Max Damage!", category = "Features")
    public static int DMMax = 100;
    @Config.Integer(description = "Sets the amount of ticks it takes for a tpa request to time out.", category = "Features")
    public static Integer TpaTimeout = 100;

    @Config.List
    public static List<Character> bannedColorCharacters = Arrays.asList('k', 'm', '4');

//"Burn", "Color", "Extinguish", "Feed", "Fly", "God", "Heal", "Home", "Kill", "Nick", "Repair", "RepairAll", "SetHome", "SetWarp", "Smite", "Warp", "ServerInfo", "Spawn", "TPA", "DrawSphere", "ClientInfo"
    @Config.Boolean(description = "Change this to false to disable automatic crash reporter when HxCKDMS Core is Possibly involved.", category = "Features")
    public static boolean autoCrashReporterEnabled = true;
    
    @Config.Map(category = "Commands")
    public static LinkedHashMap<String, Integer> commands = new LinkedHashMap<>();
    @Config.Map(category = "Permissions", description = "You can rename these... and the second part is colour. the third is number of homes (-1 = infinite)")
    public static LinkedHashMap<String, String> perms = new LinkedHashMap<>();
    @Config.Map(description = "%1$s: username %2$s: message. %g Group Tag")
    public static HashMap<String, String> formats = new HashMap<>();

    @Config.Boolean
    public static boolean EnableGroupTagInChat = true, EnableHxCTagInChat = true, EnableColourInChat = true;

    @Config.String(description = "This is the file name of the last crash reported so the same crash-report doesn't get reported multiple times.", category = "DNT",forceReset = true)
    public static String lastCheckedCrash = "";
    
    static {
        commands.put("Burn", 3);
        commands.put("Color", 1);
        commands.put("Extinguish", 2);
        commands.put("Feed", 2);
        commands.put("Fly", 1);
        commands.put("God", 3);
        commands.put("Heal", 2);
        commands.put("Home", 0);
        commands.put("Kill", 5);
        commands.put("Nick", 1);
        commands.put("Repair", 3);
        commands.put("RepairAll", 4);
        commands.put("SetHome", 0);
        commands.put("SetWarp", 4);
        commands.put("Smite", 3);
        commands.put("Warp", 0);
        commands.put("ServerInfo", 4);
        commands.put("Spawn", 0);
        commands.put("TPA", 0);
        commands.put("DrawSphere", 5);
        commands.put("ClientInfo", 4);
        commands.put("AFK", 0);
        commands.put("Cannon", 3);
        commands.put("Drain", 4);
        commands.put("Broadcast", 4);
        commands.put("Hat", 1);
        commands.put("Path", 4);

        perms.put("Default", "f 3");
        perms.put("Friend", "2 5");
        perms.put("Helper", "e 8");
        perms.put("Moderator", "9 10");
        perms.put("Admin", "6 16");
        perms.put("Owner", "4 -1");

        formats.put("ChatFormat", "<%1$s> %2$s");
        formats.put("HxCTag", "&f[%1$s&f]");
        formats.put("GroupTag", "&f[%1$s&f]");
        formats.put("BroadcastVariable", "[&6SERVER&f] <SENDER> &4MESSAGE");
    }
}
