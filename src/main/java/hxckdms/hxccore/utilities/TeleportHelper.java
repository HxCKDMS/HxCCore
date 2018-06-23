package hxckdms.hxccore.utilities;

import hxckdms.hxccore.api.event.PlayerTeleportEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ITeleporter;

public class TeleportHelper {
    public static void teleportEntityToDimension(Entity entity, BlockPos pos, int dimension) {
        teleportEntityToDimension(entity, pos.getX(), pos.getY(), pos.getZ(), dimension);
    }

    public static void teleportEntityToDimension(Entity entity, double x, double y, double z, int dimension) {
        if (entity.dimension == dimension) {
            checkAndTeleport(entity, x, y, z, dimension);
            return;
        }
        entity.changeDimension(dimension, new HxCTeleporter(x, y, z));
    }

    private static void checkAndTeleport(Entity entity, double x, double y, double z, int dimension) {
        if (entity instanceof EntityPlayerMP) {
            if (MinecraftForge.EVENT_BUS.post(new PlayerTeleportEvent((EntityPlayer) entity, x, y, z, dimension))) return;
            ((EntityPlayerMP) entity).connection.setPlayerLocation(x, y, z, ((EntityPlayerMP) entity).rotationYaw, ((EntityPlayerMP) entity).rotationPitch);
        } else entity.setPositionAndUpdate(x, y, z);
    }

    private static class HxCTeleporter implements ITeleporter {
        private final double x;
        private final double y;
        private final double z;

        private HxCTeleporter(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        @Override
        public void placeEntity(World world, Entity entity, float yaw) {
            if (entity instanceof EntityPlayerMP) {
                ((EntityPlayerMP) entity).connection.setPlayerLocation(x, y, z, yaw, entity.rotationPitch);
            } else {
                entity.moveToBlockPosAndAngles(new BlockPos(x, y, z), yaw, entity.rotationPitch);
            }
        }
    }
}
