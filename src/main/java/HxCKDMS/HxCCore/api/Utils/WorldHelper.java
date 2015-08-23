package HxCKDMS.HxCCore.api.Utils;

import net.minecraft.block.Block;
import net.minecraft.world.World;

@SuppressWarnings("unused")
public class WorldHelper {
    /**
     * @param world {@link World} where the circle is going to be made.
     * @param x the x coordinate where of the center of the circle.
     * @param y the y coordinate where of the center of the circle.
     * @param z the z coordinate where of the center of the circle.
     * @param block the {@link Block} which makes up the circle.
     * @param radius the radius of the circle.
     * @param hollow true if the circle is hollow, false if it's filled.
     * @param checkCounter the amount it adds each time it is calculated.
     */
    public static void drawCircle(World world, int x, int y, int z, Block block, int radius, boolean hollow, double checkCounter){
        drawCircle(world, x, y, z, block, radius, hollow, checkCounter, 0);
    }

    /**
     * @param world {@link World} where the circle is going to be made.
     * @param x the x coordinate where of the center of the circle.
     * @param y the y coordinate where of the center of the circle.
     * @param z the z coordinate where of the center of the circle.
     * @param block the {@link Block} which makes up the circle.
     * @param radius the radius of the circle.
     * @param hollow true if the circle is hollow, false if it's filled.
     * @param checkCounter the amount it adds each time it is calculated.
     * @param blockMeta the MetaData that's assigned to the block.
     */
    public static void drawCircle(World world, int x, int y, int z, Block block, int radius, boolean hollow, double checkCounter, int blockMeta){
        for(float xr = -radius; xr <= radius; xr += checkCounter){
            float zrSquared = (float)square(radius) - (float)square(xr);
            if(zrSquared < 0) continue;
            int zl = Math.round((float) Math.sqrt(zrSquared));

            int xPlace = x + Math.round(xr);
            int zPlace1 = z + zl;
            int zPlace2 = z - zl;

            if(zPlace1 > z + radius) zPlace1 = z + radius;
            if(zPlace2 < z - radius) zPlace2 = z - radius;

            if(world.getBlock(xPlace, y, zPlace1) != block && y >= 0) world.setBlock(xPlace, y, zPlace1, block, blockMeta, 3);
            if(world.getBlock(xPlace, y, zPlace2) != block && y >= 0) world.setBlock(xPlace, y, zPlace2, block, blockMeta, 3);

            if(!hollow)
                for(int zf = z - zl; zf <= z + zl; zf++)
                    if(world.getBlock(xPlace, y, zf) != block && y >= 0) world.setBlock(xPlace, y, zf, block, blockMeta, 3);
        }
    }

    /**
     * @param world {@link World} where the sphere is going to be made.
     * @param x the x coordinate where of the center of the sphere.
     * @param y the y coordinate where of the center of the sphere.
     * @param z the z coordinate where of the center of the sphere.
     * @param block the {@link Block} which makes up the sphere.
     * @param radius the radius of the sphere.
     * @param hollow true if the sphere is hollow, false if it's filled.
     * @param checkCounter the amount it adds each time it is calculated.
     */
    public static void drawSphere(World world, int x, int y, int z, Block block, int radius, boolean hollow, double checkCounter){
        drawSphere(world, x, y, z, block, radius, hollow, checkCounter, 0);
    }

