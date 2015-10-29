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

    public static void draw2DEllipsoid(World world, int x, int y, int z, Block block, int radius, boolean hollow, double checkCounter, int blockMeta, double n) {
        Thread thread = new Thread(() -> {
            double step = 2 * Math.PI / 2000;

            for(double theta=0;  theta < 2*Math.PI; theta+=step) {
                int xl = (int)Math.round(radius * Math.cos(theta));
                int zl = (int)Math.round(radius * Math.sin(theta));

                System.out.println(zl);

                checkAndPlaceBlock(world, block, x + xl - 1, y, z + zl - 1, blockMeta);
            }
        });
        thread.setName("2DEllipsoidCalculationThread");
        thread.setDaemon(true);
        thread.start();
    }

    public static void draw3DEllipsoid(World world, int x, int y, int z, Block block, int radius, boolean hollow, double checkCounter, int blockMeta, double n) {
        Thread thread = new Thread(() -> {
            double stepTheta = 2 * Math.PI / 2000;
            double stepPhi = Math.PI / 2000;

            for(double theta = 0;  theta < 2 * Math.PI; theta += stepTheta) {
                for(double phi = 0; phi < Math.PI; phi += stepPhi) {
                    int xl = (int)Math.round(radius * Math.cos(theta) * Math.sin(phi));
                    int yl = (int)Math.round(radius * Math.sin(theta) * Math.sin(phi));
                    int zl = (int)Math.round(radius * Math.cos(phi));

                    checkAndPlaceBlock(world, block, x + xl - 1, y + yl + 1, z + zl - 1, blockMeta);
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
