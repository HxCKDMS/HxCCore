package HxCKDMS.HxCCore.Proxy;

import java.io.File;
import java.util.Set;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import HxCKDMS.HxCCore.HxCCore;
import HxCKDMS.HxCCore.Handlers.HealthBarUpdateHandler;
import HxCKDMS.HxCCore.Handlers.NBTFileIO;
import HxCKDMS.HxCCore.renderers.RenderHxCPlayer;
import cpw.mods.fml.client.registry.RenderingRegistry;

//import HxCKDMS.HxCCore.Handlers.Keybindings;
//import cpw.mods.fml.client.registry.ClientRegistry;
@SuppressWarnings("unused")
public class ClientProxy extends CommonProxy {
    //    @Override
    //    public void registerKeyBindings(){
    //        ClientRegistry.registerKeyBinding(Keybindings.test);
    //    }
    public void recalculateHealth(RenderGameOverlayEvent.Pre event) {
        new HealthBarUpdateHandler().renderHealthbar(event);
    }
    
    @Override
    public void preInit() {
        RenderingRegistry.registerEntityRenderingHandler(EntityPlayer.class, new RenderHxCPlayer());
        RenderHxCPlayer.loadColors();
    }
}
