package HxCKDMS.HxCCore.api.Utils;

import net.minecraft.block.state.BlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

//@SuppressWarnings("unused")
public class WorldHelper {
    private static double nthrt(double d, double n) {
        if(n == 2) return Math.sqrt(d);
        else if(n == 3) return Math.cbrt(d);
        else return Math.pow(d, 1.0D / n);
    }

    public static void draw2DEllipsoid(World world, BlockState blockState, BlockPos blockPos, int radius, boolean hollow, double checkCounter, double n) {
        Thread thread = new Thread(() -> {
            for (float xr = -radius; xr <= radius; xr += checkCounter){
                float zrPowered = (float)Math.pow(radius, n) - (float)Math.pow(xr, n);
                if(zrPowered < 0) continue;
                int zl = Math.round((float) nthrt(zrPowered, n));

                BlockPos pos = blockPos.add(Math.round(xr), 0, zl);
                BlockPos pos2 = blockPos.add(Math.round(xr), 0, -zl);


                checkAndPlaceBlock(world, blockState, pos);
                checkAndPlaceBlock(world, blockState, pos2);

                if(!hollow)
                    for(int zf = pos.getZ() - zl; zf <= pos.getZ() + zl; zf++)
                        checkAndPlaceBlock(world, blockState, new BlockPos(pos.getX(), pos.getY(), zf));
            }
        });
        thread.setName("2DEllipsoidCalculationThread");
        thread.setDaemon(true);
        thread.start();
    }

    public static void draw3DEllipsoid(World world, BlockState blockState, BlockPos blockPos, int radius, boolean hollow, double checkCounter, double n) {
        Thread thread = new Thread(() -> {
            for(float xr = -radius; xr <= radius; xr += checkCounter){
                for(float zr = -radius; zr <= radius; zr += checkCounter){
                    float yrPowered = (float) Math.pow(radius, n) - ((float) Math.pow(xr, n) + (float) Math.pow(zr, n));
                    if (yrPowered < 0) continue;
                    int yl = Math.round((float) nthrt(yrPowered, n));

                    BlockPos pos = blockPos.add(Math.round(xr), yl, Math.round(zr));
                    BlockPos pos2 = blockPos.add(Math.round(xr), -yl, Math.round(zr));

                    checkAndPlaceBlock(world, blockState, pos);
                    checkAndPlaceBlock(world, blockState, pos2);

                    if(!hollow)
                        for (int yf = blockPos.getY() - yl; yf <= blockPos.getY() + yl; yf++)
                            checkAndPlaceBlock(world,  blockState, new BlockPos(pos.getX(), yf, pos.getZ()));
                }
            }
        });

        thread.setName("3DEllipsoidCalculationThread");
        thread.setDaemon(true);
        thread.start();
    }

    private synchronized static void checkAndPlaceBlock(World world, BlockState state, BlockPos pos) {
        if(world.getBlockState(pos) != state && pos.getY() >= 0) world.setBlockState(pos, state.getBaseState());
    }
}
