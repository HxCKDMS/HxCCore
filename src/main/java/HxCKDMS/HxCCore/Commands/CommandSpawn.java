package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Utils.Teleporter;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.List;

public class CommandSpawn implements ISubCommand {
    public static CommandSpawn instance = new CommandSpawn();

    @Override
    public String getCommandName() {
        return "spawn";
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] args) {
        if(sender instanceof EntityPlayerMP){
            EntityPlayerMP player = (EntityPlayerMP)sender;

            if(player.dimension != 0)
                Teleporter.transferPlayerToDimension(player, 0, player.mcServer.getConfigurationManager(), player.worldObj.getSpawnPoint().posX, player.worldObj.getSpawnPoint().posY, player.worldObj.getSpawnPoint().posZ);
            else
                player.playerNetServerHandler.setPlayerLocation(player.worldObj.getSpawnPoint().posX, player.worldObj.getSpawnPoint().posY, player.worldObj.getSpawnPoint().posZ, player.rotationYaw, player.rotationPitch);
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        return null;
    }
}
