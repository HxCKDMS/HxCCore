package HxCKDMS.HxCCore.Proxy;

import HxCKDMS.HxCCore.network.MessageColor;
import HxCKDMS.HxCCore.network.SimpleNetworkWrapperWrapper;
import cpw.mods.fml.relauncher.Side;

public abstract class CommonProxy {
    
    public void preInit() {};
    
    public void registerNetworkStuff(SimpleNetworkWrapperWrapper network) {
        network.registerMessage(MessageColor.class, MessageColor.Message.class);
    }
    
    /** Returns proxy side and therefore mod instance side. **/
    public Side getSide() {
        return this instanceof ClientProxy ? Side.CLIENT : Side.SERVER;
    }
}
