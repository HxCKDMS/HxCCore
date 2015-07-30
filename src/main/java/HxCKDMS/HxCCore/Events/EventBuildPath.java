package HxCKDMS.HxCCore.Events;

import HxCKDMS.HxCCore.Handlers.NBTFileIO;
import HxCKDMS.HxCCore.HxCCore;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.living.LivingEvent;

import java.io.File;

public class EventBuildPath {
    @SubscribeEvent
    public void eventUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entityLiving instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP)event.entityLiving;
            String UUID = player.getUniqueID().toString();
            File CustomPlayerData = new File(HxCCore.HxCCoreDir, "HxC-" + UUID + ".dat");
            int range = 15;
            if (NBTFileIO.getBoolean(CustomPlayerData, "Pathing") && !player.worldObj.isRemote) {
                String block = NBTFileIO.getString(CustomPlayerData, "PathMat");
                for (int x = (int)Math.round(player.posX-range); x < (int)Math.round(player.posX +range); x++)
                    for (int z = (int)Math.round(player.posZ-range); z < (int)Math.round(player.posZ +range); z++)
                        if (player.worldObj.getBlock(x, (int)Math.round(player.posY)-1, z) != Block.getBlockFromName(block))
                            player.worldObj.setBlock(x, (int)Math.round(player.posY)-1, z, Block.getBlockFromName(block));
            }
        }
    }
}
