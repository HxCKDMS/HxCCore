package HxCKDMS.HxCCore.Events;

import HxCKDMS.HxCCore.api.Handlers.NBTFileIO;
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
            if (NBTFileIO.getBoolean(CustomPlayerData, "Pathing") && !player.worldObj.isRemote) {
                Block block = Block.getBlockFromName(NBTFileIO.getString(CustomPlayerData, "PathMat"));
                int range = NBTFileIO.getInteger(CustomPlayerData, "PathSize");
                int meta = NBTFileIO.getInteger(CustomPlayerData, "PathMeta");
                for (int x = (int)player.posX-range; x < (int)player.posX +range; x++)
                    for (int z = (int)player.posZ-range; z < (int)player.posZ +range; z++)
                        if (player.worldObj.getBlock(x, (int)player.posY-1, z) != block || player.worldObj.getBlockMetadata(x, (int)player.posY-1, z) != meta)
                            player.worldObj.setBlock(x, (int)player.posY-1, z, block, meta, 3);
            }
        }
    }
}
