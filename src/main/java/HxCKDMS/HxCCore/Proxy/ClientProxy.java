package HxCKDMS.HxCCore.Proxy;

import HxCKDMS.HxCCore.Api.HxCClientRegistry;
import HxCKDMS.HxCCore.Registry.ClientModRegistry;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@SuppressWarnings("unused")
public class ClientProxy extends CommonProxy {
    /*public void registerKeyBindings(){
        if (HxCCore.ModHxCSkills) {
            ClientRegistry.registerKeyBinding(Keybindings.BloodDestruction);
        }
    }*/
    /*
    public void recalculateHealth(RenderGameOverlayEvent.Pre event) {
        new HealthBarUpdateHandler().renderHealthbar(event);
    }
    */
    @Override
    public void preInit(FMLPreInitializationEvent event) {
        ClientModRegistry.init(event.getAsmData().getAll(HxCClientRegistry.class.getCanonicalName()), event.getModState());
    }
}
