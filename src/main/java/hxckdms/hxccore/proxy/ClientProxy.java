package hxckdms.hxccore.proxy;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import hxckdms.hxccore.configs.Configuration;
import hxckdms.hxccore.event.EventCapeRender;
import hxckdms.hxccore.event.EventDebugOverlay;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy implements IProxy {
    @Override
    public void preInit(FMLPreInitializationEvent event) {

    }

    @Override
    public void init(FMLInitializationEvent event) {
        if (Configuration.enableCapes) MinecraftForge.EVENT_BUS.register(new EventCapeRender());
        MinecraftForge.EVENT_BUS.register(EventDebugOverlay.class);
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {

    }
}
