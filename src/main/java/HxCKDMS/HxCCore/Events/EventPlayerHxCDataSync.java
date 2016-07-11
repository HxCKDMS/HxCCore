package HxCKDMS.HxCCore.Events;

import HxCKDMS.HxCCore.HxCCore;
import HxCKDMS.HxCCore.api.Handlers.NBTFileIO;
import HxCKDMS.HxCCore.network.PacketClientSync;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.living.LivingEvent;

import java.io.File;
import java.util.EventListener;
import java.util.HashMap;

@SuppressWarnings("unused")
public class EventPlayerHxCDataSync implements EventListener {
    private static HashMap<String, Short> delay = new HashMap<>();

    @SubscribeEvent
    public void syncData(LivingEvent.LivingUpdateEvent event) {
        if (event.entityLiving instanceof EntityPlayerMP && EventPlayerNetworkCheck.hasPlayerMod.contains(event.entityLiving.getUniqueID()) && (!delay.containsKey(event.entityLiving.getUniqueID().toString()) || delay.get(event.entityLiving.getUniqueID().toString()) < 1)) {
            String UUID = event.entityLiving.getUniqueID().toString();
            delay.putIfAbsent(UUID, (short)200);
            delay.replace(UUID, (short)200);
            File CustomPlayerData = new File(HxCCore.HxCCoreDir, "HxC-" + UUID + ".dat");
            HxCCore.network.sendTo(new PacketClientSync(NBTFileIO.getData(CustomPlayerData)), (EntityPlayerMP) event.entityLiving);
        } else {
            delay.putIfAbsent(event.entityLiving.getUniqueID().toString(), (short)200);
            delay.replace(event.entityLiving.getUniqueID().toString(), (short)(delay.get(event.entityLiving.getUniqueID().toString()) - 1));
        }
    }
}
