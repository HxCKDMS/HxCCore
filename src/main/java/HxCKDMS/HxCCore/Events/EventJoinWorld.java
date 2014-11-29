package HxCKDMS.HxCCore.Events;

import java.io.File;
import java.io.IOException;
import java.util.EventListener;
import java.util.Set;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import HxCKDMS.HxCCore.HxCCore;
import HxCKDMS.HxCCore.Handlers.NBTFileIO;
import HxCKDMS.HxCCore.network.MessageColor;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

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
                
                NBTFileIO.setString(CustomPlayerData, "username", player.getDisplayName());
            } catch (IOException exceptions) {
                exceptions.printStackTrace();
            }
            
            // Send username colors to player
            if (player instanceof EntityPlayerMP) {
                File colorData = new File(HxCCore.HxCCoreDir, "HxCColorData.dat");
                NBTTagCompound data = NBTFileIO.getData(colorData);
                Set keys = data.func_150296_c();
                for (Object key : keys) {
                    if (key instanceof String) {
                        HxCCore.network.sendTo(new MessageColor.Message((String) key, data.getString((String) key).toCharArray()[0]), (EntityPlayerMP) player);
                    }
                }
            }
        }
    }
}
