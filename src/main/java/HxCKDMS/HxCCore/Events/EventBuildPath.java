package HxCKDMS.HxCCore.Events;

import HxCKDMS.HxCCore.api.Handlers.NBTFileIO;
import HxCKDMS.HxCCore.HxCCore;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraftforge.event.entity.living.LivingEvent;

import java.io.File;

public class EventBuildPath {
    @SubscribeEvent
    public void eventUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entityLiving instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP)event.entityLiving;
            String UUID = player.getUniqueID().toString();
            File CustomPlayerData = new File(HxCCore.HxCCoreDir, "HxC-" + UUID + ".dat");
            if (NBTFileIO.getBoolean(CustomPlayerData, "Pathing") && !player.worldObj.isRemote) {
                Block block = NBTFileIO.hasKey(CustomPlayerData, "PathMat") ? Block.getBlockFromName(NBTFileIO.getString(CustomPlayerData, "PathMat")) : Blocks.stone;
                int range = NBTFileIO.hasKey(CustomPlayerData, "PathMat") ? NBTFileIO.getInteger(CustomPlayerData, "PathSize") : 3;
                int meta = NBTFileIO.hasKey(CustomPlayerData, "PathMat") ? NBTFileIO.getInteger(CustomPlayerData, "PathMeta") : 0;
                for (int x = (int)player.posX - range; x <= (int)player.posX + range; x++)
                    for (int z = (int)player.posZ - range; z <= (int)player.posZ + range; z++)
                        if (player.worldObj.getBlock(x, (int)player.posY-1, z) != block || player.worldObj.getBlockMetadata(x, (int)player.posY-1, z) != meta)
                            player.worldObj.setBlock(x, (int)player.posY-1, z, block, meta, 3);
            }
        }
    }
}
