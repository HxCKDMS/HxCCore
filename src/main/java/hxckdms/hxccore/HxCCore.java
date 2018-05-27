package hxckdms.hxccore;

import hxckdms.hxcconfig.HxCConfig;
import hxckdms.hxcconfig.handlers.SpecialHandlers;
import hxckdms.hxccore.configs.Configuration;
import hxckdms.hxccore.configs.FakePlayerData;
import hxckdms.hxccore.configs.HomesConfigStorage;
import hxckdms.hxccore.network.MessageNameTagSync;
import hxckdms.hxccore.proxy.IProxy;
import hxckdms.hxccore.registry.CommandRegistry;
import hxckdms.hxccore.utilities.Kit;
import hxckdms.hxccore.utilities.Logger;
import hxckdms.hxccore.utilities.NBTFileHandler;
import net.minecraft.util.text.translation.LanguageMap;
import net.minecraft.world.GameRules;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.NetworkCheckHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static hxckdms.hxccore.libraries.Constants.*;
import static hxckdms.hxccore.libraries.GlobalVariables.*;

@Mod(modid = MOD_ID, name = MOD_NAME, version = VERSION , dependencies = DEPENDENCIES, acceptableRemoteVersions = "*")
public class HxCCore {
    @Mod.Instance(MOD_ID)
    public static HxCCore instance;

    @SidedProxy(clientSide = "hxckdms.hxccore.proxy.ClientProxy", serverSide = "hxckdms.hxccore.proxy.ServerProxy")
    public static IProxy proxy;

    @Mod.EventHandler
    public void preInitialization(FMLPreInitializationEvent event) {
        modConfigDir = new File(event.getModConfigurationDirectory(), "HxCKDMS");
        mainConfig = new HxCConfig(Configuration.class, "HxCCore", modConfigDir, "cfg", MOD_NAME);
        mainConfig.initConfiguration();
        Kit.initConfigs();
        proxy.preInit(event);

        network = NetworkRegistry.INSTANCE.newSimpleChannel(PACKET_CHANNEL_NAME);
        network.registerMessage(MessageNameTagSync.Handler.class, MessageNameTagSync.class, 0, Side.CLIENT);
        CommandRegistry.registerCommands(event);

        Logger.info("HxCKDMS Core has finished the pre-initialization process.", MOD_NAME);
    }

    @Mod.EventHandler
    public void initialization(FMLInitializationEvent event) {
        proxy.init(event);

        Logger.info("HxCKDMS Core has finished the initialization process.", MOD_NAME);
    }

    @Mod.EventHandler
    public void postInitialization(FMLPostInitializationEvent event) {
        langFile = LanguageMap.parseLangFile(this.getClass().getResourceAsStream("/assets/hxccore/lang/en_us.lang"));

        proxy.postInit(event);
        Logger.info("HxCKDMS Core has finished the post-initialization process.", MOD_NAME);
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        CommandRegistry.initializeCommands(event);

        if (!event.getServer().getEntityWorld().getGameRules().hasRule("HxC_XPBuffs")) event.getServer().getEntityWorld().getGameRules().addGameRule("HxC_XPBuffs", "true", GameRules.ValueType.BOOLEAN_VALUE);
        if (!event.getServer().getEntityWorld().getGameRules().hasRule("XPPickupCoolDown")) event.getServer().getEntityWorld().getGameRules().addGameRule("XPPickupCoolDown", "2", GameRules.ValueType.NUMERICAL_VALUE);

        modWorldDir = new File(event.getServer().getEntityWorld().getSaveHandler().getWorldDirectory(), "HxCData");
        if (!modWorldDir.exists()) modWorldDir.mkdirs();

        permissionDataFile = new File(modWorldDir, "HxC-Permissions.dat");
        customWorldDataFile = new File(modWorldDir, "HxCWorld.dat");

        try {
            if (!permissionDataFile.exists()) permissionDataFile.createNewFile();
            if (!customWorldDataFile.exists()) customWorldDataFile.createNewFile();
        } catch (IOException ignored) {}

        permissionData = new NBTFileHandler("HxCPermissionData", permissionDataFile);
        customWorldData = new NBTFileHandler("HxCWorldData", customWorldDataFile);

        if (Configuration.useTextStorageofHomes) {
            SpecialHandlers.registerSpecialClass(FakePlayerData.class);
            SpecialHandlers.registerSpecialClass(FakePlayerData.Warp.class);
            alternateHomesConfig = new HxCConfig(HomesConfigStorage.class, "HxCHomes", modConfigDir, "cfg", MOD_NAME);
            alternateHomesConfig.initConfiguration();
        }
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

    @NetworkCheckHandler
    public boolean networkCheck(Map<String, String> mods, Side side) {
        return !mods.containsKey("hxccore") || mods.get("hxccore").equals(VERSION);
    }
}
