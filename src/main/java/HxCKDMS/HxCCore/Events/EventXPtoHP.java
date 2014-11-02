package HxCKDMS.HxCCore.Events;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.living.LivingEvent;

import java.util.UUID;

public class EventXPtoHP {
    public double MH;
    public int XP;
    public static UUID BuffUUID = UUID.fromString("edff168f-32d7-438b-8d29-189e9405e032");
    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event){
        if (event.entity instanceof EntityPlayerMP){
            EntityPlayerMP PMP = (EntityPlayerMP) event.entity;
            IAttributeInstance Player = PMP.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.maxHealth);
            MH = PMP.getMaxHealth() - 10;
            XP = PMP.experienceLevel;
            if (MH != XP){
                AttributeModifier exHP = new AttributeModifier(BuffUUID, "DrZedHealthBuff", XP * 0.1, 1);
                Player.removeModifier(exHP);
                Player.applyModifier(exHP);
            }
        }
    }
}