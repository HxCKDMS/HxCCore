package HxCKDMS.HxCCore.Events;

import HxCKDMS.HxCCore.Handlers.NBTFileIO;
import HxCKDMS.HxCCore.HxCCore;
import HxCKDMS.HxCCore.network.MessageColor;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

import java.io.File;
import java.util.EventListener;

@SuppressWarnings("unused")
public class EventNickSync implements EventListener {
    private int counter = 0;

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event){
        if(counter == 0){
            for(Object object : HxCCore.server.getConfigurationManager().playerEntityList){
                if(object instanceof EntityPlayerMP){
                    EntityPlayerMP player = (EntityPlayerMP) object;
                    String UUID = player.getUniqueID().toString();

                    File CustomPlayerData = new File(HxCCore.HxCCoreDir, "HxC-" + UUID + ".dat");

                    String nick;

                    try{
                        nick = NBTFileIO.getString(CustomPlayerData, "nickname");
                    }catch(NullPointerException unhandled){
                        nick = "";
                    }

                    HxCCore.packetPipeLine.sendToAll(new MessageColor(UUID, nick, player.mcServer.getConfigurationManager().func_152596_g(player.getGameProfile())));
                }
            }
        }
        counter++;
        if(counter >= 2400){
            counter = 0;
        }
    }

    @SubscribeEvent
    public void onPlayerJoin(EntityJoinWorldEvent event){
        if(event.entity instanceof EntityPlayerMP){
            for(Object object : HxCCore.server.getConfigurationManager().playerEntityList){
                if(object instanceof EntityPlayerMP){
                    EntityPlayerMP player = (EntityPlayerMP) object;
                    String UUID = player.getUniqueID().toString();

                    File CustomPlayerData = new File(HxCCore.HxCCoreDir, "HxC-" + UUID + ".dat");

                    String nick;

                    try{
                        nick = NBTFileIO.getString(CustomPlayerData, "nickname");
                    }catch(NullPointerException unhandled){
                        nick = "";
                    }

                    HxCCore.packetPipeLine.sendToAll(new MessageColor(UUID, nick, player.mcServer.getConfigurationManager().func_152596_g(player.getGameProfile())));
                }
            }
        }
    }
}
