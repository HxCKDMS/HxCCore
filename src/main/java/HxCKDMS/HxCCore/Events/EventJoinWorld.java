package HxCKDMS.HxCCore.Events;

import HxCKDMS.HxCCore.Handlers.NBTFileIO;
import HxCKDMS.HxCCore.HxCCore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;
import java.io.IOException;
import java.util.EventListener;

public class EventJoinWorld implements EventListener {
    
    @SuppressWarnings("ResultOfMethodCallIgnored")
    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (event.entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.entity;
            
            // Player data file
            try {
                String UUID = player.getUniqueID().toString();
                File CustomPlayerData = new File(HxCCore.HxCCoreDir, "HxC-" + UUID + ".dat");
                
                if (!CustomPlayerData.exists()) CustomPlayerData.createNewFile();
                
                NBTFileIO.setString(CustomPlayerData, "username", player.getDisplayName().toString());
            } catch (IOException exceptions) {
                exceptions.printStackTrace();
            }
            
            // Send username colors to player
/*            if (HxCCore.proxy.getSide().equals(Side.SERVER) && player instanceof EntityPlayerMP) {
                File colorData = new File(HxCCore.HxCCoreDir, "HxCColorData.dat");
                NBTTagCompound data = NBTFileIO.getData(colorData);
                Set keys = data.func_150296_c();
                for (Object key : keys) {
                    if (key instanceof String) {
                        HxCCore.network.sendTo(new MessageColor.Message((String) key, data.getString((String) key).toCharArray()[0]), (EntityPlayerMP) player);
                    }
                }
            }*/
        }
    }
}
