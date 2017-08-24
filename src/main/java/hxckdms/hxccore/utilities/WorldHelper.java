package hxckdms.hxccore.utilities;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldHelper {
    public static double power(double number, int power) {
        double result = 1;
        for (int i = 0; i < power; ++i) result *= number;
        return result;
    }

    private static double nthRoot(double number, int power) {
        if (power == 1) return number;
        else if (power == 2) return Math.sqrt(number);
        else if (power == 3) return Math.cbrt(number);
        else return Math.pow(number, 1D / (double) power);
    }

    public static void draw2DEllipsoid(World world, int x, int y, int z, Block block, int blockMeta, int radius, boolean hollow, double precision, int n) {
        Thread thread = new Thread(() -> {
            for (float X_offset = -radius; X_offset <= radius; X_offset += precision) {

                double Z_offsetPowered = power(radius, n) - power(X_offset, n);
                if (Z_offsetPowered < 0) continue;
                float Z_offset = (float) nthRoot(Z_offsetPowered, n);

                int uX_offset = Math.round(X_offset);
                int uZ_offset = Math.round(Z_offset);
                checkAndPlaceBlock(world, block, x + uX_offset + (x < 0 ? -1 : 0), y, z + uZ_offset + (z < 0 ? 1 : 0), blockMeta);
                checkAndPlaceBlock(world, block, x - uX_offset + (x < 0 ? -1 : 0), y, z - uZ_offset + (z < 0 ? 1 : 0), blockMeta);

                if (!hollow) {
                    for (int X_fill = -uX_offset; X_fill < uX_offset; X_fill++) {
                        checkAndPlaceBlock(world, block, x + X_fill + (x < 0 ? -1 : 0), y, z + uZ_offset + (z < 0 ? 1 : 0), blockMeta);
                        checkAndPlaceBlock(world, block, x + X_fill + (x < 0 ? -1 : 0), y, z - uZ_offset + (z < 0 ? 1 : 0), blockMeta);
                    }
                }
            }
        });
        thread.setName("2DEllipsoidCalculationThread");
        thread.setDaemon(true);
        thread.start();
    }

    public static void draw3DEllipsoid(World world, int x, int y, int z, Block block, int blockMeta, int radius, boolean hollow, double precision, int n) {
        Thread thread = new Thread(() -> {
            for (float X_offset = -radius; X_offset < radius; X_offset += precision) {
                for (float Z_offset = -radius; Z_offset < radius; Z_offset += precision) {
                    double Y_offset_powered = power(radius, n) - power(X_offset, n) - power(Z_offset, n);
                    if(Y_offset_powered < 0) continue;
                    float Y_offset = (float) nthRoot(Y_offset_powered, n);

                    int uX_offset = Math.round(X_offset);
                    int uY_offset = Math.round(Y_offset);
                    int uZ_offset = Math.round(Z_offset);

                    checkAndPlaceBlock(world, block, x + uX_offset + (x < 0 ? -1 : 0), y + uY_offset, z + uZ_offset + (z < 0 ? 1 : 0), blockMeta);
                    checkAndPlaceBlock(world, block, x + uX_offset + (x < 0 ? -1 : 0), y - uY_offset, z + uZ_offset + (z < 0 ? 1 : 0), blockMeta);

                    if (!hollow) {
                        for (int Y_fill = -uY_offset; Y_fill < uY_offset; Y_fill++) {
                            checkAndPlaceBlock(world, block, x + uX_offset + (x < 0 ? -1 : 0), y + Y_fill, z + uZ_offset + (z < 0 ? 1 : 0), blockMeta);
                            checkAndPlaceBlock(world, block, x + uX_offset + (x < 0 ? -1 : 0), y - Y_fill, z + uZ_offset + (z < 0 ? 1 : 0), blockMeta);
                        }
                    }
                }
            }
        });

        thread.setName("3DEllipsoidCalculationThread");
        thread.setDaemon(true);
        thread.start();
    }

    @SuppressWarnings("deprecation")
    private synchronized static void checkAndPlaceBlock(World world, Block block, int x, int y, int z, int blockMeta) {
        if(world.getBlockState(new BlockPos(x, y, z)) != block.getDefaultState() && y >= 0) world.setBlockState(new BlockPos(x, y, z), block.getStateFromMeta(blockMeta), 1 | 2);
    }
}
