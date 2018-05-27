package hxckdms.hxccore.proxy;

import hxckdms.hxccore.configs.Configuration;
import hxckdms.hxccore.event.CommandEvents;
import hxckdms.hxccore.event.CommonEvents;
import hxckdms.hxccore.network.CapesDownload;
import hxckdms.hxccore.network.CodersCheck;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public abstract class CommonProxy implements IProxy {
    private static final Thread codersCheckThread = new Thread(new CodersCheck()), capesDownloadThread = new Thread(new CapesDownload());

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        codersCheckThread.setName("Coders check thread");
        codersCheckThread.start();
        capesDownloadThread.setName("Cape Down thread");
        if (Configuration.enableCapes) capesDownloadThread.start();
    }

    @Override
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new CommonEvents());
        MinecraftForge.EVENT_BUS.register(new CommandEvents());
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {

    }
}
