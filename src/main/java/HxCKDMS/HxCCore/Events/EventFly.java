package HxCKDMS.HxCCore.Events;

import HxCKDMS.HxCCore.Handlers.NBTFileIO;
import HxCKDMS.HxCCore.HxCCore;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingEvent;

import java.io.File;

public class EventFly {
    private int timer = 20;
    @SubscribeEvent
    public void eventUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entityLiving instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer)event.entityLiving;
            String UUID = player.getUniqueID().toString();
            File CustomPlayerData = new File(HxCCore.HxCCoreDir, "HxC-" + UUID + ".dat");
            timer--;
            if(!CustomPlayerData.exists()) return;
            if (timer <= 0 && NBTFileIO.getBoolean(CustomPlayerData, "fly")) {
                timer = 600;
                player.capabilities.allowFlying = true;
                player.sendPlayerAbilities();
            }
        }
    }
}
