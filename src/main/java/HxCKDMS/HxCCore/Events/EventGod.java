package HxCKDMS.HxCCore.Events;

import HxCKDMS.HxCCore.HxCCore;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
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
            File CustomPlayerData = new File(HxCCore.HxCCoreDir, UUID + ".dat");

            try {
                NBTTagCompound playerData = CompressedStreamTools.read(CustomPlayerData);
                if (playerData.getBoolean("god") && event.isCancelable()) {
                    event.setCanceled(true);
                }
            } catch (Exception e) {
                try {
                    NBTTagCompound playerData = CompressedStreamTools.read(CustomPlayerData);
                    playerData.setBoolean("god", false);
                    CompressedStreamTools.write(playerData, CustomPlayerData);
                }catch(Exception exception){
                    exception.printStackTrace();
                }
            }
        }
    }

    @SubscribeEvent(receiveCanceled = true)
    public void playerDeath(LivingDeathEvent event){
        if(event.entity instanceof EntityPlayer) {
            EntityPlayer player = ((EntityPlayer) event.entity);
            String UUID = player.getUniqueID().toString();
            File CustomPlayerData = new File(HxCCore.HxCCoreDir, UUID + ".dat");

            try {
                NBTTagCompound playerData = CompressedStreamTools.read(CustomPlayerData);
                if (playerData.getBoolean("god") && event.isCancelable()) {
                    event.setCanceled(true);
                }
            } catch (Exception e) {
                try {
                    NBTTagCompound playerData = CompressedStreamTools.read(CustomPlayerData);
                    playerData.setBoolean("god", false);
                }catch(Exception exception){
                    exception.printStackTrace();
                }
            }
        }
    }

    @SubscribeEvent(receiveCanceled = true)
    public void playerUpdate(LivingEvent.LivingUpdateEvent event){
        if(event.entity instanceof EntityPlayer && HealTimer >= 20){
            EntityPlayer player = ((EntityPlayer) event.entity);
            String UUID = player.getUniqueID().toString();
            File CustomPlayerData = new File(HxCCore.HxCCoreDir, UUID + ".dat");
            try {
                NBTTagCompound playerData = CompressedStreamTools.read(CustomPlayerData);
                if (playerData.getBoolean("god") && event.isCancelable()) {
                    player.heal(player.getMaxHealth() - player.getHealth());
                }
            } catch (Exception e) {
                try {
                    NBTTagCompound playerData = CompressedStreamTools.read(CustomPlayerData);
                    playerData.setBoolean("god", false);
                }catch(Exception exception){
                    exception.printStackTrace();
                }
            }
        }
        HealTimer++;
        if(HealTimer >= 20){
            HealTimer = 0;
        }
    }
}
