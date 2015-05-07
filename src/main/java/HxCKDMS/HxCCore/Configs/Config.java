package HxCKDMS.HxCCore.Configs;

import net.minecraftforge.common.config.Configuration;

public class Config
{
    public static boolean DebugMode;

    public static boolean CoolDownDisable;
    public static boolean XPBuffs;
    public static int HPMax;
    public static int DMMax;

    public static String PermLevel0Name;
    public static String PermLevel1Name;
    public static String PermLevel2Name;
    public static String PermLevel3Name;
    public static String PermLevel4Name;
    public static String PermLevel5Name;

    public static String PermLevel0Color;
    public static String PermLevel1Color;
    public static String PermLevel2Color;
    public static String PermLevel3Color;
    public static String PermLevel4Color;
    public static String PermLevel5Color;

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

    public String derp[] = new String[16];

    public Config(Configuration config)
    {
        config.load();
        
        derp[0] = "0";
        derp[1] = "1";
        derp[2] = "2";
        derp[3] = "3";
        derp[4] = "4";
        derp[5] = "5";
        derp[6] = "6";
        derp[7] = "7";
        derp[8] = "8";
        derp[9] = "9";
        derp[10] = "a";
        derp[11] = "b";
        derp[12] = "c";
        derp[13] = "d";
        derp[14] = "e";
        derp[15] = "f";
        
        DebugMode = config.get("DEBUG", "Debug Mode Enable?", false).getBoolean(false);

        config.addCustomCategoryComment("Features", "Any features are not required for anything else to work.");
        CoolDownDisable = config.getBoolean("XPCoolDown", "Features", true, "True means you can absorb more XP per second, Aprox. 2x as much");
        XPBuffs = config.getBoolean("XPBuffs", "Features", true, "Do you want to enable XP Buffs? (Only works when HxCSkills isn't installed.)");

        config.addCustomCategoryComment("Limits", "Any limitations are applied to HxCSkills as well");
        HPMax = config.getInt("MaxHP", "Limits", 100, 10, 2147000000, "Don't Exceed 100 without Tinkers or a mod that changes Health Bar.");
        DMMax = config.getInt("MaxDMG", "Limits", 100, 10, 2147000000, "The higher the number the more Max Damage!");

        config.addCustomCategoryComment("Permissions", "These Permissions names are up to you what you want them called!");
        PermLevel0Name = config.getString("PermLevel0", "Permissions", "Default", "Change this to anything you want as the group name, (Permission Level 0)");
        PermLevel1Name = config.getString("PermLevel1", "Permissions", "Friend", "Change this to anything you want as the group name, (Permission Level 1)");
        PermLevel2Name = config.getString("PermLevel2", "Permissions", "Helper", "Change this to anything you want as the group name, (Permission Level 2)");
        PermLevel3Name = config.getString("PermLevel3", "Permissions", "Moderator", "Change this to anything you want as the group name, (Permission Level 3)");
        PermLevel4Name = config.getString("PermLevel4", "Permissions", "Admin", "Change this to anything you want as the group name, (Permission Level 4)");
        PermLevel5Name = config.getString("PermLevel5", "Permissions", "Owner", "Change this to anything you want as the group name, (Permission Level 5)");

        PermLevel0Color = config.getString("PermLevel0Color", "Permissions", "f", "", derp);
        PermLevel1Color = config.getString("PermLevel1Color", "Permissions", "2", "", derp);
        PermLevel2Color = config.getString("PermLevel2Color", "Permissions", "e", "", derp);
        PermLevel3Color = config.getString("PermLevel3Color", "Permissions", "9", "", derp);
        PermLevel4Color = config.getString("PermLevel4Color", "Permissions", "6", "", derp);
        PermLevel5Color = config.getString("PermLevel5Color", "Permissions", "4", "", derp);

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
