package HxCKDMS.HxCCore.Utils;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.ArrayList;

@SuppressWarnings("unused")
public class AABBUtils {
    public static ArrayList<Block> getBlocksWithinAABB(World world, AxisAlignedBB box){
        ArrayList<Block> blocks = new ArrayList<>();

        for(int x = (int) box.minX; x <= box.maxX; x++){
            for(int y = (int) box.minY; y <= box.maxY; y++){
                for(int z = (int) box.minZ; z <= box.maxZ; z++){
                    Block block = world.getBlock(x, y, z);
                    if(block != null){
                        blocks.add(block);
                    }
                }
            }
        }
        return blocks;
    }

    public static ArrayList<TileEntity> getTileEntitiesWithinAABB(World world, AxisAlignedBB box){
        ArrayList<TileEntity> tileEntities = new ArrayList<>();

        for(int x = (int) box.minX; x <= box.maxX; x++){
            for(int y = (int) box.minY; y <= box.maxY; y++){
                for(int z = (int) box.minZ; z <= box.maxZ; z++){
                    TileEntity tileEntity = world.getTileEntity(x, y, z);
                    if(tileEntity != null)
                        tileEntities.add(tileEntity);
                }
            }
        }
        return tileEntities;
    }
}
