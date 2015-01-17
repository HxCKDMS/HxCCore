package HxCKDMS.HxCCore.Proxy;

import HxCKDMS.HxCCore.Handlers.Keybindings;
import HxCKDMS.HxCCore.HxCCore;
import HxCKDMS.HxCCore.renderers.RenderHxCPlayer;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.entity.player.EntityPlayer;
@SuppressWarnings("unused")
public class ClientProxy extends CommonProxy {
    public void registerKeyBindings(){
        if (HxCCore.ModHxCSkills) {
            ClientRegistry.registerKeyBinding(Keybindings.BloodDestruction);
        }
    }
    /*
    public void recalculateHealth(RenderGameOverlayEvent.Pre event) {
        new HealthBarUpdateHandler().renderHealthbar(event);
    }
    */
    @Override
    public void preInit() {
        RenderingRegistry.registerEntityRenderingHandler(EntityPlayer.class, new RenderHxCPlayer());
    }
}
