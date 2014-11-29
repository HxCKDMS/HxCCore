package HxCKDMS.HxCCore.Proxy;

import HxCKDMS.HxCCore.network.MessageColor;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

public abstract class CommonProxy {
    
    public void preInit() {};
    
    public void registerNetworkStuff(SimpleNetworkWrapper network) {
        Side s = this instanceof ClientProxy ? Side.CLIENT : Side.SERVER;
        network.registerMessage(MessageColor.class, MessageColor.Message.class, 0, s);
    }
}
