package hxckdms.hxccore.network;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.*;
import net.minecraft.network.play.client.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;

import javax.crypto.SecretKey;
import java.net.SocketAddress;

public class FakeNetHandlerPlayerServer extends NetHandlerPlayServer {
    public static class FakeNetworkManager extends NetworkManager {
        FakeNetworkManager() {
            super(EnumPacketDirection.CLIENTBOUND);
        }

        @Override
        public void channelActive(ChannelHandlerContext handlerContext) throws Exception {}

        @Override
        public void setConnectionState(EnumConnectionState connectionState) {}

        @Override
        public void channelInactive(ChannelHandlerContext handlerContext) {}

        @Override
        public void exceptionCaught(ChannelHandlerContext handlerContext, Throwable throwable) {}

        @Override
        public void setNetHandler(INetHandler netHandler) {}


        @Override
        public void sendPacket(Packet<?> packetIn) {}

        @Override
        public void sendPacket(Packet<?> packetIn, GenericFutureListener<? extends Future<? super Void>> listener, GenericFutureListener<? extends Future<? super Void>>[] listeners) {}

        @Override
        public void processReceivedPackets() {}

        @Override
        public SocketAddress getRemoteAddress() {
            return null;
        }

        @Override
        public void closeChannel(ITextComponent chatComponent) {}

        @Override
        public boolean isLocalChannel() {
            return false;
        }

        @Override
        public void enableEncryption(SecretKey secretKey) {}

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
        public void disableAutoRead() {}

        @Override
        public ITextComponent getExitMessage() {
            return null;
        }
    }


    public FakeNetHandlerPlayerServer(MinecraftServer server, EntityPlayerMP playerIn) {
        super(server, new FakeNetworkManager(), playerIn);
    }

    @Override
    public void update() {}

    @Override
    public void kickPlayerFromServer(String kickMessage) {}

    @Override
    public void processInput(CPacketInput packetInput) {}

    @Override
    public void processPlayer(CPacketPlayer packetPlayer) {}

    @Override
    public void setPlayerLocation(double x, double y, double z, float yaw, float pitch) {}

    @Override
    public void processPlayerDigging(CPacketPlayerDigging packetPlayerDigging) {}

    @Override
    public void processPlayerBlockPlacement(CPacketPlayerTryUseItem packetPlayerBlockPlacement) {}

    @Override
    public void onDisconnect(ITextComponent textComponent) {}

    @Override
    public void sendPacket(Packet packet) {}

    @Override
    public void processHeldItemChange(CPacketHeldItemChange packetHeldItemChange) {}

    @Override
    public void processChatMessage(CPacketChatMessage packetChatMessage) {}

    @Override
    public void handleAnimation(CPacketAnimation packetAnimation) {}

    @Override
    public void processEntityAction(CPacketEntityAction packetEntityAction) {}

    @Override
    public void processUseEntity(CPacketUseEntity packetUseEntity) {}

    @Override
    public void processClientStatus(CPacketClientStatus packetClientStatus) {}

    @Override
    public void processCloseWindow(CPacketCloseWindow packetCloseWindow) {}

    @Override
    public void processClickWindow(CPacketClickWindow packetClickWindow) {}

    @Override
    public void processEnchantItem(CPacketEnchantItem packetEnchantItem) {}

    @Override
    public void processCreativeInventoryAction(CPacketCreativeInventoryAction packetCreativeInventoryAction) {}

    @Override
    public void processConfirmTransaction(CPacketConfirmTransaction packetConfirmTransaction) {}

    @Override
    public void processUpdateSign(CPacketUpdateSign packetUpdateSign) {}

    @Override
    public void processKeepAlive(CPacketKeepAlive packetKeepAlive) {}

    @Override
    public void processPlayerAbilities(CPacketPlayerAbilities packetPlayerAbilities) {}

    @Override
    public void processTabComplete(CPacketTabComplete packetTabComplete) {}

    @Override
    public void processClientSettings(CPacketClientSettings packetClientSettings) {}

    @Override
    public void processCustomPayload(CPacketCustomPayload packetCustomPayload) {}
}
