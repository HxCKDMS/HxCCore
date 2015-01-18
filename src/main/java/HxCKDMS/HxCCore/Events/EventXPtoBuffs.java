package HxCKDMS.HxCCore.Events;

import HxCKDMS.HxCCore.Configs.Config;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
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
            if (Config.CooldownDisable){
                player.xpCooldown = 0;
            }
        }
        if (event.entityLiving instanceof EntityPlayer && Config.XPBuffs && !Loader.isModLoaded("HxCSkills")){
            EntityPlayer player = (EntityPlayer) event.entityLiving;
            IAttributeInstance PlayerH = player.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.maxHealth);
            IAttributeInstance PlayerD = player.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.attackDamage);
            MH = player.getMaxHealth() - 10;
            MD = PlayerD.getAttributeValue();
            XP = player.experienceLevel/LPB;
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