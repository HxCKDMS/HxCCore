package hxckdms.hxccore;

import net.minecraftforge.fml.common.Mod;

import static hxckdms.hxccore.libraries.Constants.*;

@Mod(modid = MOD_ID, name = MOD_NAME, version = VERSION, dependencies = DEPENDENCIES)
public class HxCCore {
    @Mod.Instance(MOD_ID)
    public static HxCCore instance;
}
