package HxCKDMS.HxCCore.Events;

import HxCKDMS.HxCCore.Configs.Config;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.UUID;

@SuppressWarnings("unused")
public class EventXPtoBuffs {
    public double MH;
    public double MD;
    public int XP;
    public static UUID HPBuffUUID = UUID.fromString("edff168f-32d7-438b-8d29-189e9405e032");
    public static UUID DMBuffUUID = UUID.fromString("17cb8d52-6376-11e4-b116-123b93f75cba");
    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event){
        boolean xpbuff = (Config.XPBuffs && !Loader.isModLoaded("HxCSkills"));
        if (event.entityLiving instanceof EntityPlayer && xpbuff) {
            EntityPlayer player = (EntityPlayer)event.entityLiving;
            if (player.xpCooldown > 0){
                player.xpCooldown = 0;
            }
        }
        if (event.entity instanceof EntityPlayerMP && Config.XPBuffs){
            EntityPlayerMP PMP = (EntityPlayerMP) event.entity;
            IAttributeInstance PlayerH = PMP.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.maxHealth);
            IAttributeInstance PlayerD = PMP.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.attackDamage);
            MH = PMP.getMaxHealth() - 10;
            MD = PlayerD.getAttributeValue();
            XP = PMP.experienceLevel/5;
            if (MH != XP*5 && MH <= Config.HPMax){
                AttributeModifier exHP = new AttributeModifier(HPBuffUUID, "DrZedHealthBuff", XP * 0.1, 1);
                PlayerH.removeModifier(exHP);
                PlayerH.applyModifier(exHP);
            }if (MD != XP*5 && MD <= Config.DMMax){
                AttributeModifier exDM = new AttributeModifier(DMBuffUUID, "DrZedDamageBuff", XP * 0.1, 1);
                PlayerD.removeModifier(exDM);
                PlayerD.applyModifier(exDM);
            }
        }
    }
}