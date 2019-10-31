package hxckdms.hxccore.utilities;

import hxckdms.hxccore.api.event.PlayerTeleportEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;

import static hxckdms.hxccore.libraries.GlobalVariables.server;

public class TeleportHelper {
    public static boolean teleportEntityToDimension(Entity entity, ChunkCoordinates chunkCoordinates, int dimension) {
        return teleportEntityToDimension(entity, chunkCoordinates.posX, chunkCoordinates.posY, chunkCoordinates.posZ, dimension);
    }

    public static boolean teleportEntityToDimension(Entity entity, double x, double y, double z, int dimension) {
        if (entity.dimension == dimension) {
            return teleportSafely(entity, x, y, z, dimension);
        }

        if (entity instanceof EntityPlayerMP) server.getConfigurationManager().transferPlayerToDimension((EntityPlayerMP) entity, dimension, new HxCTeleporter((WorldServer) server.getEntityWorld()));
        else teleportEntity(entity, dimension);

        if (entity.dimension == dimension) return teleportSafely(entity, x, y, z, dimension);
        return true;
    }

    public static boolean teleportSafely(Entity entity, double x, double y, double z, int dim) {
        int[] coords = findSafe(x, y, z, dim);
        if (coords.length == 0) return false;
        if (entity instanceof EntityPlayerMP) {
            if (MinecraftForge.EVENT_BUS.post(new PlayerTeleportEvent((EntityPlayer) entity, coords[0], coords[1], coords[2], dim))) return false;
            ((EntityPlayerMP) entity).playerNetServerHandler.setPlayerLocation(coords[0], coords[1], coords[2], ((EntityPlayerMP) entity).rotationYaw, ((EntityPlayerMP) entity).rotationPitch);
            return true;
        } else {
            entity.setPosition(x, y, z);
            entity.worldObj.updateEntityWithOptionalForce(entity, false);
            return true;
        }
    }

    private static boolean isSafe(World world, int x, int y, int z) {
        return world.isAirBlock(x, y, z) && world.isAirBlock(x, y + 1, z);
    }

    public static int[] findSafe(double x, double y, double z, int dim) {
        int[] coords = new int[0];
        World world = server.worldServerForDimension(dim);
        if (!isSafe(world, (int) x, (int) y, (int) z)) {
            for (int i = 1; i <= 5; i++) {
                if (isSafe(world, (int) x, (int) y + i, (int) z)) {
                    return new int[]{(int) x, (int) y + i, (int) z};
                }
            }
            byte counter = 2;
            while (coords.length == 0) {
                coords = getAirWithinAABB(world, getAreaBoundingBox((int) x, (int) y, (int) z, counter++));
                if (coords.length == 0 && counter > 9) return new int[0];
            }
        } else return new int[]{(int) x, (int) y, (int) z};
        return coords;
    }

    public static int[] findSafe(Entity entity, double x, double y, double z) {
        return findSafe(x, y, z, entity.dimension);
    }

    private static int[] getAirWithinAABB(World world, AxisAlignedBB box) {
        for(int x = (int) box.minX; x <= box.maxX; x++) {
            for(int y = (int) box.minY; y <= box.maxY; y++) {
                for(int z = (int) box.minZ; z <= box.maxZ; z++) {
                    if(isSafe(world, x, y, z))
                        return new int[]{x, y, z};
                }
            }
        }
        return new int[0];
    }

    private static AxisAlignedBB getAreaBoundingBox(int x, int y, int z, int radius) {
        return AxisAlignedBB.getBoundingBox(x - radius, y - radius, z - radius,
                x + 0.99 + radius, y + 0.99 + radius, z + 0.99 + radius);
    }

    private static void teleportEntity(Entity entity, int dimensionId) {
        if (!entity.worldObj.isRemote && !entity.isDead) {
            entity.worldObj.theProfiler.startSection("changeDimension");
            int j = entity.dimension;
            WorldServer worldserver = server.worldServerForDimension(j);
            WorldServer worldserver1 = server.worldServerForDimension(dimensionId);

            Entity newEntity = EntityList.createEntityByName(EntityList.getEntityString(entity), worldserver1);
            if (newEntity == null) return;

            entity.dimension = dimensionId;


            try {
                newEntity.copyDataFrom(entity, true);
            } catch (Exception e) {
                e.printStackTrace();
            }

            entity.worldObj.removeEntity(entity);

            newEntity.forceSpawn = true;
            worldserver1.spawnEntityInWorld(newEntity);
            worldserver1.updateEntityWithOptionalForce(newEntity, true);

            entity.isDead = true;
            entity.worldObj.theProfiler.endSection();
            worldserver.resetUpdateEntityTick();
            worldserver1.resetUpdateEntityTick();
            entity.worldObj.theProfiler.endSection();
        }
    }

    private static class HxCTeleporter extends Teleporter {
        HxCTeleporter(WorldServer worldIn) {
            super(worldIn);
        }

        @Override
        public void placeInPortal(Entity entityIn, double posX, double posY, double posZ, float yaw) {}

        @Override
        public boolean placeInExistingPortal(Entity entityIn, double posX, double posY, double posZ, float yaw) {
            return true;
        }

        @Override
        public boolean makePortal(Entity entityIn) {
            return true;
        }

        @Override
        public void removeStalePortalLocations(long worldTime) {}
    }
}
