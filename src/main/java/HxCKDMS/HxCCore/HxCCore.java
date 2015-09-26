package HxCKDMS.HxCCore;

import HxCKDMS.HxCCore.Handlers.CommandsHandler;
import HxCKDMS.HxCCore.Configs.Configurations;
import HxCKDMS.HxCCore.Contributors.CodersCheck;
import HxCKDMS.HxCCore.Crash.CrashHandler;
import HxCKDMS.HxCCore.Crash.CrashReportThread;
import HxCKDMS.HxCCore.Events.*;
import HxCKDMS.HxCCore.Handlers.HxCReflectionHandler;
import HxCKDMS.HxCCore.Registry.CommandRegistry;
import HxCKDMS.HxCCore.api.Command.HxCCommand;
import HxCKDMS.HxCCore.api.Configuration.HxCConfig;
import HxCKDMS.HxCCore.api.Utils.LogHelper;
import HxCKDMS.HxCCore.lib.References;
import HxCKDMS.HxCCore.network.MessageColor;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkCheckHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.Potion;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SuppressWarnings({"unused", "ResultOfMethodCallIgnored"})
@Mod(modid = References.MOD_ID, name = References.MOD_NAME, version = References.VERSION, dependencies = References.DEPENDENCIES, acceptableRemoteVersions = "*")
public class HxCCore {
    @Instance(References.MOD_ID)
    public static HxCCore instance;

    public static MinecraftServer server;
    public static HashMap<EntityPlayerMP, EntityPlayerMP> tpaRequestList = new HashMap<>();
    public static HashMap<EntityPlayerMP, Integer> TpaTimeoutList = new HashMap<>();
    public static File HxCCoreDir, HxCConfigDir, HxCConfigFile, commandCFGFile, kitsFile;
    public static SimpleNetworkWrapper network;
    public static HxCConfig hxCConfig = new HxCConfig(), commandCFG = new HxCConfig(),
    kits = new HxCConfig();

    public static final Thread crashReportThread = new Thread(new CrashReportThread());
    public static final Thread CodersCheckThread = new Thread(new CodersCheck());

    public static volatile ArrayList<UUID> coders = new ArrayList<>(),
            helpers = new ArrayList<>(), supporters = new ArrayList<>(),
            artists = new ArrayList<>(), mascots = new ArrayList<>();

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        FMLCommonHandler.instance().registerCrashCallable(new CrashHandler());
        crashReportThread.setName("HxCKDMS Crash check thread");
        Runtime.getRuntime().addShutdownHook(crashReportThread);

        HxCConfigDir = new File(event.getModConfigurationDirectory(), "HxCKDMS");
        if (!HxCConfigDir.exists()) HxCConfigDir.mkdirs();

        HxCConfigFile = new File(HxCConfigDir, "HxCCore.cfg");
        commandCFGFile = new File(HxCConfigDir, "HxCCommands.cfg");
        kitsFile = new File(HxCConfigDir, "HxC-Kits.cfg");

        Configurations.preInitConfigs();

        if (Configurations.enableCommands)
            MinecraftForge.EVENT_BUS.register(new EventBuildPath());

        for (int i = 0; i < Configurations.Permissions.size(); i++) {
            References.PERM_NAMES[i] = (String) Configurations.Permissions.keySet().toArray()[i];
            References.PERM_COLOURS[i] = Configurations.Permissions.get(References.PERM_NAMES[i]).charAt(0);
            References.HOMES[i] = Integer.parseInt(Configurations.Permissions.get(References.PERM_NAMES[i]).substring(1).trim());
        }

        network = NetworkRegistry.INSTANCE.newSimpleChannel(References.PACKET_CHANNEL_NAME);
        network.registerMessage(MessageColor.Handler.class, MessageColor.class, 0, Side.CLIENT);

        CodersCheckThread.setName("HxCKDMS Contributors check thread");
        CodersCheckThread.start();
        extendEnchantsArray();

        if (Configurations.enableCommands) CommandRegistry.registerCommands(new CommandsHandler(), event.getAsmData().getAll(HxCCommand.class.getCanonicalName()));

