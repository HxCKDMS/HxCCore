package HxCKDMS.HxCCore.network;

import HxCKDMS.HxCCore.Events.EventIsOp;
import HxCKDMS.HxCCore.Handlers.NBTFileIO;
import HxCKDMS.HxCCore.HxCCore;
import HxCKDMS.HxCCore.renderers.RenderHxCPlayer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.io.File;

public class MessageColor extends AbstractPacket {
    public String nick;
    public String UUID;
    public Boolean isOpped;

    public MessageColor() {}

    /**
     * Used when send to the client.
     */
    public MessageColor(String UUID, String nick, Boolean bool) {
        this.UUID = UUID;
        this.nick = nick;
        this.isOpped = bool;
    }
    
    /**
      * Used when send to the server.
     */
    public MessageColor(String UUID) {
        this.UUID = UUID;
        this.nick = "";
        this.isOpped = false;
    }

    @Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf byteBuf) {
        ByteBufUtils.writeUTF8String(byteBuf, nick);
        ByteBufUtils.writeUTF8String(byteBuf, UUID);
        ByteBufUtils.writeUTF8String(byteBuf, isOpped.toString());
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf byteBuf) {
        this.nick = ByteBufUtils.readUTF8String(byteBuf);
        this.UUID = ByteBufUtils.readUTF8String(byteBuf);
        this.isOpped = Boolean.parseBoolean(ByteBufUtils.readUTF8String(byteBuf));
    }

    @Override
    public void handleClientSide(EntityPlayer player) {
        RenderHxCPlayer.nameNicks.put(UUID, nick);
        RenderHxCPlayer.isPlayerOp.put(UUID, isOpped);
    }

    @Override
    public void handleServerSide(EntityPlayer player) {
        File CustomPlayerData = new File(HxCCore.HxCCoreDir, "HxC-" + UUID + ".dat");
        try{
            nick = NBTFileIO.getString(CustomPlayerData, "nickname");
        }catch(NullPointerException unhandled){
            nick = "";
        }
        HxCCore.packetPipeLine.sendToAll(new MessageColor(UUID, nick, EventIsOp.OppedPlayers.get(UUID)));
    }
}