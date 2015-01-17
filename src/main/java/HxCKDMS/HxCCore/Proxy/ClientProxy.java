package HxCKDMS.HxCCore.Proxy;

import HxCKDMS.HxCCore.renderers.RenderHxCPlayer;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.entity.player.EntityPlayer;

//import HxCKDMS.HxCCore.Handlers.Keybindings;
//import cpw.mods.fml.client.registry.ClientRegistry;
@SuppressWarnings("unused")
public class ClientProxy extends CommonProxy {
    //    @Override
    //    public void registerKeyBindings(){
    //        ClientRegistry.registerKeyBinding(Keybindings.test);
    //    }
  /*  public void recalculateHealth(RenderGameOverlayEvent.Pre event) {
        new HealthBarUpdateHandler().renderHealthbar(event);
    }*/
    
    @Override
    public void preInit() {
        RenderingRegistry.registerEntityRenderingHandler(EntityPlayer.class, new RenderHxCPlayer());
    }
}
