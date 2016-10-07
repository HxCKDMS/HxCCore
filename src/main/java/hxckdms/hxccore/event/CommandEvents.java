package hxckdms.hxccore.event;

import hxckdms.hxccore.utilities.HxCPlayerInfoHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.util.Calendar;
import java.util.EventListener;

import static hxckdms.hxccore.libraries.GlobalVariables.permissionData;

public class CommandEvents implements EventListener {
    private long flyTimeTracker = 0;

    @SubscribeEvent
    public void eventLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!permissionData.hasTag(event.player.getUniqueID().toString())) permissionData.setInteger(event.player.getUniqueID().toString(), 1);
    }



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
