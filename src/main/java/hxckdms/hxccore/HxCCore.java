package hxckdms.hxccore;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.network.NetworkCheckHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import hxckdms.hxcconfig.HxCConfig;
import hxckdms.hxcconfig.handlers.SpecialHandlers;
import hxckdms.hxccore.configs.Configuration;
import hxckdms.hxccore.configs.FakePlayerData;
import hxckdms.hxccore.configs.HomesConfigStorage;
import hxckdms.hxccore.event.*;
import hxckdms.hxccore.network.CapesDownload;
import hxckdms.hxccore.network.CodersCheck;
import hxckdms.hxccore.network.MessageNameTagSync;
import hxckdms.hxccore.proxy.IProxy;
import hxckdms.hxccore.registry.CommandRegistry;
import hxckdms.hxccore.utilities.*;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.potion.Potion;
import net.minecraft.util.StringTranslate;
import net.minecraftforge.common.MinecraftForge;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;

import static hxckdms.hxccore.libraries.Constants.*;
import static hxckdms.hxccore.libraries.GlobalVariables.*;

@Mod(modid = MOD_ID, name = MOD_NAME, version = VERSION, dependencies = DEPENDENCIES, acceptableRemoteVersions = "*")
public class HxCCore {
    private static final Thread codersCheckThread = new Thread(new CodersCheck()), capesDownloadThread = new Thread(new CapesDownload());

    @Mod.Instance(MOD_ID)
    public static HxCCore instance;

    @SidedProxy(clientSide = "hxckdms.hxccore.proxy.ClientProxy", serverSide = "hxckdms.hxccore.proxy.ServerProxy")
    public static IProxy proxy;

    @Mod.EventHandler
    public void preInitialization(FMLPreInitializationEvent event) {
        modConfigDir = new File(event.getModConfigurationDirectory(), "HxCKDMS");

        codersCheckThread.setName("Coders check thread");
        codersCheckThread.start();
        capesDownloadThread.setName("Cape Down thread");
        if (Configuration.enableCapes) capesDownloadThread.start();

        mainConfig = new HxCConfig(Configuration.class, "HxCCore", modConfigDir, "cfg", MOD_NAME);
        mainConfig.initConfiguration();
        Kit.initConfigs();

        extendEnchantsArray();
        extendPotionsArray();

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
        MinecraftForge.EVENT_BUS.register(new EventNetworkCheck());
        MinecraftForge.EVENT_BUS.register(new EventXPBuffs());

        Logger.info("HxCKDMS Core has finished the initialization process.", MOD_NAME);
    }

    @Mod.EventHandler
    public void postInitialization(FMLPostInitializationEvent event) {
        langFile = StringTranslate.parseLangFile(this.getClass().getResourceAsStream("/assets/hxccore/lang/en_US.lang"));

        proxy.postInit(event);
        Logger.info("HxCKDMS Core has finished the post-initialization process.", MOD_NAME);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        server = event.getServer();
        if (!server.getEntityWorld().getGameRules().hasRule("HxC_XPBuffs")) server.getEntityWorld().getGameRules().addGameRule("HxC_XPBuffs", "true");
        if (!server.getEntityWorld().getGameRules().hasRule("XPPickupCoolDown")) server.getEntityWorld().getGameRules().addGameRule("XPPickupCoolDown", "2");

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
        if (customWorldData.hasTag("warp")) {
            customWorldData.setTagCompound("warps", customWorldData.getTagCompound("warp"));
            customWorldData.removeValue("warp");
        }
        if (customWorldData.hasTag("home")) {
            customWorldData.setTagCompound("homes", customWorldData.getTagCompound("home"));
            customWorldData.removeValue("home");
        }
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

    private static void extendEnchantsArray() {
        int enchantsOffset;
        Logger.info("Extending Enchants Array", MOD_NAME);
        enchantsOffset = Enchantment.enchantmentsList.length;
        Enchantment[] enchantmentsList = new Enchantment[enchantsOffset + 256];
        System.arraycopy(Enchantment.enchantmentsList, 0, enchantmentsList, 0, enchantsOffset);
        try {
            Field field = HxCReflectionHelper.getDeclaredField(Enchantment.class, "enchantmentsList", "field_77331_b");
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
            field.set(null, enchantmentsList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Logger.info("Enchants Array now: " + Enchantment.enchantmentsList.length, MOD_NAME);
    }

    private static void extendPotionsArray() {
        int potionOffset;
        Logger.info("Extending Potions Array.", MOD_NAME);
        potionOffset = Potion.potionTypes.length;
        Potion[] potionTypes = new Potion[potionOffset + 256];
        System.arraycopy(Potion.potionTypes, 0, potionTypes, 0, potionOffset);
        try {
            Field field = HxCReflectionHelper.getDeclaredField(Potion.class, "potionTypes", "field_76425_a");
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
            field.set(null, potionTypes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Logger.info("Potion array now: " + Potion.potionTypes.length, MOD_NAME);
    }
}
