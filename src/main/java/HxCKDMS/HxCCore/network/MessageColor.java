package HxCKDMS.HxCCore.network;

import HxCKDMS.HxCCore.Asm.Hooks.RenderHooks;
import HxCKDMS.HxCCore.api.AbstractPacket;
import cpw.mods.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Set;

@SuppressWarnings({"unused", "unchecked"})
public class MessageColor extends AbstractPacket {
    public NBTTagCompound tagCompound;

    public MessageColor() {}

    /**
     * Used when send to the client.
     */
    public MessageColor(NBTTagCompound tagCompound) {
        this.tagCompound = tagCompound;
    }

    @Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf byteBuf) {
        ByteBufUtils.writeTag(byteBuf, tagCompound);
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf byteBuf) {
        this.tagCompound = ByteBufUtils.readTag(byteBuf);
    }

    @Override
    public void handleClientSide(EntityPlayer player) {
        Set<String> UUIDs = (Set<String>) tagCompound.func_150296_c();
        for(String UUID : UUIDs) {
            NBTTagCompound tagCompound2 = (NBTTagCompound) tagCompound.getTag(UUID);
            RenderHooks.nameNicks.put(UUID, tagCompound2.getString("nick"));
            RenderHooks.isPlayerOp.put(UUID, tagCompound2.getBoolean("isOP"));
        }
    }

    @Override
    public void handleServerSide(EntityPlayer player) {
        //NOPE
    }
}