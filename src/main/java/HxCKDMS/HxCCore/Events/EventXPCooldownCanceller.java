package HxCKDMS.HxCCore.Events;

import HxCKDMS.HxCCore.HxCCore;
import HxCKDMS.HxCCore.api.Utils.AABBUtils;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerPickupXpEvent;

import java.util.List;
@SuppressWarnings("unchecked")
public class EventXPCooldownCanceller {
    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entity instanceof EntityPlayerMP && HxCCore.instance.HxCRules.get("XPCooldownInterrupt").equals("true")) {
            EntityPlayerMP player = (EntityPlayerMP) event.entity;
            if (player.xpCooldown != 0)
                player.xpCooldown = 0;
            List<EntityXPOrb> xp = player.worldObj.getEntitiesWithinAABB(EntityXPOrb.class, AABBUtils.getAreaBoundingBox(Math.round((float)player.posX),Math.round((float) player.posY), Math.round((float)player.posZ), 4));
            xp.forEach(xpOrb -> {
                new PlayerPickupXpEvent(player, xpOrb);
                player.addExperience(xpOrb.getXpValue());
                xpOrb.setDead();
            });
        }
    }
}
