package HxCKDMS.HxCCore.Events;

import HxCKDMS.HxCCore.HxCCore;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

import java.io.File;
import java.io.IOException;
import java.util.EventListener;

public class EventJoinWorld implements EventListener {

    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent event){
        if(event.entity instanceof EntityPlayer){
            try {
                EntityPlayer player = (EntityPlayer) event.entity;
                String UUID = player.getUniqueID().toString();
                File CustomPlayerData = new File(HxCCore.HxCCoreDir, UUID + ".dat");
                if (!CustomPlayerData.exists()) {
                    CustomPlayerData.createNewFile();
                }
                NBTTagCompound playerData = CompressedStreamTools.read(CustomPlayerData);
                playerData.setString("playerName", player.getDisplayName());
                CompressedStreamTools.write(playerData, CustomPlayerData);
            }catch(IOException exceptions){
                exceptions.printStackTrace();
            }
        }
    }
}
