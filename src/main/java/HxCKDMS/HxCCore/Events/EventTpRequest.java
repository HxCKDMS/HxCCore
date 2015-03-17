package HxCKDMS.HxCCore.Events;

import HxCKDMS.HxCCore.HxCCore;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;

import java.util.EventListener;

public class EventTpRequest implements EventListener {

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event){
        for (EntityPlayerMP key : HxCCore.TpaTimeoutList.keySet()) {
            if (HxCCore.TpaTimeoutList.get(key) > 0) {
                HxCCore.TpaTimeoutList.put(key, HxCCore.TpaTimeoutList.get(key) - 1);

                if((HxCCore.TpaTimeoutList.get(key) % 20) == 0){
                    int timeLeft = HxCCore.TpaTimeoutList.get(key) / 20;
                    if(timeLeft <= 10){
                        HxCCore.tpaRequestList.get(key).addChatComponentMessage(new ChatComponentText("Teleport request will expire within: " + timeLeft + "."));
                    }
                }
            }

            if(HxCCore.TpaTimeoutList.get(key) <= 0){
                HxCCore.TpaTimeoutList.remove(key);
                HxCCore.tpaRequestList.remove(key);
            }
        }
    }
}
