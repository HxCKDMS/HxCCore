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

    public static int BurnPL;
    public static int ColorPL;
    public static int ExtinguishPL;
    public static int FeedPL;
    public static int FlyPL;
    public static int GodPL;
    public static int HealPL;
    public static int HomePL;
    public static int KillPL;
    public static int NickPL;
    public static int RepairPL;
    public static int RepairAllPL;
    public static int SetHomePL;
    public static int SetWarpPL;
    public static int SmitePL;
    public static int WarpPL;

    public static Integer TpaTimeout;

    public static String ChatFormat;
    public static String GroupFormat;
    public static String TagFormat;
    public static boolean GroupInChat;
    public static boolean TagInChat;

    public String derp[] = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    public Config(Configuration config)
    {
        config.load();

        DebugMode = config.get("DEBUG", "Debug Mode Enable?", false).getBoolean(false);

        config.addCustomCategoryComment("Features", "Any features are not required for anything else to work.");
        CoolDownDisable = config.getBoolean("XPCoolDown", "Features", true, "True means you can absorb more XP per second, Aprox. 2x as much");
        XPBuffs = config.getBoolean("XPBuffs", "Features", true, "Do you want to enable XP Buffs?");
        commands = config.getBoolean("EnableCommands?", "Features", true, "Enable all HxCCommands. (Disable if you don't want any new commands)");

        config.addCustomCategoryComment("Limits", "Any limitations are applied to HxCSkills as well");
        HPMax = config.getInt("MaxHP", "Limits", 100, 10, 2147000000, "Don't Exceed 100 without Tinkers or a mod that changes Health Bar.");
        DMMax = config.getInt("MaxDMG", "Limits", 100, 10, 2147000000, "The higher the number the more Max Damage!");

        config.addCustomCategoryComment("Permissions", "These Permissions names are up to you what you want them called!");
        PermLevelName = config.getStringList("GroupNames", "Permissions", new String[]{"Default", "Friend", "Helper", "Moderator", "Admin", "Owner"}, "Change this to anything you want as the group name, (Permission Level 0)");
        PermLevelColor = config.getStringList("PermLevelColor", "Permissions", new String[]{"f","2","e","9", "6", "4"}, "PermLevel 0 - 5 Colours", derp);

        BurnPL = config.getInt("Burn", "Permissions", 3, 0, 5, "The integer you set this to corresponds to the Permissions Level of the same number");
        ColorPL = config.getInt("Color", "Permissions", 1, 0, 5, "The integer you set this to corresponds to the Permissions Level of the same number");
        ExtinguishPL = config.getInt("Extinguish", "Permissions", 2, 0, 5, "The integer you set this to corresponds to the Permissions Level of the same number");
        FeedPL = config.getInt("Feed", "Permissions", 2, 0, 5, "The integer you set this to corresponds to the Permissions Level of the same number");
        FlyPL = config.getInt("Fly", "Permissions", 1, 0, 5, "The integer you set this to corresponds to the Permissions Level of the same number");
        GodPL = config.getInt("God", "Permissions", 3, 0, 5, "The integer you set this to corresponds to the Permissions Level of the same number");
        HealPL = config.getInt("Heal", "Permissions", 2, 0, 5, "The integer you set this to corresponds to the Permissions Level of the same number");
        HomePL = config.getInt("Home", "Permissions", 0, 0, 5, "The integer you set this to corresponds to the Permissions Level of the same number");
        KillPL = config.getInt("Kill", "Permissions", 5, 0, 5, "The integer you set this to corresponds to the Permissions Level of the same number");
        NickPL = config.getInt("Nick", "Permissions", 1, 0, 5, "The integer you set this to corresponds to the Permissions Level of the same number");
        RepairPL = config.getInt("Repair", "Permissions", 3, 0, 5, "The integer you set this to corresponds to the Permissions Level of the same number");
        RepairAllPL = config.getInt("RepairAll", "Permissions", 4, 0, 5, "The integer you set this to corresponds to the Permissions Level of the same number");
        SetHomePL = config.getInt("SetHome", "Permissions", 0, 0, 5, "The integer you set this to corresponds to the Permissions Level of the same number");
        SetWarpPL = config.getInt("SetWarp", "Permissions", 4, 0, 5, "The integer you set this to corresponds to the Permissions Level of the same number");
        SmitePL = config.getInt("Smite", "Permissions", 3, 0, 5, "The integer you set this to corresponds to the Permissions Level of the same number");
        WarpPL = config.getInt("Warp", "Permissions", 0, 0, 5, "The integer you set this to corresponds to the Permissions Level of the same number");

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
