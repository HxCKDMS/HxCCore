package HxCKDMS.HxCCore;

import net.minecraftforge.common.config.Configuration;

public class Config
{
    public boolean DebugMode;

    public boolean XPBuffs;
    public static int HPMax;
    public static int DMMax;

    public Config(Configuration config)
    {
        config.load();

        DebugMode = config.get("DEBUG", "Debug Mode Enable?", false).getBoolean(false);

        XPBuffs = config.get("Features", "XP Level Buffs Enable?", true).getBoolean(true);
        HPMax = config.get("Features", "HP Max", 100, "Don't Exceed 100 without Tinkers or a mod that changes Health Bar").getInt();
        DMMax = config.get("Features", "DM Max", 100, "Don't Exceed 100 Unless u wanna 1 hit anything xD").getInt();




        if(config.hasChanged())
        {
            config.save();
        }
    }
}
