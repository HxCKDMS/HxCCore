package HxCKDMS.HxCCore.Events;

import HxCKDMS.HxCCore.Handlers.NBTFileIO;
import HxCKDMS.HxCCore.HxCCore;
import HxCKDMS.HxCCore.network.MessageColor;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

import java.io.File;
import java.util.EventListener;
import java.util.List;

@SuppressWarnings({"unused", "unchecked"})
public class EventNickSync implements EventListener {
    private int counter = 0;

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event){
        if(event.phase == TickEvent.Phase.START){
            if((counter++) == 200){
                List<EntityPlayerMP> players = (List<EntityPlayerMP>) HxCCore.server.getConfigurationManager().playerEntityList;
                for(EntityPlayerMP player : players) if(EventPlayerNetworkCheck.hasPlayerMod.contains(player.getUniqueID())) HxCCore.network.sendTo(new MessageColor(getNickTagCompound(players)), player);
            }
            if(counter > 200) counter = 0;
        }
    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event){
        List<EntityPlayerMP> players = (List<EntityPlayerMP>) HxCCore.server.getConfigurationManager().playerEntityList;
        for(EntityPlayerMP player : players) if(EventPlayerNetworkCheck.hasPlayerMod.contains(player.getUniqueID())) HxCCore.network.sendTo(new MessageColor(getNickTagCompound(players)), player);
    }

    private NBTTagCompound getNickTagCompound(List<EntityPlayerMP> players) {
        NBTTagCompound tagCompound = new NBTTagCompound();

        for (EntityPlayerMP player : players) {
            NBTTagCompound tagCompound2 = new NBTTagCompound();
            String UUID = player.getUniqueID().toString();

            File CustomPlayerData = new File(HxCCore.HxCCoreDir, "HxC-" + UUID + ".dat");

            String nick = "";
            try{
                nick = NBTFileIO.getString(CustomPlayerData, "nickname");
            }catch(NullPointerException ignored){}

            tagCompound2.setString("nick", nick);
            tagCompound2.setBoolean("isOP", player.mcServer.getConfigurationManager().canSendCommands(player.getGameProfile()));

            tagCompound.setTag(UUID, tagCompound2);

        }

        return tagCompound;
    }
}