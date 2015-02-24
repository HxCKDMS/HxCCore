package HxCKDMS.HxCCore.Proxy;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;

public abstract class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {
    }

    /** Returns proxy side and therefore mod instance side. **/
    public Side getSide() {
        return this instanceof ClientProxy ? Side.CLIENT : Side.SERVER;
    }
}
