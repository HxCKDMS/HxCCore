package HxCKDMS.HxCCore.network;

import HxCKDMS.HxCCore.HxCCore;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

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
    
    public void sendToAll(IMessage message) {
        network.sendToAll(message);
    }
    
    public void sendTo(IMessage message, EntityPlayerMP player) {
        network.sendTo(message, player);
    }
    
    public void sendToAllAround(IMessage message, NetworkRegistry.TargetPoint point) {
        network.sendToAllAround(message, point);
    }
    
    public void sendToDimension(IMessage message, int dimensionId) {
        network.sendToDimension(message, dimensionId);
    }
    
    /** If dedicated server, sends to server. If integrated server, sends to everybody.
     * Gotta just love the side system and how impossible it is to work with LAN packets **/
    public void sendToServer(IMessage message) {
        if (MinecraftServer.getServer().isDedicatedServer()) {
            network.sendToServer(message);
        } else {
            network.sendToAll(message);
        }
    }
}
