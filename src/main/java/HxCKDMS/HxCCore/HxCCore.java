package HxCKDMS.HxCCore;

import HxCKDMS.HxCCore.lib.Reference;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION)

public class HxCCore
{

	@SidedProxy(serverSide = "HxCKDMS.HxCCore.Proxy.CommonProxy", clientSide = "HxCKDMS.HxCCore.Proxy.ClientProxy")
	@Mod.Instance(Reference.MOD_ID)
	public static HxCCore instance;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{

	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
	}
}