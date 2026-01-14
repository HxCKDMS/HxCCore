package hxckdms.hxccore.event;

import hxckdms.hxccore.configs.Configuration;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.EventListener;
import java.util.UUID;

public class EventXPBuffs implements EventListener {
    private static final UUID HEALTH_UUID = UUID.fromString("edff168f-32d7-438b-8d29-189e9405e032");
    private static final UUID DAMAGE_UUID = UUID.fromString("17cb8d52-6376-11e4-b116-123b93f75cba");
    private static final UUID ATK_SPEED_UUID = UUID.fromString("352a7989-8b7a-4bb5-8e21-fd86469f2f47");
    private static final UUID MOVE_SPEED_UUID = UUID.fromString("c15e7d78-07c5-46c2-9b12-a49a75a7513c");
    private static final UUID ARMOUR_UUID = UUID.fromString("8f70e9aa-0533-4e06-b137-7671b94dd667");
    private static final UUID TOUGHNESS_UUID = UUID.fromString("fcb68be8-71c8-4f30-becd-5e44a46ed92a");
    private static final UUID LUCK_UUID = UUID.fromString("2bb86719-f7f5-4984-a477-f1d2024eca49");
    private static final UUID KNOCKBACK_UUID = UUID.fromString("4c1493cf-8ab5-4152-a626-96ac283d4532-");

    @SubscribeEvent
    public void applyBuffEvent(LivingEvent.LivingUpdateEvent event) {

        if (event.getEntityLiving() instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) event.getEntityLiving();

            boolean enabled = player.worldObj.getGameRules().getBoolean("HxC_XPBuffs");
            if (!enabled || player.experienceLevel < 1) return;

            IAttributeInstance playerHealthAttributes = player.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MAX_HEALTH);
            IAttributeInstance playerDamageAttributes = player.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ATTACK_DAMAGE);
            IAttributeInstance playerSpeedAttributes = player.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MOVEMENT_SPEED);
            IAttributeInstance playerKnockbackResistAttributes = player.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.KNOCKBACK_RESISTANCE);
            IAttributeInstance playerAttackSpeedAttributes = player.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ATTACK_SPEED);
            IAttributeInstance playerArmourAttributes = player.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ARMOR);
            IAttributeInstance playerArmourToughAttributes = player.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ARMOR_TOUGHNESS);
            IAttributeInstance playerLuckAttributes = player.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.LUCK);


            /* Operation 0=add 1=multiply_base 2=multiply*/
            double healthBuff = Math.min(Configuration.maxBonusHealth, (player.experienceLevel / Configuration.buffPerLevelLife) * Configuration.healthPerBuff);
            AttributeModifier attributeModifier;
            if (Math.round(healthBuff) != 0) {
                attributeModifier = playerHealthAttributes.getModifier(HEALTH_UUID);
                if (attributeModifier == null || attributeModifier.getAmount() != healthBuff) {
                    playerHealthAttributes.removeModifier(HEALTH_UUID);
                    if (enabled)
                        playerHealthAttributes.applyModifier(new AttributeModifier(HEALTH_UUID, "HxCHealthBuff", healthBuff, 0));
                }
            }


            double damageBuff = Math.min(Configuration.maxBonusDamage, (player.experienceLevel / Configuration.buffPerLevelDmg) * Configuration.damagePerBuff);

            attributeModifier = playerDamageAttributes.getModifier(DAMAGE_UUID);
            if (attributeModifier == null || attributeModifier.getAmount() != damageBuff) {
                playerDamageAttributes.removeModifier(DAMAGE_UUID);
                if (enabled) playerDamageAttributes.applyModifier(new AttributeModifier(DAMAGE_UUID, "HxCDamageBuff", damageBuff, 0));
            }

            double atkSpeedBuff = Math.min(Configuration.maxBonusAtkSpeed, (player.experienceLevel / Configuration.buffPerLevelAtkSpeed) * Configuration.atkSpeedPerBuff);

            attributeModifier = playerAttackSpeedAttributes.getModifier(ATK_SPEED_UUID);
            if (attributeModifier == null || attributeModifier.getAmount() != atkSpeedBuff) {
                playerAttackSpeedAttributes.removeModifier(ATK_SPEED_UUID);
                if (enabled) playerAttackSpeedAttributes.applyModifier(new AttributeModifier(ATK_SPEED_UUID, "HxCAtkSpeedBuff", atkSpeedBuff, 0));
            }

            double mvSpeedBuff = Math.min(Configuration.maxBonusMvSpeed, (player.experienceLevel / Configuration.buffPerLevelMvSpeed) * Configuration.moveSpeedPerBuff);

            attributeModifier = playerSpeedAttributes.getModifier(MOVE_SPEED_UUID);
            if (attributeModifier == null || attributeModifier.getAmount() != mvSpeedBuff) {
                playerSpeedAttributes.removeModifier(MOVE_SPEED_UUID);
                if (enabled) playerSpeedAttributes.applyModifier(new AttributeModifier(MOVE_SPEED_UUID, "HxCMvSpeedBuff", mvSpeedBuff, 0));
            }

            double KnockbackBuff = Math.min(Configuration.maxBonusKnockbackResist, (player.experienceLevel / Configuration.buffPerLevelKnockback) * Configuration.knockbackResistPerBuff);

            attributeModifier = playerKnockbackResistAttributes.getModifier(KNOCKBACK_UUID);
            if (attributeModifier == null || attributeModifier.getAmount() != KnockbackBuff) {
                playerKnockbackResistAttributes.removeModifier(KNOCKBACK_UUID);
                if (enabled) playerKnockbackResistAttributes.applyModifier(new AttributeModifier(KNOCKBACK_UUID, "HxCKnockbackBuff", KnockbackBuff, 0));
            }

            double armourBuff = Math.min(Configuration.maxBonusArmour, (player.experienceLevel / Configuration.buffPerLevelArmour) * Configuration.armourPerBuff);

            attributeModifier = playerArmourAttributes.getModifier(ARMOUR_UUID);
            if (attributeModifier == null || attributeModifier.getAmount() != armourBuff) {
                playerArmourAttributes.removeModifier(ARMOUR_UUID);
                if (enabled) playerArmourAttributes.applyModifier(new AttributeModifier(ARMOUR_UUID, "HxCArmourBuff", armourBuff, 0));
            }

            double armourToughnessBuff = Math.min(Configuration.maxBonusArmourToughness, (player.experienceLevel / Configuration.buffPerLevelToughness) * Configuration.toughnessPerBuff);

            attributeModifier = playerArmourToughAttributes.getModifier(TOUGHNESS_UUID);
            if (attributeModifier == null || attributeModifier.getAmount() != armourToughnessBuff) {
                playerArmourToughAttributes.removeModifier(TOUGHNESS_UUID);
                if (enabled) playerArmourToughAttributes.applyModifier(new AttributeModifier(TOUGHNESS_UUID, "HxCArmourToughnessBuff", armourToughnessBuff, 0));
            }

            double luckBuff = Math.min(Configuration.maxBonusLuck, (player.experienceLevel / Configuration.buffPerLevelLuck) * Configuration.luckPerBuff);

            attributeModifier = playerLuckAttributes.getModifier(LUCK_UUID);
            if (attributeModifier == null || attributeModifier.getAmount() != luckBuff) {
                playerLuckAttributes.removeModifier(LUCK_UUID);
                if (enabled) playerLuckAttributes.applyModifier(new AttributeModifier(LUCK_UUID, "HxCLuckBuff", luckBuff, 0));
            }


            player.sendPlayerAbilities();
            player.setPlayerHealthUpdated();
        }
    }
}
