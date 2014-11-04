package HxCKDMS.HxCCore.Events;

import HxCKDMS.HxCCore.Handlers.NBTFileIO;
import HxCKDMS.HxCCore.HxCCore;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import java.io.File;
import java.util.EventListener;

public class EventGod implements EventListener {
    private int HealTimer = 0;

    @SubscribeEvent(receiveCanceled = true)
    public void playerHurt(LivingHurtEvent event){
        if(event.entity instanceof EntityPlayer){
            EntityPlayer player = ((EntityPlayer) event.entity);
            String UUID = player.getUniqueID().toString();
            File CustomPlayerData = new File(HxCCore.HxCCoreDir, "HxC-" + UUID + ".dat");

            if (NBTFileIO.getBoolean(CustomPlayerData, "god") && event.isCancelable()) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent(receiveCanceled = true)
    public void playerDeath(LivingDeathEvent event){
        if(event.entity instanceof EntityPlayer){
            EntityPlayer player = ((EntityPlayer) event.entity);
            String UUID = player.getUniqueID().toString();
            File CustomPlayerData = new File(HxCCore.HxCCoreDir, "HxC-" + UUID + ".dat");

            if (NBTFileIO.getBoolean(CustomPlayerData, "god") && event.isCancelable()) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent(receiveCanceled = true)
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event){
        if(event.entity instanceof EntityPlayer && HealTimer >= 20){
            EntityPlayer player = ((EntityPlayer) event.entity);
            String UUID = player.getUniqueID().toString();
            File CustomPlayerData = new File(HxCCore.HxCCoreDir, "HxC-" + UUID + ".dat");
            if(NBTFileIO.getBoolean(CustomPlayerData, "god")){
                player.heal(player.getMaxHealth() - player.getHealth());
                player.getFoodStats().addStats(20, 20F);
            }
        }
        HealTimer++;
        if(HealTimer >= 21){
            HealTimer = 0;
        }
    }
}
