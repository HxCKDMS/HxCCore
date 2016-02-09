package HxCKDMS.HxCCore.Events;

import HxCKDMS.HxCCore.Configs.Configurations;
import HxCKDMS.HxCCore.HxCCore;
import HxCKDMS.HxCCore.lib.References;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.living.LivingEvent;

@SuppressWarnings("unused")
public class EventXPtoBuffs {
    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entity instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) event.entity;
            IAttributeInstance PlayerH = player.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.maxHealth);
            IAttributeInstance PlayerD = player.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.attackDamage);
            double CurrentHealth = player.getMaxHealth() - 20;
            double CurrentDamage = PlayerD.getAttributeValue()-1;
            int BuffPoints = player.experienceLevel / Configurations.XPBuffPerLevels;
            if (CurrentHealth != BuffPoints && CurrentHealth <= Configurations.MaxHealth) {
                AttributeModifier exHP = new AttributeModifier(References.HPBuffUUID, "DrZedHealthBuff", BuffPoints, 0);
                PlayerH.removeModifier(exHP);
                if (HxCCore.instance.HxCRules.get("XPBuffs").equals("true"))
                    PlayerH.applyModifier(exHP);
                player.sendPlayerAbilities();
                player.setPlayerHealthUpdated();
            }
            if (CurrentDamage != BuffPoints && CurrentDamage <= Configurations.MaxDamage) {
                AttributeModifier exDM = new AttributeModifier(References.DMBuffUUID, "DrZedDamageBuff", BuffPoints, 0);
                PlayerD.removeModifier(exDM);
                if (HxCCore.instance.HxCRules.get("XPBuffs").equals("true"))
                    PlayerD.applyModifier(exDM);
                player.sendPlayerAbilities();
            }
        }
    }
}
