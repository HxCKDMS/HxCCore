package HxCKDMS.HxCCore;

import net.minecraftforge.common.config.*;

public class Config
{
    public boolean DebugMode;

    public boolean XPHPBoost;

    public Config(Configuration config)
    {
        config.load();

        DebugMode = config.get("DEBUG", "Debug Mode Enable?", false).getBoolean(false);

        XPHPBoost = config.get("Features", "XP Level Health Boost Enable?", true).getBoolean(true);



        if(config.hasChanged())
        {
            config.save();
        }
    }
}
