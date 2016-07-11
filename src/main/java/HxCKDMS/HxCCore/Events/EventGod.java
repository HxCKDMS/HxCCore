package HxCKDMS.HxCCore.Events;

import HxCKDMS.HxCCore.Configs.Configurations;
import HxCKDMS.HxCCore.HxCCore;
import HxCKDMS.HxCCore.api.Handlers.NBTFileIO;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import java.io.File;
import java.util.EventListener;

@SuppressWarnings("unused")
public class EventGod implements EventListener {
    private int secondCounter = 0, delay = 0;

    @SubscribeEvent
    public void playerHurt(LivingHurtEvent event){
        if(event.entity instanceof EntityPlayer && !event.entity.worldObj.isRemote){
            EntityPlayer player = ((EntityPlayer) event.entity);
            String UUID = player.getUniqueID().toString();
            File CustomPlayerData = new File(HxCCore.HxCCoreDir, "HxC-" + UUID + ".dat");
            if(!CustomPlayerData.exists()) return;

            if (NBTFileIO.getBoolean(CustomPlayerData, "god") && event.isCancelable()) {
                event.setCanceled(true);
            } else if (NBTFileIO.getBoolean(CustomPlayerData, "AFK") && event.isCancelable() && event.source.getEntity() instanceof EntityPlayer) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event){
        if(event.entityLiving instanceof EntityPlayer && secondCounter >= 20 && !event.entityLiving.worldObj.isRemote){
            EntityPlayer player = (EntityPlayer) event.entityLiving;
            String UUID = player.getUniqueID().toString();
            File CustomPlayerData = new File(HxCCore.HxCCoreDir, "HxC-" + UUID + ".dat");
            if(!CustomPlayerData.exists()) return;
            if(NBTFileIO.getBoolean(CustomPlayerData, "god")){
                player.heal(player.getMaxHealth() - player.getHealth());
                player.getFoodStats().addStats(20, 20F);
            } else if (((EntityPlayer) event.entityLiving).capabilities.isCreativeMode && delay <= 0 && Configurations.GameMode) {
                HxCCore.instance.logCommand(player.getDisplayName() + " is in Creative mode.");
                delay = 60;
            }
        }
        secondCounter++;
        delay--;
        if(secondCounter > 20){
            secondCounter = 0;
        }
    }
}
