package HxCKDMS.HxCCore.Events;

import HxCKDMS.HxCCore.Handlers.NBTFileIO;
import HxCKDMS.HxCCore.HxCCore;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

import java.io.File;
import java.io.IOException;
import java.util.EventListener;

public class EventJoinWorld implements EventListener {
    
    @SuppressWarnings("ResultOfMethodCallIgnored")
    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (event.entity instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) event.entity;
            
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
            if (HxCCore.proxy.getSide().equals(Side.SERVER)) {
                File PermissionsData = new File(HxCCore.HxCCoreDir, "HxC-Permissions.dat");
                NBTTagCompound Permissions = NBTFileIO.getNbtTagCompound(PermissionsData, "Permissions");
                NBTFileIO.setNbtTagCompound(HxCCore.HxCCoreDir, "Permissions", Permissions);
                NBTTagCompound PermLevel = Permissions.getCompoundTag("PermLevel");

                int pl = (PermLevel.getInteger(player.getDisplayName()));

                if (pl == 0) {
                    PermLevel.setInteger(player.getDisplayName(), 0);
                }
                NBTFileIO.setNbtTagCompound(HxCCore.HxCCoreDir, "Permissions", Permissions);
/*
                File colorData = new File(HxCCore.HxCCoreDir, "HxCColorData.dat");
                NBTTagCompound data;
                try {
                    data = NBTFileIO.getData(colorData);
                }catch(Exception unused){
                    data = new NBTTagCompound();
                }
                Set keys = data.func_150296_c();
                for (Object key : keys) {
                    if (key instanceof String) {
                        HxCCore.packetPipeLine.sendTo(new MessageColor((String) key, data.getString((String) key).toCharArray()[0]), (EntityPlayerMP) player);
                    }
                }*/
            }
        }
    }
}
