package hxckdms.hxccore;

import hxckdms.hxcconfig.HxCConfig;
import hxckdms.hxccore.configs.Configuration;
import hxckdms.hxccore.events.CommandEvents;
import hxckdms.hxccore.events.EventChat;
import hxckdms.hxccore.events.EventNickSync;
import hxckdms.hxccore.network.CodersCheck;
import hxckdms.hxccore.network.MessageNameTagSync;
import hxckdms.hxccore.proxy.IProxy;
import hxckdms.hxccore.registry.command.CommandRegistry;
import hxckdms.hxccore.utilities.HxCPlayerInfoHandler;
import hxckdms.hxccore.utilities.Logger;
import hxckdms.hxccore.utilities.NBTFileHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;

import java.io.File;
import java.io.IOException;

import static hxckdms.hxccore.libraries.Constants.*;
import static hxckdms.hxccore.libraries.GlobalVariables.*;

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

        codersCheckThread.setName("Coders check thread");
        codersCheckThread.start();

        HxCConfig config = new HxCConfig(Configuration.class, "HxCCore", modConfigDir, "cfg", MOD_NAME);
        config.initConfiguration();

        network = NetworkRegistry.INSTANCE.newSimpleChannel(PACKET_CHANNEL_NAME);
        network.registerMessage(MessageNameTagSync.Handler.class, MessageNameTagSync.class, 0, Side.CLIENT);

        CommandRegistry.registerCommands(event);

        proxy.preInit(event);
        Logger.info("HxCKDMS Core has finished the pre-initialization process.", MOD_NAME);
    }

    @Mod.EventHandler
    public void initialization(FMLInitializationEvent event) {
        proxy.init(event);
        MinecraftForge.EVENT_BUS.register(new EventChat());
        MinecraftForge.EVENT_BUS.register(new CommandEvents());
        MinecraftForge.EVENT_BUS.register(new EventNickSync());
        MinecraftForge.EVENT_BUS.register(new NBTFileHandler.NBTSaveEvents());
        MinecraftForge.EVENT_BUS.register(new HxCPlayerInfoHandler.CustomPlayerDataEvents());


        Logger.info("HxCKDMS Core has finished the initialization process.", MOD_NAME);
    }

    @Mod.EventHandler
    public void postInitialization(FMLPostInitializationEvent event) {

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

        customWorldDataFile = new File(modWorldDir, "HxCWorld.dat");
        permissionDataFile = new File(modWorldDir, "HxC-Permissions.dat");

        try {
            if (!permissionDataFile.exists()) permissionDataFile.createNewFile();
            if (!customWorldDataFile.exists()) customWorldDataFile.createNewFile();
        } catch (IOException ignored) {}

        customWorldData = new NBTFileHandler("HxCWorldData", customWorldDataFile);
        permissionData = new NBTFileHandler("HxCPermissionData", permissionDataFile);
    }

    @Mod.EventHandler
    public void serverStarted(FMLServerStartedEvent event) {
        NBTFileHandler.loadCustomNBTFiles(true);
    }

    @Mod.EventHandler
    public void serverStopped(FMLServerStoppedEvent event) {
        NBTFileHandler.saveCustomNBTFiles(true);
        NBTFileHandler.getHandlers().parallelStream().forEach(NBTFileHandler::unRegister);
    }
}
