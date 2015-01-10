package HxCKDMS.HxCCore.network;

import HxCKDMS.HxCCore.Handlers.NBTFileIO;
import HxCKDMS.HxCCore.HxCCore;
import HxCKDMS.HxCCore.Utils.LogHelper;
import HxCKDMS.HxCCore.lib.Reference;
import HxCKDMS.HxCCore.renderers.RenderHxCPlayer;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import java.io.File;

public class MessageColor implements IMessageHandler<MessageColor.Message, IMessage> {
    
    @Override
    public IMessage onMessage(Message message, MessageContext ctx) {
        if (HxCCore.proxy.getSide().equals(Side.CLIENT)) {
            RenderHxCPlayer.nameColors.put(message.target, message.color);
        } else {
            LogHelper.info("[DEBUG] Somebody changed their color", Reference.MOD_NAME);
            File colorData = new File(HxCCore.HxCCoreDir, "HxCColorData.dat");
            NBTFileIO.setString(colorData, message.target, String.valueOf(message.color));
            HxCCore.network.sendToAll(message);
        }
        
        return null;
    }
    
    public static class Message implements IMessage {
        public String target;
        public char color;
        
        public Message() {}
        
        public Message(String target, char color) {
            this.target = target;
            this.color = color;
        }
        
        @Override
        public void fromBytes(ByteBuf buf) {
            this.target = ByteBufUtils.readUTF8String(buf);
            this.color = buf.readChar();
        }
        
        @Override
        public void toBytes(ByteBuf buf) {
            ByteBufUtils.writeUTF8String(buf, target);
            buf.writeChar(color);
        }
        
    }
    
}
