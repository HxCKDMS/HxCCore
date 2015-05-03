package HxCKDMS.HxCCore.Utils;

import net.minecraft.block.Block;
import net.minecraft.world.World;

@SuppressWarnings("unused")
public class PlaceHelper {
    public static boolean PlaceCustomFromItem(int x, int y, int z, int side, Block block, World world, int metadata, int flag){
        if(side == 0) {
            world.setBlock(x, y - 1, z, block, metadata, flag);
            return true;
        }else if(side == 1) {
            world.setBlock(x, y + 1, z, block, metadata, flag);
            return true;
        }else if(side == 2) {
            world.setBlock(x, y, z - 1, block, metadata, flag);
            return true;
        }else if(side == 3) {
            world.setBlock(x, y, z + 1, block, metadata, flag);
            return true;
        }else if(side == 4) {
            world.setBlock(x - 1, y, z, block, metadata, flag);
            return true;
        }else if(side == 5) {
            world.setBlock(x + 1, y, z, block, metadata, flag);
            return true;
        }else {
            return false;
        }
    }

    public static boolean PlaceCustomFromItem(int x, int y, int z, int side, Block block, World world){
        return PlaceCustomFromItem(x, y, z, side, block, world, 0, 0);
    }
}
