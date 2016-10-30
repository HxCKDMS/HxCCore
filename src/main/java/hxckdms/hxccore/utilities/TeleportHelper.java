package hxckdms.hxccore.utilities;

import hxckdms.hxccore.api.event.PlayerTeleportEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;

import static hxckdms.hxccore.libraries.GlobalVariables.server;

public class TeleportHelper {

    public static void teleportEntityToDimension(Entity entity, BlockPos pos, int dimension) {
        teleportEntityToDimension(entity, pos.getX(), pos.getY(), pos.getZ(), dimension);
    }

    public static void teleportEntityToDimension(Entity entity, double x, double y, double z, int dimension) {
        if (entity.dimension == dimension) {
            checkAndTeleport(entity, x, y, z, dimension);
            return;
        }

        if (entity instanceof EntityPlayerMP) server.getPlayerList().transferPlayerToDimension((EntityPlayerMP) entity, dimension, new HxCTeleporter((WorldServer) server.getEntityWorld()));
        else teleportEntity(entity, dimension);

        if (entity.dimension == dimension) checkAndTeleport(entity, x, y, z, dimension);
    }

    private static void checkAndTeleport(Entity entity, double x, double y, double z, int dimension) {
        if (entity instanceof EntityPlayerMP) {
            if (MinecraftForge.EVENT_BUS.post(new PlayerTeleportEvent((EntityPlayer) entity, x, y, z, dimension))) return;
            ((EntityPlayerMP) entity).connection.setPlayerLocation(x, y, z, ((EntityPlayerMP) entity).rotationYaw, ((EntityPlayerMP) entity).rotationPitch);
        } else entity.setPositionAndUpdate(x, y, z);
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
                newEntity.copyDataFromOld(entity);
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
        public void placeInPortal(Entity entityIn, float rotationYaw) {}

        @Override
        public boolean placeInExistingPortal(Entity entityIn, float rotationYaw) {
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
