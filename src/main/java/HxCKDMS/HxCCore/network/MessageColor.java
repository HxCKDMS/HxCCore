package HxCKDMS.HxCCore.network;

import HxCKDMS.HxCCore.Asm.Hooks.RenderHooks;
import cpw.mods.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;

@SuppressWarnings("unused")
public class MessageColor extends AbstractPacket {
    public String nick;
    public String UUID;
    public Boolean isOP;

    public MessageColor() {}

    /**
     * Used when send to the client.
     */
    public MessageColor(String UUID, String nick, Boolean isOP) {
        this.UUID = UUID;
        this.nick = nick;
        this.isOP = isOP;
    }

    @Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf byteBuf) {
        ByteBufUtils.writeUTF8String(byteBuf, nick);
        ByteBufUtils.writeUTF8String(byteBuf, UUID);
        ByteBufUtils.writeUTF8String(byteBuf, isOP.toString());
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf byteBuf) {
        this.nick = ByteBufUtils.readUTF8String(byteBuf);
        this.UUID = ByteBufUtils.readUTF8String(byteBuf);
        this.isOP = Boolean.parseBoolean(ByteBufUtils.readUTF8String(byteBuf));
    }

    @Override
    public void handleClientSide(EntityPlayer player) {
        RenderHooks.nameNicks.put(UUID, nick);
        RenderHooks.isPlayerOp.put(UUID, isOP);
    }

    @Override
    public void handleServerSide(EntityPlayer player) {
        //NOPE
    }
}