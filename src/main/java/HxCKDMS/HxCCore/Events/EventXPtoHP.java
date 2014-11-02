package HxCKDMS.HxCCore.Events;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.living.LivingEvent;

public class EventXPtoHP {
    @SubscribeEvent
    public void Event(LivingEvent.LivingUpdateEvent event){
        if (event.entity instanceof EntityPlayerMP){
            EntityPlayerMP player = (EntityPlayerMP) event.entity;
            IAttributeInstance ph = player.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.maxHealth);
            float xp = player.experienceLevel;
            double HealthBoost = xp * 1;
            AttributeModifier HealthBuff = new AttributeModifier("DrZedsHealthBonus", HealthBoost, 1);
            ph.removeModifier(HealthBuff);
            ph.applyModifier(HealthBuff);
        }
    }
}
