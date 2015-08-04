package HxCKDMS.HxCCore.network;

import HxCKDMS.HxCCore.Asm.Hooks.RenderHooks;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

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
        public IMessage onMessage(final MessageColor message, final MessageContext ctx) {
            IThreadListener mainThread = Minecraft.getMinecraft();
            mainThread.addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    Set<String> UUIDs = (Set<String>) message.tagCompound.getKeySet();
                    for(String UUID : UUIDs) {
                        NBTTagCompound tagCompound2 = (NBTTagCompound) message.tagCompound.getTag(UUID);
                        RenderHooks.nameNicks.put(UUID, tagCompound2.getString("nick"));
                        RenderHooks.isPlayerOp.put(UUID, tagCompound2.getBoolean("isOP"));
                    }
                }
            });
            return null;
        }
    }
}
