package HxCKDMS.HxCCore.api.Utils;

import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

@SuppressWarnings("unused")
public class WorldHelper {
    /**
     * @param world {@link World} where the circle is going to be made.
     * @param pos the position of the block.
     * @param block the {@link Block} which makes up the circle.
     * @param radius the radius of the circle.
     * @param hollow true if the circle is hollow, false if it's filled.
     * @param checkCounter the amount it adds each time it is calculated.
     */
    public static void drawCircle(World world, BlockPos pos, Block block, int radius, boolean hollow, double checkCounter){
        drawCircle(world, pos, block, radius, hollow, checkCounter, 0);
    }

    /**
     * @param world {@link World} where the circle is going to be made.
     * @param pos the position of the block.
     * @param block the {@link Block} which makes up the circle.
     * @param radius the radius of the circle.
     * @param hollow true if the circle is hollow, false if it's filled.
     * @param checkCounter the amount it adds each time it is calculated.
     * @param blockMeta the MetaData that's assigned to the block.
     */
    public static void drawCircle(World world, BlockPos pos, Block block, int radius, boolean hollow, double checkCounter, int blockMeta){
        for(float xr = -radius; xr <= radius; xr += checkCounter){
            float zrSquared =  (float)square(radius) - (float)square(xr);
            if(zrSquared < 0) continue;
            int zl = Math.round((float) Math.sqrt(zrSquared));

            world.setBlockState(pos.add(Math.round(xr), 0, zl), block.getStateFromMeta(blockMeta));
            world.setBlockState(pos.add(Math.round(xr), 0, -zl), block.getStateFromMeta(blockMeta));

            if(!hollow)
                for(int zf = pos.getY() - zl; zf <= pos.getY() + zl; zf++)
                    world.setBlockState(pos.add(Math.round(xr), 0, zf), block.getStateFromMeta(blockMeta));
        }
    }

    /**
     * @param world {@link World} where the sphere is going to be made.
     * @param pos the position of the block.
     * @param block the {@link Block} which makes up the sphere.
     * @param radius the radius of the sphere.
     * @param hollow true if the sphere is hollow, false if it's filled.
     * @param checkCounter the amount it adds each time it is calculated.
     */
    public static void drawSphere(World world, BlockPos pos, Block block, int radius, boolean hollow, double checkCounter){
        drawSphere(world, pos, block, radius, hollow, checkCounter, 0);
    }

    /**
     * @param world {@link World} where the sphere is going to be made.
     * @param pos the position of the block.
     * @param block the {@link Block} which makes up the sphere.
     * @param radius the radius of the sphere.
     * @param hollow true if the sphere is hollow, false if it's filled.
     * @param checkCounter the amount it adds each time it is calculated.
     * @param blockMeta the MetaData that's assigned to the block.
     */
    public static void drawSphere(World world, BlockPos pos, Block block, int radius, boolean hollow, double checkCounter, int blockMeta){
        for(float xr = -radius; xr <= radius; xr += checkCounter){
            for(float zr = -radius; zr <= radius; zr += checkCounter){
                float yrSquared = (float) square(radius) - ((float) square(xr) + (float) square(zr));
                if (yrSquared < 0) continue;
                int yl = Math.round((float) Math.sqrt(yrSquared));

                world.setBlockState(pos.add(Math.round(xr), yl, Math.round(zr)), block.getStateFromMeta(blockMeta));
                world.setBlockState(pos.add(Math.round(xr), -yl, Math.round(zr)), block.getStateFromMeta(blockMeta));

                if(!hollow)
                    for(int yf = pos.getY() - yl; yf <= pos.getY() + yl; yf++)
                        world.setBlockState(pos.add(Math.round(xr), yf, Math.round(zr)), block.getStateFromMeta(blockMeta));

            }
        }
    }
    
    private static double square(double d){
        return d * d;
    }
}
