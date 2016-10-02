package hxckdms.hxccore;

import hxckdms.hxcconfig.HxCConfig;
import hxckdms.hxccore.configs.Configuration;
import hxckdms.hxccore.events.EventChat;
import hxckdms.hxccore.network.CodersCheck;
import hxckdms.hxccore.proxy.IProxy;
import hxckdms.hxccore.registry.command.CommandRegistry;
import hxckdms.hxccore.utilities.Logger;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

import java.io.File;
import java.lang.reflect.Field;

import static hxckdms.hxccore.libraries.Constants.*;
import static hxckdms.hxccore.libraries.GlobalVariables.modConfigDir;

@Mod(modid = MOD_ID, name = MOD_NAME, version = VERSION, dependencies = DEPENDENCIES)
public class HxCCore {
    private static final Thread codersCheckThread = new Thread(new CodersCheck());

    @Mod.Instance(MOD_ID)
    public static HxCCore instance;

    @SidedProxy(clientSide = "hxckdms.hxccore.proxy.ClientProxy", serverSide = "hxckdms.hxccore.proxy.ServerProxy")
    public static IProxy proxy;

    @Mod.EventHandler
    public void preInitialization(FMLPreInitializationEvent event) {
        modConfigDir = new File(event.getModConfigurationDirectory(), "HxCKDMS");


        codersCheckThread.setName("Coders check thrad");
        codersCheckThread.start();

        HxCConfig config = new HxCConfig(Configuration.class, "HxCCore", modConfigDir, "cfg", MOD_NAME);
        config.initConfiguration();

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
    public void postInitialization(FMLPostInitializationEvent event) throws NoSuchFieldException {
        Field field = TextFormatting.class.getDeclaredField("formattingCode");
        field.setAccessible(true);


        proxy.postInit(event);
        Logger.info("HxCKDMS Core has finished the post-initialization process.", MOD_NAME);
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        CommandRegistry.initializeCommands(event);
    }
}
