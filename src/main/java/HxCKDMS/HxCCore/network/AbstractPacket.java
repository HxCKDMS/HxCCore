package HxCKDMS.HxCCore.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;

public abstract class AbstractPacket {
    public abstract void encodeInto(ChannelHandlerContext ctx, ByteBuf byteBuf);

    public abstract void decodeInto(ChannelHandlerContext ctx, ByteBuf byteBuf);

    public abstract void handleClientSide(EntityPlayer player);

    public abstract void handleServerSide(EntityPlayer player);
}
