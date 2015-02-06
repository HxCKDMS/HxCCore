package HxCKDMS.HxCCore.Events;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.EventListener;
import java.util.HashMap;

public class EventIsOp implements EventListener {
    int checkTimer = 0;
    public static HashMap<String, Boolean> OppedPlayers = new HashMap<String, Boolean>();

    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event){
        if(event.entity instanceof EntityPlayerMP && checkTimer == 0 && !event.entity.worldObj.isRemote){
            EntityPlayerMP player = ((EntityPlayerMP) event.entity);
            OppedPlayers.put(player.getUniqueID().toString(), player.mcServer.getConfigurationManager().canSendCommands(player.getGameProfile()));
        }
        checkTimer++;
        if(checkTimer > 100){
            checkTimer = 0;
        }
    }
}
