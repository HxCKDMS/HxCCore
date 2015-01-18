package HxCKDMS.HxCCore.Configs;

import net.minecraftforge.common.config.Configuration;

public class Config
{
    public static boolean DebugMode;

    public static boolean CooldownDisable;
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

    public static Integer TpaTimeout;

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
        CooldownDisable = config.getBoolean("XPCooldown", "Features", true, "True means you can absorb more XP per second, Aprox. 2x as much");
        XPBuffs = config.getBoolean("XPBuffs", "Features", true, "Do you want to enable XP Buffs? (Only works when HxCSkills isn't installed.)");

        config.addCustomCategoryComment("Limits", "Any limitations are applied to HxCSkills as well");
        HPMax = config.getInt("MaxHP", "Limits", 100, 10, 2147000000, "Don't Exceed 100 without Tinkers or a mod that changes Health Bar.");
        DMMax = config.getInt("MaxDMG", "Limits", 100, 10, 2147000000, "The higher the number the more Max Damage!");

        config.addCustomCategoryComment("Permissions", "These Permissions names are up to you what you want them called!");
        PermLevel0Name = config.getString("PermLevel0Name", "Permissions", "Default", "Change this to anything you want as the group name, (Permissiosn Level 0)");
        PermLevel1Name = config.getString("PermLevel1Name", "Permissions", "Friend", "Change this to anything you want as the group name, (Permissiosn Level 1)");
        PermLevel2Name = config.getString("PermLevel2Name", "Permissions", "Helper", "Change this to anything you want as the group name, (Permissiosn Level 2)");
        PermLevel3Name = config.getString("PermLevel3Name", "Permissions", "Moderator", "Change this to anything you want as the group name, (Permissiosn Level 3)");
        PermLevel4Name = config.getString("PermLevel4Name", "Permissions", "Admin", "Change this to anything you want as the group name, (Permissiosn Level 4)");
        PermLevel5Name = config.getString("PermLevel5Name", "Permissions", "Owner", "Change this to anything you want as the group name, (Permissiosn Level 5)");

        config.addCustomCategoryComment("Permissions", "These Permissions colors are up to your preferences!");
        PermLevel0Color = config.getString("PermLevel0Color", "Permissions", "f", "", derp);
        PermLevel1Color = config.getString("PermLevel1Color", "Permissions", "2", "", derp);
        PermLevel2Color = config.getString("PermLevel2Color", "Permissions", "e", "", derp);
        PermLevel3Color = config.getString("PermLevel3Color", "Permissions", "9", "", derp);
        PermLevel4Color = config.getString("PermLevel4Color", "Permissions", "6", "", derp);
        PermLevel5Color = config.getString("PermLevel5Color", "Permissions", "4", "", derp);

        config.addCustomCategoryComment("Commands", "These command options specify specific values for commands.");
        TpaTimeout = config.getInt("TpaTimeout", "Commands", 600, 1200, 72000, "Sets the amount of ticks it takes for a tpa request to time out.");

        if(config.hasChanged())
        {
            config.save();
        }
    }
}
