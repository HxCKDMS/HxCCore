package HxCKDMS.HxCCore.network;

import HxCKDMS.HxCCore.Asm.Hooks.RenderHooks;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Set;

@SuppressWarnings("unused")
public class MessageColor implements IMessage {
    public NBTTagCompound tagCompound;

    //Empty constructor needed for FML initializing the packet.
    public MessageColor() {}

    public MessageColor(NBTTagCompound tagCompound) {
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
    public static class Handler implements IMessageHandler<MessageColor, IMessage> {

        @Override
        public IMessage onMessage(MessageColor message, MessageContext ctx) {
            Set<String> UUIDs = (Set<String>) message.tagCompound.func_150296_c();
            for(String UUID : UUIDs) {
                NBTTagCompound tagCompound2 = (NBTTagCompound) message.tagCompound.getTag(UUID);
                RenderHooks.nameNicks.put(UUID, tagCompound2.getString("nick"));
                RenderHooks.isPlayerOp.put(UUID, tagCompound2.getBoolean("isOP"));
            }
            return null;
        }
    }
}
