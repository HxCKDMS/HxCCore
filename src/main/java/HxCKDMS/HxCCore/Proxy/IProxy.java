package HxCKDMS.HxCCore.Proxy;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@SuppressWarnings("unused")
public interface IProxy {
    void preInit(FMLPreInitializationEvent event);
    void init(FMLInitializationEvent event);
    void postInit(FMLPostInitializationEvent event);
}
