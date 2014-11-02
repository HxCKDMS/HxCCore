package HxCKDMS.HxCCore.Events;

import HxCKDMS.HxCCore.HxCCore;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

import java.io.File;
import java.io.IOException;
import java.util.EventListener;

public class EventJoinWorld implements EventListener {

    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent event){
        if(event.entity instanceof EntityPlayer){
            try {
                EntityPlayer player = (EntityPlayer) event.entity;
                String UUID = player.getUniqueID().toString();
                File CustomPlayerData = new File(HxCCore.HxCCoreDir, UUID);
                if (!CustomPlayerData.exists()) {
                    CustomPlayerData.createNewFile();
                }
            }catch(IOException exceptions){
                exceptions.printStackTrace();
            }
        }
    }
}
