package hxckdms.hxccore.events;

import hxckdms.hxccore.utilities.HxCPlayerInfoHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Calendar;
import java.util.EventListener;

public class CommandEvents implements EventListener {
    private long flyTimeTracker = 0;

    @SubscribeEvent
    public void eventFly(LivingEvent.LivingUpdateEvent event) {
        if (Calendar.getInstance().getTimeInMillis() - flyTimeTracker >= 2000L && event.getEntityLiving() instanceof EntityPlayerMP) {
            flyTimeTracker = Calendar.getInstance().getTimeInMillis();
            EntityPlayerMP player = (EntityPlayerMP) event.getEntityLiving();

            if (HxCPlayerInfoHandler.getBoolean(player, "AllowFlying")) {
                player.capabilities.allowFlying = true;
                player.sendPlayerAbilities();
            }
        }
    }
}
