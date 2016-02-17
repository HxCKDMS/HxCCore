package HxCKDMS.HxCCore.Events;

import HxCKDMS.HxCCore.Handlers.NBTFileIO;
import HxCKDMS.HxCCore.HxCCore;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.BlockPos;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;

public class EventBuildPath {
    @SubscribeEvent
    public void eventUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entityLiving instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP)event.entityLiving;
            String UUID = player.getUniqueID().toString();
            File CustomPlayerData = new File(HxCCore.HxCCoreDir, "HxC-" + UUID + ".dat");
            if (NBTFileIO.getBoolean(CustomPlayerData, "Pathing") && !player.worldObj.isRemote) {
                int meta = NBTFileIO.getInteger(CustomPlayerData, "PathMeta");
                IBlockState blockState = Block.getBlockFromName(NBTFileIO.getString(CustomPlayerData, "PathMat")).getStateFromMeta(meta);
                int range = NBTFileIO.getInteger(CustomPlayerData, "PathSize");
                for (int x = (int)player.posX-range; x < (int)player.posX +range; x++)
                    for (int z = (int)player.posZ-range; z < (int)player.posZ +range; z++)
                        if (player.worldObj.getBlockState(new BlockPos(x, (int) player.posY - 1, z)) != blockState)
                            player.worldObj.setBlockState(new BlockPos(x, (int)player.posY-1, z), blockState, 3);
            }
        }
    }
}
