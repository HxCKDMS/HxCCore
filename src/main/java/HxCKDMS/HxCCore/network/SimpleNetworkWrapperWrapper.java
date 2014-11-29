package HxCKDMS.HxCCore.network;

import HxCKDMS.HxCCore.HxCCore;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

/** No joke intended **/
public class SimpleNetworkWrapperWrapper {
    private SimpleNetworkWrapper network;
    private int discriminatorN;
    
    public SimpleNetworkWrapperWrapper(SimpleNetworkWrapper network) {
        this.network = network;
        this.discriminatorN = 0;
    }
    
    public <REQ extends IMessage, REPLY extends IMessage> void registerMessage(Class<? extends IMessageHandler<REQ, REPLY>> messageHandler, Class<REQ> requestMessageType) {
        this.network.registerMessage(messageHandler, requestMessageType, discriminatorN++, HxCCore.proxy.getSide());
    }
}
