package HxCKDMS.HxCCore.Proxy;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.client.Minecraft;

public class ClientProxy implements IProxy {
    Minecraft mc;
    @Override
    public void preInit(FMLPreInitializationEvent event) {
        mc = Minecraft.getMinecraft();
    }

    @Override
    public void init(FMLInitializationEvent event) {

    }
    @Override
    public void postInit(FMLPostInitializationEvent event) {

    }
}
