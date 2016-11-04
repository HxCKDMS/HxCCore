package hxckdms.hxccore.proxy;

import hxckdms.hxccore.configs.Configuration;
import hxckdms.hxccore.event.EventCapeRender;
import hxckdms.hxccore.event.EventDebugOverlay;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

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
