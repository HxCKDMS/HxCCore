package HxCKDMS.HxCCore.Events;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import java.util.EventListener;

public class EventGod implements EventListener {
    public static EventGod instance = new EventGod();

    @SubscribeEvent(receiveCanceled = true)
    public void playerHurt(LivingHurtEvent event){
        if(event.entity instanceof EntityPlayer){
            EntityPlayer player = ((EntityPlayer) event.entity);
            NBTTagCompound playerData = player.getEntityData();
            try{
                if(playerData.getBoolean("god") && event.isCancelable()){
                    event.setCanceled(true);
                }
            }catch(NullPointerException e){
                playerData.setBoolean("god", false);
            }
        }
    }

    @SubscribeEvent(receiveCanceled = true)
    public void playerDeath(LivingDeathEvent event){
        if(event.entity instanceof EntityPlayer) {
            EntityPlayer player = ((EntityPlayer) event.entity);
            NBTTagCompound playerData = player.getEntityData();
            try {
                if (playerData.getBoolean("god") && event.isCancelable()) {
                    event.setCanceled(true);
                }
            } catch (NullPointerException e) {
                playerData.setBoolean("god", false);
            }
        }
    }
}
