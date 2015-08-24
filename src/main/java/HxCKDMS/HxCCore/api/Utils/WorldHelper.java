package HxCKDMS.HxCCore.api.Utils;

import net.minecraft.block.Block;
import net.minecraft.world.World;

//@SuppressWarnings("unused")
public class WorldHelper {
    private static double nthrt(double d, double n) {
        if(n == 2) return Math.sqrt(d);
        else if(n == 3) return Math.cbrt(d);
        else return Math.pow(d, 1.0D / n);
    }

    public strictfp static void draw2DEllipsoid(final World world, final int x, final int y, final int z, final Block block, final int radius, final boolean hollow, final double checkCounter, final int blockMeta, final double n) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (float xr = -radius; xr <= radius; xr += checkCounter){
                    float zrPowered = (float)Math.pow(radius, n) - (float)Math.pow(xr, n);
                    if(zrPowered < 0) continue;
                    int zl = Math.round((float) nthrt(zrPowered, n));

                    int xPlace = x + Math.round(xr);
                    int zPlace1 = z + zl;
                    int zPlace2 = z - zl;

                    checkAndPlaceBlock(world, block, xPlace, y, zPlace1, blockMeta);
                    checkAndPlaceBlock(world, block, xPlace, y, zPlace2, blockMeta);

                    if(!hollow)
                        for(int zf = z - zl; zf <= z + zl; zf++)
                            checkAndPlaceBlock(world, block, xPlace, y, zf, blockMeta);
                }
            }
        });
        thread.setName("2DEllipsoidCalculationThread");
        thread.setDaemon(true);
        thread.start();
    }

    public static void draw3DEllipsoid(final World world, final int x, final int y, final int z, final Block block, final int radius, final boolean hollow, final double checkCounter, final int blockMeta, final double n) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for(float xr = -radius; xr <= radius; xr += checkCounter){
                    for(float zr = -radius; zr <= radius; zr += checkCounter){
                        float yrPowered = (float) Math.pow(radius, n) - ((float) Math.pow(xr, n) + (float) Math.pow(zr, n));
                        if (yrPowered < 0) continue;
                        int yl = Math.round((float) nthrt(yrPowered, n));

                        int xPlace = x + Math.round(xr);
                        int yPlace1 = y + yl;
                        int yPlace2 = y - yl;
                        int zPlace = z + Math.round(zr);

                        checkAndPlaceBlock(world, block, xPlace, yPlace1, zPlace, blockMeta);
                        checkAndPlaceBlock(world, block, xPlace, yPlace2, zPlace, blockMeta);

                        if(!hollow)
                            for (int yf = y - yl; yf <= y + yl; yf++)
                                checkAndPlaceBlock(world, block, xPlace, yf, zPlace, blockMeta);
                    }
                }
            }
        });

        thread.setName("3DEllipsoidCalculationThread");
        thread.setDaemon(true);
        thread.start();
    }

    private synchronized static void checkAndPlaceBlock(World world, Block block, int x, int y, int z, int blockMeta) {
        if(world.getBlock(x, y, z) != block && y >= 0) world.setBlock(x, y, z, block, blockMeta, 3);
    }
}
