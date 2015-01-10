package HxCKDMS.HxCCore;

import net.minecraftforge.common.config.Configuration;

public class Config
{
    public boolean DebugMode;

    public boolean XPBuffs;
    public static int HPMax;
    public static int DMMax;
    public static int LMPer;
    public static double HPPer;
    public static double DMPer;

    public Config(Configuration config)
    {
        config.load();

        DebugMode = config.get("DEBUG", "Debug Mode Enable?", false).getBoolean(false);

        XPBuffs = config.get("Features", "XP Level Buffs Enable?", true).getBoolean(true);
        HPMax = config.get("Features", "HP Max", 100, "Don't Exceed 100 without Tinkers or a mod that changes Health Bar").getInt();
        DMMax = config.get("Features", "DM Max", 100, "Don't Exceed 100 Unless u wanna 1 hit anything xD").getInt();

        LMPer = config.get("Features", "Level Boost", 5, "Boost Health and Damage every _ Levels.").getInt();

        HPPer = config.get("Features", "HP Boost", 0.1, "boost hp _ much. every time(1 heart per boost default (0.1))").getDouble();
        DMPer = config.get("Features", "DM Boost", 0.1, "boost damage _ much. every time.(0.1 hearts of damage per boost default(0.1))").getDouble();



        if(config.hasChanged())
        {
            config.save();
        }
    }
}
