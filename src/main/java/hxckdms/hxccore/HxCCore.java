package hxckdms.hxccore;

import hxckdms.hxcconfig.HxCConfig;
import hxckdms.hxccore.configs.Configuration;
import hxckdms.hxccore.events.EventChat;
import hxckdms.hxccore.network.CodersCheck;
import hxckdms.hxccore.proxy.IProxy;
import hxckdms.hxccore.registry.command.CommandRegistry;
import hxckdms.hxccore.utilities.Logger;
import hxckdms.hxccore.utilities.NBTFileHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;

import java.io.File;
import java.io.IOException;

import static hxckdms.hxccore.libraries.Constants.*;
import static hxckdms.hxccore.libraries.GlobalVariables.*;

@Mod(modid = MOD_ID, name = MOD_NAME, version = VERSION, dependencies = DEPENDENCIES)
public class HxCCore {
    private static final Thread codersCheckThread = new Thread(new CodersCheck());

    public static NBTFileHandler test;

    @Mod.Instance(MOD_ID)
    public static HxCCore instance;

    @SidedProxy(clientSide = "hxckdms.hxccore.proxy.ClientProxy", serverSide = "hxckdms.hxccore.proxy.ServerProxy")
    public static IProxy proxy;

    @Mod.EventHandler
    public void preInitialization(FMLPreInitializationEvent event) {
        modConfigDir = new File(event.getModConfigurationDirectory(), "HxCKDMS");


        codersCheckThread.setName("Coders check thread");
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

        proxy.postInit(event);
        Logger.info("HxCKDMS Core has finished the post-initialization process.", MOD_NAME);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        server = event.getServer();

        CommandRegistry.initializeCommands(event);

        modWorldDir = new File(event.getServer().getEntityWorld().getSaveHandler().getWorldDirectory(), "HxCData");
        if (!modWorldDir.exists()) modWorldDir.mkdirs();

        customWorldData = new File(modWorldDir, "HxCWorld.dat");
        permissionData = new File(modWorldDir, "HxC-Permissions.dat");

        test = new NBTFileHandler(customWorldData);

        try {
            if (!permissionData.exists()) permissionData.createNewFile();
            if (!customWorldData.exists()) customWorldData.createNewFile();
        } catch (IOException ignored) {}
    }

    @Mod.EventHandler
    public void serverStarted(FMLServerStartedEvent event) {
        //NBTFileHandler.loadCustomNBTFiles();
    }

    @Mod.EventHandler
    public void serverStopped(FMLServerStoppedEvent event) {
        NBTFileHandler.saveCustomNBTFiles();
    }
}
