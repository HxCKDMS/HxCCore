package HxCKDMS.HxCCore.Proxy;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class ServerProxy implements IProxy {
    @Override
    public void preInit(FMLPreInitializationEvent event) {}

    @Override
    public void init(FMLInitializationEvent event){}

    @Override
    @SuppressWarnings("unchecked")
    public void postInit(FMLPostInitializationEvent event) {}

}
