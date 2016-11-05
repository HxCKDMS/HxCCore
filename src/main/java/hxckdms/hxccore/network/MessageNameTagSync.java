package hxckdms.hxccore.network;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import hxckdms.hxccore.libraries.GlobalVariables;
import hxckdms.hxccore.utilities.ColorHelper;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class MessageNameTagSync implements IMessage {
    private NBTTagCompound tagCompound = new NBTTagCompound();

    public MessageNameTagSync() {}

    public MessageNameTagSync(List<EntityPlayerMP> playerList) {
        for (EntityPlayerMP player : playerList) {
            NBTTagCompound subTag = new NBTTagCompound();
            subTag.setString("Nick", ColorHelper.handleNick(player, false).getFormattedText());
            subTag.setBoolean("IsOP", Arrays.asList(GlobalVariables.server.getConfigurationManager().func_152603_m().func_152685_a()).contains(player.getDisplayName()));

            tagCompound.setTag(player.getUniqueID().toString(), subTag);
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        tagCompound = ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeTag(buf, tagCompound);
    }

    public static class Handler implements IMessageHandler<MessageNameTagSync, IMessage> {

        @Override
        public IMessage onMessage(MessageNameTagSync message, MessageContext ctx) {
            for (String uuid : (Set<String>) message.tagCompound.func_150296_c()) {
                NBTTagCompound subTag = message.tagCompound.getCompoundTag(uuid);

                ColorHelper.playerNickNames.put(UUID.fromString(uuid), subTag.getString("Nick"));
                ColorHelper.isPlayerOp.put(UUID.fromString(uuid), subTag.getBoolean("IsOP"));
            }
            return null;
        }
    }
}
