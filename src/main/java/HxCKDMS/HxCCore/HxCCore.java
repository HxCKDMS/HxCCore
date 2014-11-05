package HxCKDMS.HxCCore;

import HxCKDMS.HxCCore.Commands.CommandBase;
import HxCKDMS.HxCCore.Events.*;
import HxCKDMS.HxCCore.Handlers.HxCReflectionHandler;
import HxCKDMS.HxCCore.Handlers.KeyInputHandler;
import HxCKDMS.HxCCore.Proxy.IProxy;
import HxCKDMS.HxCCore.Utils.LogHelper;
import HxCKDMS.HxCCore.lib.Reference;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.Level;

import java.io.File;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION)

public class HxCCore
{
    public static File HxCCoreDir = null;

    @SidedProxy(serverSide = "HxCKDMS.HxCCore.Proxy.ServerProxy", clientSide = "HxCKDMS.HxCCore.Proxy.ClientProxy")
    public static IProxy proxy;

    public static Config Config;

    @Instance(Reference.MOD_ID)
    public static HxCCore instance;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        Config = new Config(new Configuration(event.getSuggestedConfigurationFile()));
        extendEnchantsArray();
        LogHelper.info("Thank your for using HxCCore");
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(new EventGod());
        if (Config.XPBuffs) MinecraftForge.EVENT_BUS.register(new EventXPtoBuffs());
        MinecraftForge.EVENT_BUS.register(new EventJoinWorld());
        FMLCommonHandler.instance().bus().register(new KeyInputHandler());
        proxy.registerKeyBindings();
    }

    @EventHandler
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void serverStart(FMLServerStartingEvent event)
    {
        CommandBase.initCommands(event);

        File WorldDir = new File(event.getServer().getEntityWorld().getSaveHandler().getWorldDirectory(), "HxCCore");
        if (!WorldDir.exists()) {
            WorldDir.mkdirs();
        }
        HxCCoreDir = WorldDir;
    }
    @SuppressWarnings("MismatchedReadAndWriteOfArray")
    private static void extendEnchantsArray()
    {
        int enchantsOffset;
        LogHelper.info("Extending Enchants Array");
        enchantsOffset = Enchantment.enchantmentsList.length;
        Enchantment[] enchantmentsList = new Enchantment[enchantsOffset + 256];
        System.arraycopy(Enchantment.enchantmentsList, 0, enchantmentsList, 0, enchantsOffset);
        HxCReflectionHandler.setPrivateFinalValue(Enchantment.class, null, enchantmentsList, "enchantmentsList", "field_76425_a");
        LogHelper.info("Enchants Array now 512");
    }
}