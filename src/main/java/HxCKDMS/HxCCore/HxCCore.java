package HxCKDMS.HxCCore;

import HxCKDMS.HxCCore.Commands.CommandBase;
import HxCKDMS.HxCCore.Configs.Config;
import HxCKDMS.HxCCore.Events.*;
import HxCKDMS.HxCCore.Handlers.KeyInputHandler;
import HxCKDMS.HxCCore.Proxy.ClientProxy;
import HxCKDMS.HxCCore.Proxy.CommonProxy;
import HxCKDMS.HxCCore.Utils.LogHelper;
import HxCKDMS.HxCCore.lib.Reference;
import HxCKDMS.HxCCore.network.PacketPipeline;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION)

public class HxCCore
{
    public static File HxCCoreDir = null;
    public static ClientProxy cproxy;
    public static final PacketPipeline packetPipeLine = new PacketPipeline();
    public static HashMap<EntityPlayerMP, EntityPlayerMP> tpaRequestList = new HashMap<EntityPlayerMP, EntityPlayerMP>();
    public static HashMap<EntityPlayerMP, Integer> TpaTimeoutList = new HashMap<EntityPlayerMP, Integer>();

    @SidedProxy(serverSide = "HxCKDMS.HxCCore.Proxy.ServerProxy", clientSide = "HxCKDMS.HxCCore.Proxy.ClientProxy")
    public static CommonProxy proxy;
    public static MinecraftServer server;
    
    public static Config Config;

    @Mod.Instance(Reference.MOD_ID)
    public static HxCCore instance;

    public static boolean ModHxCSkills;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        proxy.preInit();
//        proxy.registerNetworkStuff(network = new SimpleNetworkWrapperWrapper(NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MOD_ID)));
        Config = new Config(new Configuration(event.getSuggestedConfigurationFile()));
//        extendEnchantsArray();
//        ModHxCSkills = Loader.isModLoaded("HxCSkills");
//        FMLCommonHandler.instance().bus().register(new KeyInputHandler());
//        cproxy.registerKeyBindings();
        LogHelper.info("Thank your for using HxCCore", Reference.MOD_NAME);
        LogHelper.info("If you see any debug messages, feel free to bug one of the authors about it ^_^", Reference.MOD_NAME);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        packetPipeLine.initialize();

        MinecraftForge.EVENT_BUS.register(new EventGod());
        MinecraftForge.EVENT_BUS.register(new EventXPtoBuffs());
        MinecraftForge.EVENT_BUS.register(new EventJoinWorld());
        MinecraftForge.EVENT_BUS.register(new EventChat());

        FMLCommonHandler.instance().bus().register(new KeyInputHandler());
        FMLCommonHandler.instance().bus().register(new EventTpRequest());
    }
    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    packetPipeLine.postInitialize();
    event.getModState();
    if (ModHxCSkills)LogHelper.info("Thank your for using HxCSkills", Reference.MOD_NAME);
}

    @Mod.EventHandler
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
        File PermissionsData = new File(HxCCoreDir, "HxC-Permissions.dat");

        try {
            if (!CustomWorldFile.exists()) CustomWorldFile.createNewFile();
            if (!PermissionsData.exists()) PermissionsData.createNewFile();
        }catch(IOException e){
            e.printStackTrace();
        }


    }
/*    @SuppressWarnings("MismatchedReadAndWriteOfArray")
    private static void extendEnchantsArray()
    {
        int enchantsOffset;
        LogHelper.info("Extending Enchants Array", Reference.MOD_NAME);
        enchantsOffset = Enchantment.enchantmentsList.length;
        Enchantment[] enchantmentsList = new Enchantment[enchantsOffset + 256];
        System.arraycopy(Enchantment.enchantmentsList, 0, enchantmentsList, 0, enchantsOffset);
        HxCReflectionHandler.setPrivateFinalValue(Enchantment.class, null, enchantmentsList, "enchantmentsList", "field_77331_b");
        LogHelper.info("Enchants Array now: " + Enchantment.enchantmentsList.length, Reference.MOD_NAME);
    }*/
}