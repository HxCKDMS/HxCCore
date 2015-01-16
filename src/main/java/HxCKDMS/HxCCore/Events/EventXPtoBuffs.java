package HxCKDMS.HxCCore.Events;

import HxCKDMS.HxCCore.Config;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.UUID;

public class EventXPtoBuffs {
    public double MH;
    public double MD;
    public int XP;
    public static UUID HPBuffUUID = UUID.fromString("edff168f-32d7-438b-8d29-189e9405e032");
    public static UUID DMBuffUUID = UUID.fromString("17cb8d52-6376-11e4-b116-123b93f75cba");

    //Levels Per Boost : LPB
    public int LPB = Config.LMPer;

    //Health Per Boost : HPB
    public double HPB = Config.HPPer;
    //Damage Per Boost : DPB
    public double DPB = Config.DMPer;

    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event){
        if (event.entityLiving instanceof EntityPlayer){
            EntityPlayer player = (EntityPlayer) event.entityLiving;
            if (Config.XPCooldownWeaken){
                player.xpCooldown = 0;
            }
        }
        if (event.entityLiving instanceof EntityPlayerMP && Config.XPBuffs || !Loader.isModLoaded("HxCSkills")){
            EntityPlayerMP PMP = (EntityPlayerMP) event.entityLiving;
            IAttributeInstance PlayerH = PMP.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.maxHealth);
            IAttributeInstance PlayerD = PMP.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.attackDamage);
            MH = PMP.getMaxHealth() - 10;
            MD = PlayerD.getAttributeValue();
            XP = PMP.experienceLevel/LPB;
            if (MH != XP*LPB && MH <= Config.HPMax){
                AttributeModifier exHP = new AttributeModifier(HPBuffUUID, "DrZedHealthBuff", XP * HPB, 1);
                PlayerH.removeModifier(exHP);
                PlayerH.applyModifier(exHP);
            }if (MD != XP*LPB && MD <= Config.DMMax){
                AttributeModifier exDM = new AttributeModifier(DMBuffUUID, "DrZedDamageBuff", XP * DPB, 1);
                PlayerD.removeModifier(exDM);
                PlayerD.applyModifier(exDM);
            }
        }
    }
}