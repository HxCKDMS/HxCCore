package HxCKDMS.HxCCore.Events;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.entity.player.PlayerPickupXpEvent;

public class EventXPCooldownCanceller {
    @SubscribeEvent
    public void onLivingUpdate(PlayerPickupXpEvent event) {
        event.entityPlayer.xpCooldown = 0;
    }
}
