package HxCKDMS.HxCCore.Events;

import HxCKDMS.HxCCore.HxCCore;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

import java.util.EventListener;
import java.util.Map;

@SuppressWarnings("unused")
public class EventTpRequest implements EventListener {

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            for (Map.Entry<EntityPlayerMP, Integer> entry : HxCCore.TpaTimeoutList.entrySet()) {
                if (entry.getValue() > 0) {
                    entry.setValue(entry.getValue() - 1);

                    if ((entry.getValue() % 20) == 0) {
                        int timeLeft = entry.getValue() / 20;
                        ChatComponentText willExpire = new ChatComponentText("The teleportation request will expire within: " + timeLeft + ".");
                        willExpire.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.YELLOW));
                        ChatComponentText hasExpired = new ChatComponentText("The teleportation request has expired.");
                        hasExpired.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED).setBold(true));

                        if (timeLeft <= 10 && timeLeft != 0) HxCCore.tpaRequestList.get(entry.getKey()).addChatComponentMessage(willExpire);
                        else if(timeLeft == 0) HxCCore.tpaRequestList.get(entry.getKey()).addChatComponentMessage(hasExpired);
                    }
                }

                if (entry.getValue() <= 0) {
                    HxCCore.TpaTimeoutList.remove(entry.getKey());
                    HxCCore.tpaRequestList.remove(entry.getKey());
                }
            }
        }
    }
}
