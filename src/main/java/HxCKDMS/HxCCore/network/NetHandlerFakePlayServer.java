package HxCKDMS.HxCCore.network;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.*;
import net.minecraft.network.play.client.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.IChatComponent;

import javax.crypto.SecretKey;

public class NetHandlerFakePlayServer extends NetHandlerPlayServer {
    
    public static class FakeNetworkManager extends NetworkManager {

        public FakeNetworkManager() {
            super(null);
        }

        @Override
        public void channelActive(ChannelHandlerContext handlerContext) throws Exception {
            
        }

        @Override
        public void setConnectionState(EnumConnectionState connectionState) {
            
        }

        @Override
        public void channelInactive(ChannelHandlerContext handlerContext) {
            
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext handlerContext, Throwable throwable) {
            
        }

        @Override
        public void setNetHandler(INetHandler netHandler) {
            
        }

        @Override
        public void processReceivedPackets() {
            
        }

        @Override
        public void closeChannel(IChatComponent chatComponent) {
            
        }

        @Override
        public boolean isLocalChannel() {
            return false;
        }

        @Override
        public void enableEncryption(SecretKey secretKey) {
            
        }

        @Override
        public boolean isChannelOpen() {
            return false;
        }

        @Override
        public INetHandler getNetHandler() {
            return null;
        }

        @Override
        public Channel channel() {
            return null;
        }

        @Override
        public void disableAutoRead() {

        }

        @Override
        public IChatComponent getExitMessage() {
            return null;
        }
    }
    
    public NetHandlerFakePlayServer(MinecraftServer minecraftServer, EntityPlayerMP player) {
        super(minecraftServer, new FakeNetworkManager(), player);
    }

    @Override
    public void kickPlayerFromServer(String kickMessage) {
        
    }

    @Override
    public void processInput(C0CPacketInput packetInput) {
        
    }

    @Override
    public void processPlayer(C03PacketPlayer packetPlayer) {
        
    }

    @Override
    public void setPlayerLocation(double x, double y, double z, float yaw, float pitch) {
        
    }

    @Override
    public void processPlayerDigging(C07PacketPlayerDigging packetPlayerDigging) {
        
    }

    @Override
    public void processPlayerBlockPlacement(C08PacketPlayerBlockPlacement packetPlayerBlockPlacement) {
        
    }

    @Override
    public void onDisconnect(IChatComponent chatComponent) {
        
    }

    @Override
    public void sendPacket(Packet packet) {
        
    }

    @Override
    public void processHeldItemChange(C09PacketHeldItemChange packetHeldItemChange) {
        
    }

    @Override
    public void processChatMessage(C01PacketChatMessage packetChatMessage) {
        
    }

    @Override
    public void processEntityAction(C0BPacketEntityAction packetEntityAction) {
        
    }

    @Override
    public void processUseEntity(C02PacketUseEntity packetUseEntity) {
        
    }

    @Override
    public void processClientStatus(C16PacketClientStatus packetClientStatus) {
        
    }

    @Override
    public void processCloseWindow(C0DPacketCloseWindow packetCloseWindow) {
        
    }

    @Override
    public void processClickWindow(C0EPacketClickWindow packetClickWindow) {
        
    }

    @Override
    public void processEnchantItem(C11PacketEnchantItem packetEnchantItem) {
        
    }

    @Override
    public void processCreativeInventoryAction(C10PacketCreativeInventoryAction packetCreativeInventoryAction) {
        
    }

    @Override
    public void processConfirmTransaction(C0FPacketConfirmTransaction packetConfirmTransaction) {
        
    }

    @Override
    public void processUpdateSign(C12PacketUpdateSign packetUpdateSign) {
        
    }

    @Override
    public void processKeepAlive(C00PacketKeepAlive packetKeepAlive) {
        
    }

    @Override
    public void processPlayerAbilities(C13PacketPlayerAbilities packetPlayerAbilities) {
        
    }

    @Override
    public void processTabComplete(C14PacketTabComplete packetTabComplete) {
        
    }

    @Override
    public void processClientSettings(C15PacketClientSettings packetClientSettings) {
        
    }

    @Override
    public void processVanilla250Packet(C17PacketCustomPayload packetCustomPayload) {
        
    }
}