package HxCKDMS.HxCCore.network;

import HxCKDMS.HxCCore.renderers.RenderHxCPlayer;
import cpw.mods.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;

public class MessageColor extends AbstractPacket {
    public String target;
    public char color;

    public MessageColor() {}

    public MessageColor(String target, char color) {
        this.target = target;
        this.color = color;
    }

    @Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf byteBuf) {
        ByteBufUtils.writeUTF8String(byteBuf, target);
        byteBuf.writeChar(color);
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf byteBuf) {
        this.target = ByteBufUtils.readUTF8String(byteBuf);
        this.color = byteBuf.readChar();
    }

    @Override
    public void handleClientSide(EntityPlayer player) {
        RenderHxCPlayer.nameColors.put(target, color);
    }

    @Override
    public void handleServerSide(EntityPlayer player) {
//        LogHelper.debug("[DEBUG] Somebody changed their color", Reference.MOD_NAME);
//        File colorData = new File(HxCCore.HxCCoreDir, "HxCColorData.dat");
//        NBTFileIO.setString(colorData, target, String.valueOf(color));
//        HxCCore.packetPipeLine.sendToAll(new MessageColor(target, color));
    }
}