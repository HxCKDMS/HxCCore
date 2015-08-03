package HxCKDMS.HxCCore.api.Utils;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.network.play.server.S1DPacketEntityEffect;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class Teleporter {

    private static void transferPlayerToWorld(EntityPlayerMP player, WorldServer oldWorldServer, WorldServer newWorldServer, BlockPos pos){
        oldWorldServer.theProfiler.startSection("placing");

        if(player.isEntityAlive()){
            player.playerNetServerHandler.setPlayerLocation(pos.getX(), pos.getY(), pos.getZ(), player.rotationYaw, player.rotationPitch);
            newWorldServer.spawnEntityInWorld(player);
            newWorldServer.updateEntityWithOptionalForce(player, false);
        }

        oldWorldServer.theProfiler.endSection();

        player.setWorld(newWorldServer);
    }

    //TODO: make work on all entities.
    public static void transferPlayerToDimension(EntityPlayerMP player, int dimension, BlockPos pos){
        int startDim = player.dimension;
        WorldServer worldServer_old = player.mcServer.worldServerForDimension(player.dimension);
        player.dimension = dimension;
        WorldServer worldServer_new = player.mcServer.worldServerForDimension(player.dimension);
        player.playerNetServerHandler.sendPacket(new S07PacketRespawn(player.dimension, player.worldObj.getDifficulty(), player.worldObj.getWorldInfo().getTerrainType(), player.theItemInWorldManager.getGameType()));
        worldServer_old.removePlayerEntityDangerously(player);
        player.isDead = false;
        transferPlayerToWorld(player, worldServer_old, worldServer_new, pos);
        player.mcServer.getConfigurationManager().func_72375_a(player, worldServer_old);
        player.playerNetServerHandler.setPlayerLocation(pos.getX(), pos.getY(), pos.getZ(), player.rotationYaw, player.rotationPitch);
        player.theItemInWorldManager.setWorld(worldServer_new);
        player.mcServer.getConfigurationManager().updateTimeAndWeatherForPlayer(player, worldServer_new);
        player.mcServer.getConfigurationManager().syncPlayerInventory(player);

        for (Object o : player.getActivePotionEffects()) {
            PotionEffect potionEffect = (PotionEffect) o;
            player.playerNetServerHandler.sendPacket(new S1DPacketEntityEffect(player.getEntityId(), potionEffect));
        }
        FMLCommonHandler.instance().firePlayerChangedDimensionEvent(player, startDim, dimension);
    }
}