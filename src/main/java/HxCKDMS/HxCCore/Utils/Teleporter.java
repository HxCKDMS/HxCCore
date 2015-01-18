package HxCKDMS.HxCCore.Utils;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.network.play.server.S1DPacketEntityEffect;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.world.WorldServer;

public class Teleporter {

    private static void transferPlayerToWorld(EntityPlayerMP player, WorldServer oldWorldServer, WorldServer newWorldServer, int x, int y, int z){
        oldWorldServer.theProfiler.startSection("placing");

        if(player.isEntityAlive()){
            player.playerNetServerHandler.setPlayerLocation(x, y, z, player.rotationYaw, player.rotationPitch);
            newWorldServer.spawnEntityInWorld(player);
            newWorldServer.updateEntityWithOptionalForce(player, false);
        }

        oldWorldServer.theProfiler.endSection();

        player.setWorld(newWorldServer);
    }

    public static void transferPlayerToDimension(EntityPlayerMP player, int dimension, ServerConfigurationManager configurationManager, int x, int y, int z){
        int startDim = player.dimension;
        WorldServer worldServer_old = player.mcServer.worldServerForDimension(player.dimension);
        player.dimension = dimension;
        WorldServer worldServer_new = player.mcServer.worldServerForDimension(player.dimension);
        player.playerNetServerHandler.sendPacket(new S07PacketRespawn(player.dimension, player.worldObj.difficultySetting, player.worldObj.getWorldInfo().getTerrainType(), player.theItemInWorldManager.getGameType()));
        worldServer_old.removePlayerEntityDangerously(player);
        player.isDead = false;
        transferPlayerToWorld(player, worldServer_old, worldServer_new, x, y, z);
        configurationManager.func_72375_a(player, worldServer_old);
        player.playerNetServerHandler.setPlayerLocation(x, y, z, player.rotationYaw, player.rotationPitch);
        player.theItemInWorldManager.setWorld(worldServer_new);
        configurationManager.updateTimeAndWeatherForPlayer(player, worldServer_new);
        configurationManager.syncPlayerInventory(player);

        for (Object o : player.getActivePotionEffects()) {
            PotionEffect potionEffect = (PotionEffect) o;
            player.playerNetServerHandler.sendPacket(new S1DPacketEntityEffect(player.getEntityId(), potionEffect));
        }
        FMLCommonHandler.instance().firePlayerChangedDimensionEvent(player, startDim, dimension);
    }
}
