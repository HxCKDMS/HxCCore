package hxckdms.hxccore.event;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import hxckdms.hxccore.configs.Configuration;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.player.EntityPlayerMP;
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

				double healthBuff = Math.min(Configuration.maxBonusHealth * 2, (player.experienceLevel / Configuration.LevelsPerBuff) * Configuration.healthPerBuff);


				AttributeModifier attributeModifier = playerHealthAttributes.getModifier(HEALTH_UUID);
				if (attributeModifier == null || attributeModifier.getAmount() != healthBuff) {
					if (attributeModifier != null) playerHealthAttributes.removeModifier(attributeModifier);
					if (enabled) playerHealthAttributes.applyModifier(new AttributeModifier(HEALTH_UUID, "HxCHealthBuff", healthBuff, 0));
				}
				double damageBuff = Math.min(Configuration.maxBonusDamage, (player.experienceLevel / Configuration.LevelsPerBuff) * Configuration.damagePerBuff);

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

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void livingHurtEvent(LivingHurtEvent event) {
        /*if (event.entityLiving instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP)event.entityLiving;
            float ammount = event.ammount;
        }*/
        if (enabled) {
            if (event.source.getSourceOfDamage() instanceof EntityPlayerMP) {
                EntityPlayerMP player = (EntityPlayerMP) event.source.getSourceOfDamage();
                if (Configuration.buffEnchants) {
                    if (player.getHeldItem() != null && player.getHeldItem().isItemEnchanted()) {
                        if (EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, player.getHeldItem()) > 0) {
                            event.ammount *= (1 + (EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, player.getHeldItem()) * Configuration.enchantBuff));
                        }
                        if (EnchantmentHelper.getEnchantmentLevel(Enchantment.smite.effectId, player.getHeldItem()) > 0 && (event.entityLiving.isEntityUndead() || event.entityLiving instanceof EntityEnderman)) {
                            event.ammount *= (1 + (EnchantmentHelper.getEnchantmentLevel(Enchantment.smite.effectId, player.getHeldItem()) * Configuration.enchantBuff));
                        }
                        if (EnchantmentHelper.getEnchantmentLevel(Enchantment.baneOfArthropods.effectId, player.getHeldItem()) > 0 && event.entityLiving instanceof EntitySpider) {
                            event.ammount *= (1 + (EnchantmentHelper.getEnchantmentLevel(Enchantment.baneOfArthropods.effectId, player.getHeldItem()) * Configuration.enchantBuff));
                        }
                    }
                }
                if (player.experienceLevel > Configuration.lifestealLevel) {
					if (Configuration.debugMode) {
						System.out.println("Damage Done : " + event.ammount);
						System.out.println("Player Health : " + player.getHealth());
						System.out.println("Player Max Health : " + player.getMaxHealth());
						System.out.println("Lifesteal Buff : " + Configuration.lifestealPerBuff);
						System.out.println("Levels Buff : " + Configuration.LevelsPerBuff);
					}
                    float healAmount = (int) event.ammount * ((float)(player.experienceLevel / Configuration.LevelsPerBuff) * Configuration.lifestealPerBuff);
                    healAmount = Math.min(healAmount, player.getMaxHealth() - player.getHealth());
                    if (Configuration.debugMode) {
						System.out.println(player.getDisplayName() + " should heal for " + healAmount);
					}
                    player.heal(healAmount);
                }
            }
        }
    }

    @SubscribeEvent
    public void PlayerEvent(PlayerEvent.BreakSpeed event) {
		event.newSpeed = event.originalSpeed;
        if (!event.entityPlayer.getDisplayName().contains("[") && event.entityPlayer.experienceLevel > 10) {
            boolean isTile = event.block.hasTileEntity(event.block.getDamageValue(event.entityPlayer.worldObj, event.x, event.y, event.z));
            String un = GameRegistry.findUniqueIdentifierFor(event.block).name;
            if (GameRegistry.findUniqueIdentifierFor(event.block).modId.equalsIgnoreCase("terrafirmacraft")) {
                isTile = !(un.contains("Ore") || un.contains("Stone") || un.contains("Log") || un.contains("Dirt") || un.contains("Grass") || un.equalsIgnoreCase("Charcoal"));
            }

            if (Configuration.debugMode && !un.equalsIgnoreCase("Charcoal")) {
				System.out.println("Mod ID : " + GameRegistry.findUniqueIdentifierFor(event.block).modId);
                System.out.println("Unlocalized Name : " + un);
                System.out.println("Block is Tile : " + isTile);
            }

            if (!isTile || Configuration.miningSpeedApplytoTiles) {
				float sp = ((event.block.getBlockHardness(event.entityPlayer.worldObj, event.x, event.y, event.z) * 10) * ((Configuration.miningSpeedPerBuff * (float) (event.entityPlayer.experienceLevel / Configuration.LevelsPerBuff))));
				if (sp > event.originalSpeed) {
					event.newSpeed = sp + event.originalSpeed;
				}
            }
        }
    }
}
