package HxCKDMS.HxCCore.network;

import HxCKDMS.HxCCore.Utils.LogHelper;
import HxCKDMS.HxCCore.lib.References;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.FMLOutboundHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;

import java.util.*;

@ChannelHandler.Sharable
@SuppressWarnings({"unused"})
public class PacketPipeline extends MessageToMessageCodec<FMLProxyPacket, AbstractPacket> {

    private EnumMap<Side, FMLEmbeddedChannel> channels;
    private LinkedList<Class<? extends AbstractPacket>> packets = new LinkedList<Class<? extends AbstractPacket>>();
    private boolean isPostInitialized = false;

    public boolean registerPacket(Class<? extends AbstractPacket> packetClass){
        if(this.packets.size() >= 256){
            LogHelper.fatal("Mod registered more than 256 packets please report this to the mod author: karelmikie3.", References.MOD_NAME);
            return false;
        }

        if(this.packets.contains(packetClass)){
            LogHelper.fatal("Mod registered same packet twice please report this to the mod author: karelmikie3.", References.MOD_NAME);
            return false;
        }

        if(this.isPostInitialized){
            LogHelper.fatal("Mod registed the packet to late please report this to the mod author: karelmikie3.", References.MOD_NAME);
            return false;
        }

        this.packets.add(packetClass);
        return true;
    }

    public void initialize(){
        this.channels = NetworkRegistry.INSTANCE.newChannel(References.PACKET_CHANNEL_NAME, this);

        registerPackets();
    }

    public void postInitialize(){
        if(isPostInitialized)
            return;

        isPostInitialized = true;
        Collections.sort(this.packets, new Comparator<Class<? extends AbstractPacket>>() {
            @Override
            public int compare(Class<? extends AbstractPacket> packetClass1, Class<? extends AbstractPacket> packetClass2) {
                int comparison = String.CASE_INSENSITIVE_ORDER.compare(packetClass1.getCanonicalName(), packetClass2.getCanonicalName());
                if(comparison == 0)
                    comparison = packetClass1.getCanonicalName().compareTo(packetClass2.getCanonicalName());

                return comparison;
            }
        });
    }

    public void registerPackets(){
        registerPacket(MessageColor.class);
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, AbstractPacket msg, List<Object> out) throws Exception {
        ByteBuf byteBuf =  Unpooled.buffer();
        Class<? extends AbstractPacket> packetClass = msg.getClass();

        if(!this.packets.contains(packetClass)){
            throw new NullPointerException("The package: " + packetClass.getCanonicalName() + " Has never been initialized.");
        }

        byte discriminator = (byte) this.packets.indexOf(packetClass);
        byteBuf.writeByte(discriminator);
        msg.encodeInto(ctx, byteBuf);

        FMLProxyPacket proxyPacket = new FMLProxyPacket(byteBuf.copy(), ctx.channel().attr(NetworkRegistry.FML_CHANNEL).get());
        out.add(proxyPacket);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, FMLProxyPacket msg, List<Object> out) throws Exception {
        ByteBuf payload = msg.payload();
        byte discriminator = payload.readByte();

        Class<? extends AbstractPacket> packetClass = this.packets.get(discriminator);

        if(packetClass == null)
            throw new NullPointerException("The package: " + packetClass.getCanonicalName() + " Has never been initialized.");

        AbstractPacket abstractPacket = packetClass.newInstance();
        abstractPacket.decodeInto(ctx, payload.slice());

        EntityPlayer player;
        switch(FMLCommonHandler.instance().getEffectiveSide()){
            case CLIENT:
                player = this.getClientPlayer();
                abstractPacket.handleClientSide(player);
                break;
            case SERVER:
                INetHandler iNetHandler = ctx.channel().attr(NetworkRegistry.NET_HANDLER).get();
                player = ((NetHandlerPlayServer) iNetHandler).playerEntity;
                abstractPacket.handleServerSide(player);
                break;
        }

        out.add(abstractPacket);
    }

    @SideOnly(Side.CLIENT)
    private EntityPlayer getClientPlayer(){
        return Minecraft.getMinecraft().thePlayer;
    }

    public void sendToAll(AbstractPacket message) {
        this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALL);
        this.channels.get(Side.SERVER).writeAndFlush(message);
    }

    public void sendTo(AbstractPacket message, EntityPlayerMP player) {
        this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.PLAYER);
        this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(player);
        this.channels.get(Side.SERVER).writeAndFlush(message);
    }

    public void sendToAllAround(AbstractPacket message, NetworkRegistry.TargetPoint point) {
        this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALLAROUNDPOINT);
        this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(point);
        this.channels.get(Side.SERVER).writeAndFlush(message);
    }

    public void sendToDimension(AbstractPacket message, int dimensionId) {
        this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.DIMENSION);
        this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(dimensionId);
        this.channels.get(Side.SERVER).writeAndFlush(message);
    }

    public void sendToServer(AbstractPacket message){
        this.channels.get(Side.CLIENT).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.TOSERVER);
        this.channels.get(Side.CLIENT).writeAndFlush(message);
    }
}
