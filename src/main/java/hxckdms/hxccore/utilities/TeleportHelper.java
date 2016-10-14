package hxckdms.hxccore.utilities;

import net.minecraft.command.CommandException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

import static hxckdms.hxccore.libraries.GlobalVariables.server;

public class TeleportHelper {

    public static void teleportEntityToDimension(Entity entity, BlockPos pos, int dimension) throws CommandException {
        if (entity.dimension == dimension) {
            if (entity instanceof EntityPlayerMP) ((EntityPlayerMP) entity).connection.setPlayerLocation(pos.getX(), pos.getY(), pos.getZ(), ((EntityPlayerMP) entity).rotationYaw, ((EntityPlayerMP) entity).rotationPitch);
            else entity.setPositionAndUpdate(pos.getX(), pos.getY(), pos.getZ());
            return;
        }

        if (entity instanceof EntityPlayerMP) server.getPlayerList().transferPlayerToDimension((EntityPlayerMP) entity, dimension, new HxCTeleporter((WorldServer) server.getEntityWorld()));
        else teleportEntity(entity, dimension);

        if (entity.dimension == dimension) {
            if (entity instanceof EntityPlayerMP) ((EntityPlayerMP) entity).connection.setPlayerLocation(pos.getX(), pos.getY(), pos.getZ(), ((EntityPlayerMP) entity).rotationYaw, ((EntityPlayerMP) entity).rotationPitch);
            else entity.setPositionAndUpdate(pos.getX(), pos.getY(), pos.getZ());
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
