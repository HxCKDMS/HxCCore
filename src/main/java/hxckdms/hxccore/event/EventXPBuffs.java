package hxckdms.hxccore.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import hxckdms.hxccore.configs.Configuration;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;

import java.util.EventListener;
import java.util.HashMap;
import java.util.UUID;

public class EventXPBuffs implements EventListener {
    private static final UUID HEALTH_UUID = UUID.fromString("edff168f-32d7-438b-8d29-189e9405e032");
    private static final UUID DAMAGE_UUID = UUID.fromString("17cb8d52-6376-11e4-b116-123b93f75cba");
    public static boolean enabled = true;
    private static HashMap<String, Integer> lastXP = new HashMap<>();
    @SubscribeEvent
    public void applyBuffEvent(LivingEvent.LivingUpdateEvent event) {
        if (event.entityLiving instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) event.entityLiving;
			enabled = player.worldObj.getGameRules().getGameRuleBooleanValue("HxC_XPBuffs");
			if (enabled) {
				int lastKnownXP = 0;
				if (!lastXP.containsKey(player.getCommandSenderName())) {
					lastKnownXP = player.experienceLevel;
					lastXP.put(player.getCommandSenderName(), player.experienceLevel);
				} else {
					lastKnownXP = lastXP.get(player.getCommandSenderName());
					lastXP.replace(player.getCommandSenderName(), player.experienceLevel);
				}


				IAttributeInstance playerHealthAttributes = player.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.maxHealth);
				IAttributeInstance playerDamageAttributes = player.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.attackDamage);

				double healthBuff = Math.min(Configuration.maxBonusHealth, (player.experienceLevel / Configuration.XPBuffPerLevels) * Configuration.healthPerBuff);


				AttributeModifier attributeModifier = playerHealthAttributes.getModifier(HEALTH_UUID);
				if (attributeModifier == null || attributeModifier.getAmount() != healthBuff) {
					if (attributeModifier != null) playerHealthAttributes.removeModifier(attributeModifier);
					if (enabled) playerHealthAttributes.applyModifier(new AttributeModifier(HEALTH_UUID, "HxCHealthBuff", healthBuff, 0));
				}
				double damageBuff = Math.min(Configuration.maxBonusDamage, (player.experienceLevel / Configuration.XPBuffPerLevels) * Configuration.damagePerBuff);

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

    @SubscribeEvent
    public void livingHurtEvent(LivingHurtEvent event) {
        /*if (event.entityLiving instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP)event.entityLiving;
            float ammount = event.ammount;
        }*/
        if (enabled) {
            if (event.source.getSourceOfDamage() instanceof EntityPlayerMP) {
                EntityPlayerMP player = (EntityPlayerMP) event.source.getSourceOfDamage();
                if (player.getHeldItem() != null && player.getHeldItem().isItemEnchanted()) {
                    if (EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, player.getHeldItem()) > 0) {
                        event.ammount *= (1 + (EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, player.getHeldItem()) * 0.25));
                    }
                    if (EnchantmentHelper.getEnchantmentLevel(Enchantment.smite.effectId, player.getHeldItem()) > 0 && (event.entityLiving.isEntityUndead() || event.entityLiving instanceof EntityEnderman)) {
                        event.ammount *= (1 + (EnchantmentHelper.getEnchantmentLevel(Enchantment.smite.effectId, player.getHeldItem()) * 0.5));
                    }
                    if (EnchantmentHelper.getEnchantmentLevel(Enchantment.baneOfArthropods.effectId, player.getHeldItem()) > 0 && event.entityLiving instanceof EntitySpider) {
                        event.ammount *= (1 + (EnchantmentHelper.getEnchantmentLevel(Enchantment.baneOfArthropods.effectId, player.getHeldItem()) * 0.5));
                    }
                }
                if (player.experienceLevel > 5 && player.shouldHeal()) {
                    player.heal(event.ammount * (player.experienceLevel * Configuration.lifestealPerBuff));
                }
            }
        }
    }


    @SubscribeEvent
    public void PlayerEvent(PlayerEvent.BreakSpeed event) {
        if (enabled) {
            event.newSpeed = (event.originalSpeed + event.originalSpeed * ((Configuration.miningSpeedPerBuff * event.entityPlayer.experienceLevel)));
        }
    }
}
