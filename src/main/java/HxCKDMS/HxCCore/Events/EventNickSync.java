package HxCKDMS.HxCCore.Events;

import HxCKDMS.HxCCore.Handlers.NBTFileIO;
import HxCKDMS.HxCCore.HxCCore;
import HxCKDMS.HxCCore.network.MessageColor;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
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
            if((counter++) == 600){
                List<EntityPlayerMP> players = (List<EntityPlayerMP>) HxCCore.server.getConfigurationManager().playerEntityList;
                HxCCore.network.sendToAll(new MessageColor(getNickTagCompound(players)));
            }
            if(counter > 600) counter = 0;
        }
    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event){
        List<EntityPlayerMP> players = (List<EntityPlayerMP>) HxCCore.server.getConfigurationManager().playerEntityList;
        HxCCore.network.sendToAll(new MessageColor(getNickTagCompound(players)));
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
            tagCompound2.setBoolean("isOP", player.mcServer.getConfigurationManager().func_152596_g(player.getGameProfile()));

            tagCompound.setTag(UUID, tagCompound2);

        }

        return tagCompound;
    }
}
