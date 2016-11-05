package hxckdms.hxccore.utilities;

import hxckdms.hxccore.api.event.PlayerTeleportEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;

import static hxckdms.hxccore.libraries.GlobalVariables.server;

public class TeleportHelper {
    public static void teleportEntityToDimension(Entity entity, ChunkCoordinates chunkCoordinates, int dimension) {
        teleportEntityToDimension(entity, chunkCoordinates.posX, chunkCoordinates.posY, chunkCoordinates.posZ, dimension);
    }

    public static void teleportEntityToDimension(Entity entity, double x, double y, double z, int dimension) {
        if (entity.dimension == dimension) {
            checkAndTeleport(entity, x, y, z, dimension);
            return;
        }

        if (entity instanceof EntityPlayerMP) server.getConfigurationManager().transferPlayerToDimension((EntityPlayerMP) entity, dimension, new HxCTeleporter((WorldServer) server.getEntityWorld()));
        else teleportEntity(entity, dimension);

        if (entity.dimension == dimension) checkAndTeleport(entity, x, y, z, dimension);
    }

    private static void checkAndTeleport(Entity entity, double x, double y, double z, int dimension) {
        if (entity instanceof EntityPlayerMP) {
            if (MinecraftForge.EVENT_BUS.post(new PlayerTeleportEvent((EntityPlayer) entity, x, y, z, dimension))) return;
            ((EntityPlayerMP) entity).playerNetServerHandler.setPlayerLocation(x, y, z, ((EntityPlayerMP) entity).rotationYaw, ((EntityPlayerMP) entity).rotationPitch);
        } else {
            entity.setPosition(x, y, z);
            entity.worldObj.updateEntityWithOptionalForce(entity, false);
        }
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
