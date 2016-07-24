package HxCKDMS.HxCCore.Events;

import HxCKDMS.HxCCore.api.Handlers.NBTFileIO;
import HxCKDMS.HxCCore.HxCCore;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

import java.io.File;

public class EventPlayerDeath {
    @SubscribeEvent
    public void playerDeath(LivingDeathEvent event) {
        if (event.entityLiving instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.entityLiving;
            String UUID = player.getUniqueID().toString();
            File CustomPlayerData = new File(HxCCore.HxCCoreDir, "HxC-" + UUID + ".dat");
            if (!CustomPlayerData.exists()) return;

            if (NBTFileIO.getBoolean(CustomPlayerData, "god") && event.isCancelable())
                event.setCanceled(true);
            else
                NBTFileIO.setIntArray(CustomPlayerData, "back", new int[]{(int) Math.round(player.posX), (int) Math.round(player.posY), (int) Math.round(player.posZ), player.dimension});
        }
    }
}