    /**
     * @param world {@link World} where the sphere is going to be made.
     * @param x the x coordinate where of the center of the sphere.
     * @param y the y coordinate where of the center of the sphere.
     * @param z the z coordinate where of the center of the sphere.
     * @param block the {@link Block} which makes up the sphere.
     * @param radius the radius of the sphere.
     * @param hollow true if the sphere is hollow, false if it's filled.
     * @param checkCounter the amount it adds each time it is calculated.
     * @param blockMeta the MetaData that's assigned to the block.
     */
    public static void drawSphere(World world, int x, int y, int z, Block block, int radius, boolean hollow, double checkCounter, int blockMeta){
        for(float xr = -radius; xr <= radius; xr += checkCounter){
            for(float zr = -radius; zr <= radius; zr += checkCounter){
                float yrSquared = (float) square(radius) - ((float) square(xr) + (float) square(zr));
                if (yrSquared < 0) continue;
                float yl = (float) Math.sqrt(yrSquared);

                int xPlace = Math.round(x + xr);
                int yPlace1 = Math.round(y + yl);
                int yPlace2 = Math.round(y - yl);
                int zPlace = Math.round(z + zr);

                if(yPlace1 > y + radius) yPlace1 = y + radius;
                if(yPlace2 < y - radius) yPlace2 = y - radius;

                if(world.getBlock(xPlace, yPlace1, zPlace) != block && yPlace1 >= 0) world.setBlock(xPlace, yPlace1, zPlace, block, blockMeta, 3);
                if(world.getBlock(xPlace, yPlace2, zPlace) != block && yPlace2 >= 0) world.setBlock(xPlace, yPlace2, zPlace, block, blockMeta, 3);

                if(!hollow) {
                    for (float yf = y - yl; yf <= y + yl; yf++) {
                        if(world.getBlock(xPlace, Math.round(yf), zPlace) != block && yPlace2 >= 0) world.setBlock(xPlace, Math.round(yf), zPlace, block, blockMeta, 3);
                    }
                }
            }
        }
    }
    
    private static double square(double d) {
        return d * d;
    }

    /**
     * @param world {@link World} where the squircle is going to be made.
     * @param x the x coordinate where of the center of the squircle.
     * @param y the y coordinate where of the center of the squircle.
     * @param z the z coordinate where of the center of the squircle.
     * @param block the {@link Block} which makes up the squircle.
     * @param radius the radius of the squircle.
     * @param hollow true if the squircle is hollow, false if it's filled.
     * @param checkCounter the amount it adds each time it is calculated.
     */
    public static void draw2DSquircle(World world, int x, int y, int z, Block block, int radius, boolean hollow, double checkCounter){
        draw2DSquircle(world, x, y, z, block, radius, hollow, checkCounter, 0);
    }

    /**
     * @param world {@link World} where the squircle is going to be made.
     * @param x the x coordinate where of the center of the squircle.
     * @param y the y coordinate where of the center of the squircle.
     * @param z the z coordinate where of the center of the squircle.
     * @param block the {@link Block} which makes up the squircle.
     * @param radius the radius of the squircle.
     * @param hollow true if the squircle is hollow, false if it's filled.
     * @param checkCounter the amount it adds each time it is calculated.
     * @param blockMeta the MetaData that's assigned to the block.
     */
    public static void draw2DSquircle(World world, int x, int y, int z, Block block, int radius, boolean hollow, double checkCounter, int blockMeta) {
        for(float xr = -radius; xr <= radius; xr += checkCounter){
            float zrHyperCubed = (float)hyperCube(radius) - (float)hyperCube(xr);
            if(zrHyperCubed < 0) continue;
            int zl = Math.round((float) nthrt(zrHyperCubed, 4));

            int xPlace = x + Math.round(xr);
            int zPlace1 = z + zl;
            int zPlace2 = z - zl;

            if(zPlace1 > z + radius) zPlace1 = z + radius;
            if(zPlace2 < z - radius) zPlace2 = z - radius;

            if(world.getBlock(xPlace, y, zPlace1) != block && y >= 0) world.setBlock(xPlace, y, zPlace1, block, blockMeta, 3);
            if(world.getBlock(xPlace, y, zPlace2) != block && y >= 0) world.setBlock(xPlace, y, zPlace2, block, blockMeta, 3);

            if(!hollow)
                for(int zf = z - zl; zf <= z + zl; zf++)
                    if(world.getBlock(xPlace, y, zf) != block && y >= 0) world.setBlock(xPlace, y, zf, block, blockMeta, 3);
        }
    }

    /**
     * @param world {@link World} where the squircle is going to be made.
     * @param x the x coordinate where of the center of the squircle.
     * @param y the y coordinate where of the center of the squircle.
     * @param z the z coordinate where of the center of the squircle.
     * @param block the {@link Block} which makes up the squircle.
     * @param radius the radius of the squircle.
     * @param hollow true if the squircle is hollow, false if it's filled.
     * @param checkCounter the amount it adds each time it is calculated.
     */
    public static void draw3DSquircle(World world, int x, int y, int z, Block block, int radius, boolean hollow, double checkCounter){
        draw3DSquircle(world, x, y, z, block, radius, hollow, checkCounter, 0);
    }

