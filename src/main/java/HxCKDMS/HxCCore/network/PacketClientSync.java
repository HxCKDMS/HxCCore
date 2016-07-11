package HxCKDMS.HxCCore.network;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;

public class PacketClientSync implements IMessage {
    private NBTTagCompound tagCompound;

    //Empty constructor needed for FML initializing the packet.
    public PacketClientSync() {
    }

    public PacketClientSync(NBTTagCompound tagCompound) {
        this.tagCompound = tagCompound;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.tagCompound = ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeTag(buf, tagCompound);
    }

    @SuppressWarnings("unchecked")
    public static class Handler implements IMessageHandler<PacketClientSync, IMessage> {
        @Override
        public IMessage onMessage(PacketClientSync message, MessageContext ctx) {
            NBTTagCompound tagCompound = message.tagCompound;
            //Was going to try to stop movement client side for AFK, failed many times going to bed
            return null;
        }
    }
}