        if (!Loader.isModLoaded("BiomesOPlenty")) extendPotionsArray();
        //NEED TO IMPLEMENT Reika's Packet changes...
//        FMLCommonHandler.instance().bus().register(new KeyInputHandler());
        LogHelper.info("Thank your for using HxCCore", References.MOD_NAME);
        LogHelper.info("If you see any debug messages, feel free to bug one of the authors about it ^_^", References.MOD_NAME);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new EventGod());
        if (!Loader.isModLoaded("HxCSkills")) MinecraftForge.EVENT_BUS.register(new EventXPtoBuffs());
        MinecraftForge.EVENT_BUS.register(new EventChat());

        FMLCommonHandler.instance().bus().register(new EventNickSync());
        FMLCommonHandler.instance().bus().register(new EventTpRequest());
        FMLCommonHandler.instance().bus().register(new EventJoinWorld());
        FMLCommonHandler.instance().bus().register(new EventPlayerNetworkCheck());
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        if (Configurations.DebugMode) event.getModState();

        if (Loader.isModLoaded("HxCSkills"))
            LogHelper.info("Thank your for using HxCSkills", References.MOD_NAME);
        if (Loader.isModLoaded("HxCEnchants"))
            LogHelper.info("Thank your for using HxCEnchants", References.MOD_NAME);
        if (Loader.isModLoaded("HxCWorldGen"))
            LogHelper.info("Thank your for using HxCWorldGen", References.MOD_NAME);
        if (Loader.isModLoaded("HxCLinkPads"))
            LogHelper.info("Thank your for using HxCLinkPads", References.MOD_NAME);
        if (Loader.isModLoaded("HxCBlocks"))
            LogHelper.info("Thank your for using HxCBlocks", References.MOD_NAME);
        if (Loader.isModLoaded("HxCFactions"))
            LogHelper.info("Thank your for using HxCFactions", References.MOD_NAME);
        if (Loader.isModLoaded("HxCTiC"))
            LogHelper.info("Thank your for using HxCTiC", References.MOD_NAME);
        if (Loader.isModLoaded("magicenergy"))
            LogHelper.info("Thank your for using MagicEnergy", References.MOD_NAME);
        if (Loader.isModLoaded("hxcbows"))
            LogHelper.info("Thank your for using HxCBows", References.MOD_NAME);
        if (Loader.isModLoaded("hxcdiseases"))
            LogHelper.info("Thank your for using HxCDiseases", References.MOD_NAME);
    }

    @EventHandler
    public void serverStart(FMLServerStartingEvent event) {
        server = event.getServer();
        if (Configurations.enableCommands) CommandsHandler.initCommands(event);

        File WorldDir = new File(event.getServer().getEntityWorld().getSaveHandler().getWorldDirectory(), "HxCCore");
        if (!WorldDir.exists())
            WorldDir.mkdirs();
        HxCCoreDir = WorldDir;

        File CustomWorldFile = new File(HxCCoreDir, "HxCWorld.dat");
        File PermissionsData = new File(HxCCoreDir, "HxC-Permissions.dat");

        try {
            if (!CustomWorldFile.exists())
                CustomWorldFile.createNewFile();
            if (!PermissionsData.exists())
                PermissionsData.createNewFile();
        } catch (IOException ignored) {}
    }

    private static void extendEnchantsArray() {
        /**
         * Made by DrZed inspired by
         * Biomes O'Plenty
         **/
        int enchantsOffset;
        LogHelper.info("Extending Enchants Array", References.MOD_NAME);
        enchantsOffset = Enchantment.enchantmentsList.length;
        Enchantment[] enchantmentsList = new Enchantment[enchantsOffset + 256];
        System.arraycopy(Enchantment.enchantmentsList, 0, enchantmentsList, 0, enchantsOffset);
        HxCReflectionHandler.setPrivateFinalValue(Enchantment.class, null, enchantmentsList, "enchantmentsList", "field_77331_b");
        LogHelper.info("Enchants Array now: " + Enchantment.enchantmentsList.length, References.MOD_NAME);
    }

    private static void extendPotionsArray() {
        /**
         * Taken From Biomes O'Plenty
         * modified to fit my interest
         * The License for BOP is CC
         * I Thank BOP Team for this
         **/
        int potionOffset;
        LogHelper.info("Extending Potions Array.", References.MOD_NAME);
        potionOffset = Potion.potionTypes.length;
        Potion[] potionTypes = new Potion[potionOffset + 256];
        System.arraycopy(Potion.potionTypes, 0, potionTypes, 0, potionOffset);
        HxCReflectionHandler.setPrivateFinalValue(Potion.class, null, potionTypes, "potionTypes", "field_76425_a");
    }

    @NetworkCheckHandler
    public boolean checkNetwork(Map<String, String> mods, Side side) {
        return !mods.containsKey("HxCCore") || mods.get("HxCCore").equals(References.VERSION);
    }
}