    /**
     * @param world {@link World} where the squircle is going to be made.
     * @param x the x coordinate where of the center of the squircle.
     * @param y the y coordinate where of the center of the squircle.
     * @param z the z coordinate where of the center of the squircle.
     * @param block the {@link Block} which makes up the squircle.
     * @param radius the radius of the squircle.
     * @param hollow true if the squircle is hollow, false if it's filled.
     * @param checkCounter the amount it adds each time it is calculated.
     * @param blockMeta the MetaData that's assigned to the block.
     */
    public static void draw3DSquircle(World world, int x, int y, int z, Block block, int radius, boolean hollow, double checkCounter, int blockMeta) {
        for(float xr = -radius; xr <= radius; xr += checkCounter){
            for(float zr = -radius; zr <= radius; zr += checkCounter){
                float yrHyperCubed = (float) hyperCube(radius) - ((float) hyperCube(xr) + (float) hyperCube(zr));
                if (yrHyperCubed < 0) continue;
                int yl = Math.round((float) nthrt(yrHyperCubed, 4));

                int xPlace = x + Math.round(xr);
                int yPlace1 = y + yl;
                int yPlace2 = y - yl;
                int zPlace = z + Math.round(zr);

                if(yPlace1 > y + radius) yPlace1 = y + radius;
                if(yPlace2 < y - radius) yPlace2 = y - radius;

                if(world.getBlock(xPlace, yPlace1, zPlace) != block && yPlace1 >= 0) world.setBlock(xPlace, yPlace1, zPlace, block, blockMeta, 3);
                if(world.getBlock(xPlace, yPlace2, zPlace) != block && yPlace2 >= 0) world.setBlock(xPlace, yPlace2, zPlace, block, blockMeta, 3);

                if(!hollow) {
                    for (int yf = y - yl; yf <= y + yl; yf++) {
                        xPlace = x + Math.round(xr);
                        zPlace = z + Math.round(zr);
                        if(world.getBlock(xPlace, yf, zPlace) != block && yPlace2 >= 0) world.setBlock(xPlace, yf, zPlace, block, blockMeta, 3);
                    }
                }
            }
        }
    }

    private static double hyperCube(double d) {
        return d * d * d * d;
    }

    private static double nthrt(double d, double n) {
        if(n == 2) return Math.sqrt(d);
        else if(n == 3) return Math.cbrt(d);
        else return Math.pow(d, 1.0D / n);
    }

    public static void draw2DEllipsoid(World world, int x, int y, int z, Block block, int radius, boolean hollow, double checkCounter, int blockMeta, double a, double b) {
        for(float xr = -radius; xr <= radius; xr += checkCounter) {
            float zrPowered = (float) Math.pow(radius, a) - (float) Math.pow(xr, a);
            if (zrPowered < 0) continue;
            double zl = nthrt(zrPowered, b);

            double xPlace = x + xr;
            double zPlace1 = z + zl;
            double zPlace2 = z - zl;

            if(zPlace1 > z + radius) zPlace1 = z + radius;
            if(zPlace2 < z - radius) zPlace2 = z - radius;

            if (world.getBlock((int)Math.round(xPlace), y, (int)Math.round(zPlace1)) != block && y >= 0) world.setBlock((int)Math.round(xPlace), y, (int)Math.round(zPlace1), block, blockMeta, 3);
            if (world.getBlock((int)Math.round(xPlace), y, (int)Math.round(zPlace2)) != block && y >= 0) world.setBlock((int)Math.round(xPlace), y, (int)Math.round(zPlace2), block, blockMeta, 3);

            if (!hollow)
                for (double zf = z - zl; zf <= z + zl; zf++)
                    if (world.getBlock((int) Math.round(xPlace), y, (int)Math.round(zf)) != block && y >= 0)world.setBlock((int) Math.round(xPlace), y, (int)Math.round(zf), block, blockMeta, 3);
        }
    }
}
