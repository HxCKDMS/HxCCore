package HxCKDMS.HxCCore;

import HxCKDMS.HxCCore.Commands.CommandBase;
import HxCKDMS.HxCCore.Events.EventGod;
import HxCKDMS.HxCCore.Proxy.IProxy;
import HxCKDMS.HxCCore.Utils.LogHelper;
import HxCKDMS.HxCCore.lib.Reference;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.common.MinecraftForge;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION)

public class HxCCore
{
    @SidedProxy(serverSide = "HxCKDMS.HxCCore.Proxy.CommonProxy", clientSide = "HxCKDMS.HxCCore.Proxy.ClientProxy")
    public static IProxy proxy;

	@Mod.Instance(Reference.MOD_ID)
	public static HxCCore instance;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
        LogHelper.info("Thank your for using HxCCore");
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
        MinecraftForge.EVENT_BUS.register(EventGod.instance);
    }

    @EventHandler
    public void serverStart(FMLServerStartingEvent event)
    {
        CommandBase.initCommands(event);
    }
}