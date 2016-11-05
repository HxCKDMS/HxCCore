package hxckdms.hxccore.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import hxckdms.hxccore.configs.Configuration;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.living.LivingEvent;

import java.util.EventListener;
import java.util.UUID;

public class EventXPBuffs implements EventListener {
    private static final UUID HEALTH_UUID = UUID.fromString("edff168f-32d7-438b-8d29-189e9405e032");
    private static final UUID DAMAGE_UUID = UUID.fromString("17cb8d52-6376-11e4-b116-123b93f75cba");

    @SubscribeEvent
    public void applyBuffEvent(LivingEvent.LivingUpdateEvent event) {

        if (event.entityLiving instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) event.entityLiving;

            boolean enabled = player.worldObj.getGameRules().getGameRuleBooleanValue("HxC_XPBuffs");

            IAttributeInstance playerHealthAttributes = player.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.maxHealth);
            IAttributeInstance playerDamageAttributes = player.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.attackDamage);
            double healthBuff = Math.min(Configuration.maxBonusHealth, player.experienceLevel / Configuration.XPBuffPerLevels);

            AttributeModifier attributeModifier = playerHealthAttributes.getModifier(HEALTH_UUID);
            if (attributeModifier == null || attributeModifier.getAmount() != healthBuff) {
                if (attributeModifier != null) playerHealthAttributes.removeModifier(attributeModifier);
                if (enabled) playerHealthAttributes.applyModifier(new AttributeModifier(HEALTH_UUID, "HxCHealthBuff", healthBuff, 0));
            }
            double damageBuff = Math.min(Configuration.maxBonusDamage, player.experienceLevel / Configuration.XPBuffPerLevels);

            attributeModifier = playerDamageAttributes.getModifier(DAMAGE_UUID);
            if (attributeModifier == null || attributeModifier.getAmount() != damageBuff) {
                if (attributeModifier != null) playerDamageAttributes.removeModifier(attributeModifier);
                if (enabled) playerDamageAttributes.applyModifier(new AttributeModifier(DAMAGE_UUID, "HxCDamageBuff", damageBuff, 0));
            }

            player.sendPlayerAbilities();
            player.setPlayerHealthUpdated();
        }
    }
}
