package HxCKDMS.HxCCore.api.Utils;

import net.minecraft.block.Block;
import net.minecraft.world.World;

//@SuppressWarnings("unused")
public class WorldHelper {
    public static void draw2DEllipsoid(World world, int x, int y, int z, Block block, int radius, boolean hollow, double precision, int blockMeta, double n) {
        Thread thread = new Thread(() -> {
            for (float X_offset = -radius; X_offset <= radius; X_offset += precision) {

                double Z_offsetPowered = radius * radius - X_offset * X_offset;
                if (Z_offsetPowered < 0) continue;
                float Z_offset = (float) Math.sqrt(Z_offsetPowered);

                int uX_offset = Math.round(X_offset) + (x < 0 ? -1 : 0);
                int uZ_offset = Math.round(Z_offset) + (z < 0 ? -1 : 0);
                checkAndPlaceBlock(world, block, x + uX_offset, y, z + uZ_offset, blockMeta);
                checkAndPlaceBlock(world, block, x - uX_offset, y, z - uZ_offset, blockMeta);

                if (!hollow) {
                    for (int X_fill = -uX_offset; X_fill < uX_offset; X_fill++) {
                        checkAndPlaceBlock(world, block, x + X_fill, y, z + uZ_offset, blockMeta);
                        checkAndPlaceBlock(world, block, x + X_fill, y, z - uZ_offset, blockMeta);
                    }
                }
            }
        });
        thread.setName("2DEllipsoidCalculationThread");
        thread.setDaemon(true);
        thread.start();
    }

    public static void draw3DEllipsoid(World world, int x, int y, int z, Block block, int radius, boolean hollow, double precision, int blockMeta, double n) {
        Thread thread = new Thread(() -> {
            for (float X_offset = -radius; X_offset < radius; X_offset += precision) {
                for (float Z_offset = -radius; Z_offset < radius; Z_offset += precision) {
                    double Y_offset_powered = radius * radius - X_offset * X_offset - Z_offset * Z_offset;
                    if(Y_offset_powered < 0) continue;
                    float Y_offset = (float) Math.sqrt(Y_offset_powered);

                    int uX_offset = Math.round(X_offset) + (x < 0 ? -1 : 0);
                    int uY_offset = Math.round(Y_offset);
                    int uZ_offset = Math.round(Z_offset) + (z < 0 ? -1 : 0);

                    checkAndPlaceBlock(world, block, x + uX_offset, y + uY_offset, z + uZ_offset, blockMeta);
                    checkAndPlaceBlock(world, block, x + uX_offset, y - uY_offset, z + uZ_offset, blockMeta);

                    if (!hollow) {
                        for (int Y_fill = -uY_offset; Y_fill < uY_offset; Y_fill++) {
                            checkAndPlaceBlock(world, block, x + uX_offset, y + Y_fill, z + uZ_offset, blockMeta);
                            checkAndPlaceBlock(world, block, x + uX_offset, y - Y_fill, z + uZ_offset, blockMeta);
                        }
                    }
                }
            }
        });

        thread.setName("3DEllipsoidCalculationThread");
        thread.setDaemon(true);
        thread.start();
    }

    private synchronized static void checkAndPlaceBlock(World world, Block block, int x, int y, int z, int blockMeta) {
        if(world.getBlock(x, y, z) != block && y >= 0) world.setBlock(x, y, z, block, blockMeta, 1 | 2);
    }
}
