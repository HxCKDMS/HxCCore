package HxCKDMS.HxCCore.Events;

import HxCKDMS.HxCCore.Configs.Configurations;
import HxCKDMS.HxCCore.lib.References;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.living.LivingEvent;

@SuppressWarnings("unused")
public class EventXPtoBuffs {
    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        boolean xpbuff = (Configurations.XPBuffs && !Loader.isModLoaded("HxCSkills"));
        if (event.entity instanceof EntityPlayerMP) {
            EntityPlayerMP PMP = (EntityPlayerMP) event.entity;
            if (PMP.xpCooldown > 0)
                PMP.xpCooldown = 0;
            if (xpbuff) {
                IAttributeInstance PlayerH = PMP.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.maxHealth);
                IAttributeInstance PlayerD = PMP.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.attackDamage);
                double MH = PMP.getMaxHealth() - 10;
                double MD = PlayerD.getAttributeValue();
                int XP = PMP.experienceLevel / 5;
                if (MH != XP * 5 && MH <= Configurations.HPMax) {
                    AttributeModifier exHP = new AttributeModifier(References.HPBuffUUID, "DrZedHealthBuff", XP * 0.1, 0);
                    PlayerH.removeModifier(exHP);
                    PlayerH.applyModifier(exHP);
                }
                if (MD != XP * 5 && MD <= Configurations.DMMax) {
                    AttributeModifier exDM = new AttributeModifier(References.DMBuffUUID, "DrZedDamageBuff", XP * 0.1, 0);
                    PlayerD.removeModifier(exDM);
                    PlayerD.applyModifier(exDM);
                }
            }
        }
    }
}
