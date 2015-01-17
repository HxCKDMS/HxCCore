package HxCKDMS.HxCCore.Configs;

import net.minecraftforge.common.config.Configuration;

public class PermissionsConfig
{

    public PermissionsConfig(Configuration config)
    {
        config.load();

        if(config.hasChanged())
        {
            config.save();
        }
    }
}
