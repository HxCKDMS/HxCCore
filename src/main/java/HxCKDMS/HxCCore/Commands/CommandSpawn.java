package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Utils.Teleporter;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.List;

public class CommandSpawn implements ISubCommand {
    public static CommandSpawn instance = new CommandSpawn();

    @Override
    public String getCommandName() {
        return "spawn";
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] args) throws PlayerNotFoundException {
        if(sender instanceof EntityPlayerMP){
            EntityPlayerMP player = args.length == 2 ? CommandBase.getPlayer(sender, args[1]) : (EntityPlayerMP)sender;

            if(player.dimension != 0)
                Teleporter.transferPlayerToDimension(player, 0, player.mcServer.getConfigurationManager(), player.worldObj.getSpawnPoint().getX(), player.worldObj.getSpawnPoint().getY(), player.worldObj.getSpawnPoint().getZ());
            else
                player.playerNetServerHandler.setPlayerLocation(player.worldObj.getSpawnPoint().getX(), player.worldObj.getSpawnPoint().getY(), player.worldObj.getSpawnPoint().getZ(), player.rotationYaw, player.rotationPitch);
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        return null;
    }
}
