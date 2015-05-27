package HxCKDMS.HxCCore;

import HxCKDMS.HxCCore.Commands.CommandBase;
import HxCKDMS.HxCCore.Configs.Config;
import HxCKDMS.HxCCore.Contributors.CodersCheck;
import HxCKDMS.HxCCore.Events.*;
import HxCKDMS.HxCCore.Handlers.HxCReflectionHandler;
import HxCKDMS.HxCCore.Proxy.CommonProxy;
import HxCKDMS.HxCCore.Utils.LogHelper;
import HxCKDMS.HxCCore.lib.References;
import HxCKDMS.HxCCore.network.PacketPipeline;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.Potion;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

@Mod(modid = References.MOD_ID, name = References.MOD_NAME, version = References.VERSION)
public class HxCCore
{
    public static File HxCCoreDir = null;
    public static MinecraftServer server;
    public static final PacketPipeline packetPipeLine = new PacketPipeline();
    public static HashMap<EntityPlayerMP, EntityPlayerMP> tpaRequestList = new HashMap<>();
    public static HashMap<EntityPlayerMP, Integer> TpaTimeoutList = new HashMap<>();

    public static final Thread CodersCheckThread = new Thread(new CodersCheck());
    public static volatile ArrayList<UUID> coders = new ArrayList<>();
    public static volatile ArrayList<UUID> helpers = new ArrayList<>();
    public static volatile ArrayList<UUID> supporters = new ArrayList<>();
    public static volatile ArrayList<UUID> artists = new ArrayList<>();
    public static volatile ArrayList<UUID> mascots = new ArrayList<>();

    @SidedProxy(serverSide = "HxCKDMS.HxCCore.Proxy.ServerProxy", clientSide = "HxCKDMS.HxCCore.Proxy.ClientProxy")
    public static CommonProxy proxy;
    
    public static HxCKDMS.HxCCore.Configs.Config Config;


    @Instance(References.MOD_ID)
    public static HxCCore instance;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        CodersCheckThread.setName("HxCKDMS Contributors check thread");
        CodersCheckThread.start();

        proxy.preInit(event);
        Config = new Config(new Configuration(event.getSuggestedConfigurationFile()));
        extendEnchantsArray();
        if (!Loader.isModLoaded("BiomesOPlenty")) extendPotionsArray();
//        FMLCommonHandler.instance().bus().register(new KeyInputHandler());
        LogHelper.info("Thank your for using HxCCore", References.MOD_NAME);
        LogHelper.info("If you see any debug messages, feel free to bug one of the authors about it ^_^", References.MOD_NAME);
        LogHelper.warn("Please guys can you start reporting the bugs as soon as you find them it's not hard and only takes like 2 minutes", References.MOD_NAME);
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        packetPipeLine.initialize(References.PACKET_CHANNEL_NAME);

        MinecraftForge.EVENT_BUS.register(new EventGod());
        MinecraftForge.EVENT_BUS.register(new EventXPtoBuffs());
        MinecraftForge.EVENT_BUS.register(new EventJoinWorld());
        MinecraftForge.EVENT_BUS.register(new EventChat());
        MinecraftForge.EVENT_BUS.register(new EventNickSync());

        FMLCommonHandler.instance().bus().register(new EventNickSync());
        FMLCommonHandler.instance().bus().register(new EventTpRequest());
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        packetPipeLine.postInitialize();
        if (HxCKDMS.HxCCore.Configs.Config.DebugMode)event.getModState();

        if (Loader.isModLoaded("HxCSkills"))LogHelper.info("Thank your for using HxCSkills", References.MOD_NAME);
        if (Loader.isModLoaded("HxCEnchants"))LogHelper.info("Thank your for using HxCEnchants", References.MOD_NAME);
        if (Loader.isModLoaded("HxCWorldGen"))LogHelper.info("Thank your for using HxCWorldGen", References.MOD_NAME);
        if (Loader.isModLoaded("HxCLinkPads"))LogHelper.info("Thank your for using HxCLinkPads", References.MOD_NAME);
        if (Loader.isModLoaded("HxCBlocks"))LogHelper.info("Thank your for using HxCBlocks", References.MOD_NAME);
        if (Loader.isModLoaded("magicenergy"))LogHelper.info("Thank your for using MagicEnergy", References.MOD_NAME);
        if (Loader.isModLoaded("hxcbows"))LogHelper.info("Thank your for using HxCBows", References.MOD_NAME);
    }

    @EventHandler
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void serverStart(FMLServerStartingEvent event)
    {
        server = event.getServer();
        if (Config.commands) CommandBase.initCommands(event);

        File WorldDir = new File(event.getServer().getEntityWorld().getSaveHandler().getWorldDirectory(), "HxCCore");
        if (!WorldDir.exists()) {
            WorldDir.mkdirs();
        }
        HxCCoreDir = WorldDir;

        File CustomWorldFile = new File(HxCCoreDir, "HxCWorld.dat");
        File PermissionsData = new File(HxCCoreDir, "HxC-Permissions.dat");

        try {
            if (!CustomWorldFile.exists()) CustomWorldFile.createNewFile();
            if (!PermissionsData.exists()) PermissionsData.createNewFile();
        } catch(IOException e) {
            e.printStackTrace();
        }
        LogHelper.info(Potion.potionTypes.length, References.MOD_NAME);
    }
    private static void extendEnchantsArray()
    {
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
    private static void extendPotionsArray()
    {
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
}