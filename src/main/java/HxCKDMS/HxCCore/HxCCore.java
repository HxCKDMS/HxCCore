package HxCKDMS.HxCCore;

import HxCKDMS.HxCCore.Commands.CommandBase;
import HxCKDMS.HxCCore.Configs.Config;
import HxCKDMS.HxCCore.Events.EventGod;
import HxCKDMS.HxCCore.Events.EventJoinWorld;
import HxCKDMS.HxCCore.Events.EventXPtoBuffs;
import HxCKDMS.HxCCore.Handlers.HxCReflectionHandler;
import HxCKDMS.HxCCore.Proxy.ClientProxy;
import HxCKDMS.HxCCore.Proxy.CommonProxy;
import HxCKDMS.HxCCore.Utils.LogHelper;
import HxCKDMS.HxCCore.lib.Reference;
import HxCKDMS.HxCCore.network.PacketPipeline;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;

import java.io.File;
import java.io.IOException;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION)
public class HxCCore
{
    public static File HxCCoreDir = null;
    public static MinecraftServer server;
    public static final PacketPipeline packetPipeLine = new PacketPipeline();

    @SidedProxy(serverSide = "HxCKDMS.HxCCore.Proxy.ServerProxy", clientSide = "HxCKDMS.HxCCore.Proxy.ClientProxy")
    public static CommonProxy proxy;
    public static ClientProxy cproxy;
    
    public static HxCKDMS.HxCCore.Configs.Config Config;

    @Instance(Reference.MOD_ID)
    public static HxCCore instance;

    public static boolean ModHxCSkills;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        proxy.preInit();
        Config = new Config(new Configuration(event.getSuggestedConfigurationFile()));
        extendEnchantsArray();
//        ModHxCSkills = Loader.isModLoaded("HxCSkills");
//        FMLCommonHandler.instance().bus().register(new KeyInputHandler());
//        cproxy.registerKeyBindings();
        LogHelper.info("Thank your for using HxCCore", Reference.MOD_NAME);
        LogHelper.info("If you see any debug messages, feel free to bug one of the authors about it ^_^", Reference.MOD_NAME);
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        packetPipeLine.initialize();
        MinecraftForge.EVENT_BUS.register(new EventGod());
        MinecraftForge.EVENT_BUS.register(new EventXPtoBuffs());
        MinecraftForge.EVENT_BUS.register(new EventJoinWorld());
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        packetPipeLine.postInitialize();
        event.getModState();
        if (ModHxCSkills)LogHelper.info("Thank your for using HxCSkills", Reference.MOD_NAME);
    }
    @EventHandler
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void serverStart(FMLServerStartingEvent event)
    {
        server = event.getServer();
        CommandBase.initCommands(event);

        File WorldDir = new File(event.getServer().getEntityWorld().getSaveHandler().getWorldDirectory(), "HxCCore");
        if (!WorldDir.exists()) {
            WorldDir.mkdirs();
        }
        HxCCoreDir = WorldDir;

        File CustomWorldFile = new File(HxCCoreDir, "HxCWorld.dat");

        try {
            if (!CustomWorldFile.exists()) CustomWorldFile.createNewFile();
        }catch(IOException e){
            e.printStackTrace();
        }


    }
    @SuppressWarnings("MismatchedReadAndWriteOfArray")
    private static void extendEnchantsArray()
    {
        int enchantsOffset;
        LogHelper.info("Extending Enchants Array", Reference.MOD_NAME);
        enchantsOffset = Enchantment.enchantmentsList.length;
        Enchantment[] enchantmentsList = new Enchantment[enchantsOffset + 256];
        System.arraycopy(Enchantment.enchantmentsList, 0, enchantmentsList, 0, enchantsOffset);
        HxCReflectionHandler.setPrivateFinalValue(Enchantment.class, null, enchantmentsList, "enchantmentsList", "field_77331_b");
        LogHelper.info("Enchants Array now: " + Enchantment.enchantmentsList.length, Reference.MOD_NAME);
    }
}