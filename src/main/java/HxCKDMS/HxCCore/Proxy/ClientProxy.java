package HxCKDMS.HxCCore.Proxy;

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
    public void preInit() {
//        RenderingRegistry.registerEntityRenderingHandler(EntityPlayer.class, new RenderHxCPlayer(Minecraft.getMinecraft().getRenderManager()));
    }
}
