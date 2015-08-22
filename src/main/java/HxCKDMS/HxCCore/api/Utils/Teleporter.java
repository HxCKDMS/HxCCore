package HxCKDMS.HxCCore.api.Utils;

import HxCKDMS.HxCCore.Handlers.NBTFileIO;
import HxCKDMS.HxCCore.HxCCore;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.network.play.server.S1DPacketEntityEffect;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.WorldServer;

import java.io.File;

public class Teleporter {

    private static void transferPlayerToWorld(EntityPlayerMP player, WorldServer oldWorldServer, WorldServer newWorldServer, int x, int y, int z){
        oldWorldServer.theProfiler.startSection("placing");
        int oldx = 0, oldy = 0, oldz = 0;
        if(player.isEntityAlive()){
            oldx = (int)player.posX; oldy = (int)player.posY; oldz = (int)player.posZ;
            player.playerNetServerHandler.setPlayerLocation(x, y, z, player.rotationYaw, player.rotationPitch);
            newWorldServer.spawnEntityInWorld(player);
            newWorldServer.updateEntityWithOptionalForce(player, false);
        }

        oldWorldServer.theProfiler.endSection();

        player.setWorld(newWorldServer);

        onPlayerTeleport(player, x, y, z, oldx, oldy, oldz, oldWorldServer.provider.dimensionId, newWorldServer.provider.dimensionId);
    }

    //TODO: make work on all entities.
    public static void transferPlayerToDimension(EntityPlayerMP player, int dimension, int x, int y, int z){
        int startDim = player.dimension, oldx, oldy, oldz;
        WorldServer worldServer_old = player.mcServer.worldServerForDimension(player.dimension);
        player.dimension = dimension;
        oldx = (int)player.posX; oldy = (int)player.posY; oldz = (int)player.posZ;
        WorldServer worldServer_new = player.mcServer.worldServerForDimension(player.dimension);
        player.playerNetServerHandler.sendPacket(new S07PacketRespawn(player.dimension, player.worldObj.difficultySetting, player.worldObj.getWorldInfo().getTerrainType(), player.theItemInWorldManager.getGameType()));
        worldServer_old.removePlayerEntityDangerously(player);
        player.isDead = false;
        transferPlayerToWorld(player, worldServer_old, worldServer_new, x, y, z);
        player.mcServer.getConfigurationManager().func_72375_a(player, worldServer_old);
        player.playerNetServerHandler.setPlayerLocation(x, y, z, player.rotationYaw, player.rotationPitch);
        player.theItemInWorldManager.setWorld(worldServer_new);
        player.mcServer.getConfigurationManager().updateTimeAndWeatherForPlayer(player, worldServer_new);
        player.mcServer.getConfigurationManager().syncPlayerInventory(player);

        for (Object o : player.getActivePotionEffects()) {
            PotionEffect potionEffect = (PotionEffect) o;
            player.playerNetServerHandler.sendPacket(new S1DPacketEntityEffect(player.getEntityId(), potionEffect));
        }
        FMLCommonHandler.instance().firePlayerChangedDimensionEvent(player, startDim, dimension);

        onPlayerTeleport(player, x, y, z, oldx, oldy, oldz, startDim, dimension);
    }

    public static void onPlayerTeleport(EntityPlayer player, int x, int y, int z, int oldx, int oldy, int oldz, int olddim, int newdim) {
        String UUID = player.getUniqueID().toString();
        File CustomPlayerData = new File(HxCCore.HxCCoreDir, "HxC-" + UUID + ".dat");
        if(!CustomPlayerData.exists()) return;

        NBTFileIO.setIntArray(CustomPlayerData, "back", new int[]{oldx, oldy, oldz, olddim});
    }
}