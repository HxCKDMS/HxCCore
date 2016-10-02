package hxckdms.hxccore;

import hxckdms.hxccore.events.EventChat;
import hxckdms.hxccore.proxy.IProxy;
import hxckdms.hxccore.registry.command.CommandRegistry;
import hxckdms.hxccore.utilities.Logger;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

import java.io.File;

import static hxckdms.hxccore.libraries.Constants.*;

@Mod(modid = MOD_ID, name = MOD_NAME, version = VERSION, dependencies = DEPENDENCIES)
public class HxCCore {
    public static File modConfigDir;

    @Mod.Instance(MOD_ID)
    public static HxCCore instance;

    @SidedProxy(clientSide = "hxckdms.hxccore.proxy.ClientProxy", serverSide = "hxckdms.hxccore.proxy.ServerProxy")
    public static IProxy proxy;

    @Mod.EventHandler
    public void preInitialization(FMLPreInitializationEvent event) {
        modConfigDir = new File(event.getModConfigurationDirectory(), "HxCKDMS");

        CommandRegistry.registerCommands(event);

        proxy.preInit(event);
        Logger.info("HxCKDMS Core has finished the pre-initialization process.", MOD_NAME);
    }

    @Mod.EventHandler
    public void initialization(FMLInitializationEvent event) {
        proxy.init(event);
        MinecraftForge.EVENT_BUS.register(new EventChat());

        Logger.info("HxCKDMS Core has finished the initialization process.", MOD_NAME);
    }

    @Mod.EventHandler
    public void postInitialization(FMLPostInitializationEvent event) {
        proxy.postInit(event);
        Logger.info("HxCKDMS Core has finished the post-initialization process.", MOD_NAME);
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        CommandRegistry.initializeCommands(event);
    }
}
