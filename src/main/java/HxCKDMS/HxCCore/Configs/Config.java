package HxCKDMS.HxCCore.Configs;

import net.minecraftforge.common.config.Configuration;

public class Config
{
    public static boolean DebugMode;

    public static boolean CoolDownDisable;
    public static boolean XPBuffs;
    public boolean commands;
    public static int HPMax;
    public static int DMMax;

    public static String[] PermLevelName;
    public static String[] PermLevelColor;
    private static String[] PermDummy;
    public static int[] PermLevels;

    public static Integer TpaTimeout;

    public static String ChatFormat;
    public static String GroupFormat;
    public static String TagFormat;
    public static boolean GroupInChat;
    public static boolean TagInChat;
    public static boolean autoCrashReporterEnabled;

    public String derp[] = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    public Config(Configuration config)
    {
        config.load();

        DebugMode = config.get("DEBUG", "Debug Mode Enable?", false).getBoolean(false);

        config.addCustomCategoryComment("Features", "Any features are not required for anything else to work.");
        CoolDownDisable = config.getBoolean("XPCoolDown", "Features", true, "True means you can absorb more XP per second, Aprox. 2x as much");
        XPBuffs = config.getBoolean("XPBuffs", "Features", true, "Do you want to enable XP Buffs?");
        commands = config.getBoolean("EnableCommands?", "Features", true, "Enable all HxCCommands. (Disable if you don't want any new commands)");
        autoCrashReporterEnabled = config.getBoolean("CrashReporter", "Features", true, "Change this to false to disable automatic crash reporter when HxCKDMS Core is Possibly involved.");

        config.addCustomCategoryComment("Limits", "Any limitations are applied to HxCSkills as well");
        HPMax = config.getInt("MaxHP", "Limits", 100, 10, 2147000000, "Don't Exceed 100 without Tinkers or a mod that changes Health Bar.");
        DMMax = config.getInt("MaxDMG", "Limits", 100, 10, 2147000000, "The higher the number the more Max Damage!");

        config.addCustomCategoryComment("Permissions", "These Permissions names are up to you what you want them called!");
        PermLevelName = config.getStringList("GroupNames", "Permissions", new String[]{"Default", "Friend", "Helper", "Moderator", "Admin", "Owner"}, "Change this to anything you want as the group name, (Permission Level 0)");
        PermLevelColor = config.getStringList("PermLevelColor", "Permissions", new String[]{"f", "2", "e", "9", "6", "4"}, "PermLevel 0 - 5 Colours", derp);

        PermDummy = config.getStringList("DummyPermList", "Permissions", new String[]{"Burn", "Color", "Extinguish", "Feed", "Fly", "God", "Heal", "Home", "Kill", "Nick", "Repair", "RepairAll", "SetHome", "SetWarp", "Smite", "Warp", "ServerInfo", "Spawn", "TPA", "DrawSphere", "ClientInfo"}, "This is for below");
        PermLevels = config.get("Permissions", "PermLevels", new int[]{3, 1, 2, 2, 1, 3, 2, 0, 5, 1, 3, 4, 0, 4, 3, 0, 4, 0, 0, 5, 4}).getIntList();

        config.addCustomCategoryComment("Commands", "These command options specify specific values for commands.");
        TpaTimeout = config.getInt("TpaTimeout", "Commands", 1200, 600, 72000, "Sets the amount of ticks it takes for a tpa request to time out.");

        config.addCustomCategoryComment("Chat", "These configuration settings are chat specific.");
        ChatFormat = config.getString("ChatFormat", "Chat", "<%1$s> %2$s", "Chat format %1$s: username %2$s: message.");
        GroupFormat = config.getString("GroupFormat", "Chat", "&f[%1$s&f]", "Group format %g: group name");
        GroupInChat = config.getBoolean("ChatGroup", "Chat", true, "Change this to false if you don't want to see [Default|Owner|etc] beside player names");
        TagFormat = config.getString("TagFormat", "Chat", "&f[%1$s&f]", "Group format %g: tag");
        TagInChat = config.getBoolean("ChatTag", "Chat", true, "Change this to false if you don't want to see a tag next to hxc contributors in chat.");

        if(config.hasChanged())
        {
            config.save();
        }
    }
}
