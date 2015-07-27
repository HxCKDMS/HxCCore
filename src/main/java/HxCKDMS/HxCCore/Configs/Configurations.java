package HxCKDMS.HxCCore.Configs;

import HxCKDMS.HxCCore.api.Configuration.Config;

import java.util.HashMap;
import java.util.LinkedHashMap;

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
    public static boolean CoolDownDisable = true, test = false, fart = false, lepoot = true;
    @Config.Boolean(description = "Do you want to enable XP Buffs?")
    public static boolean XPBuffs = true;

    @Config.Boolean(description = "Enable all HxCCommands. (Disable if you don't want any new commands)")
    public static boolean enableCommands = true;
    @Config.Integer(description = "Don't Exceed 100 without Tinkers or a mod that changes Health Bar.")
    public static int HPMax = 100;
    @Config.Integer(description = "The higher the number the more Max Damage!")
    public static int DMMax = 100;
    @Config.Integer(description = "Sets the amount of ticks it takes for a tpa request to time out.")
    public static Integer TpaTimeout = 100;

//"Burn", "Color", "Extinguish", "Feed", "Fly", "God", "Heal", "Home", "Kill", "Nick", "Repair", "RepairAll", "SetHome", "SetWarp", "Smite", "Warp", "ServerInfo", "Spawn", "TPA", "DrawSphere", "ClientInfo"
    @Config.Boolean(description = "Change this to false to disable automatic crash reporter when HxCKDMS Core is Possibly involved.")
    public static boolean autoCrashReporterEnabled = true;
    
    @Config.Map(description = "")
    public static LinkedHashMap<String, Integer> commands = new LinkedHashMap<>();
    @Config.Map(description = "")
    public static LinkedHashMap<String, String> perms = new LinkedHashMap<>();
    @Config.Map(description = "%1$s: username %2$s: message. %g Group Tag")
    public static HashMap<String, String> formats = new HashMap<>();

    @Config.Boolean()
    public static boolean EnableGroupTagInChat = true, EnableHxCTagInChat = true, EnableColourInChat = true;
    
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

        perms.put("Default", "f");
        perms.put("Friend", "2");
        perms.put("Helper", "e");
        perms.put("Moderator", "9");
        perms.put("Admin", "6");
        perms.put("Owner", "4");

        formats.put("ChatFormat", "<%1$s> %2$s");
        formats.put("HxCTag", "&f[%1$s&f]");
        formats.put("GroupTag", "&f[%1$s&f]");
    }
}
