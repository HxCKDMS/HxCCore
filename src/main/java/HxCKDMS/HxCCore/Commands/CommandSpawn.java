package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Utils.Teleporter;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;

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
            EntityPlayerMP player = args.length == 2 ? CommandBase.getPlayer(sender, args[1]) : (EntityPlayerMP)sender;
            if(player.dimension != 0) {
                Teleporter.transferPlayerToDimension(player, 0, player.mcServer.getConfigurationManager(), player.worldObj.getSpawnPoint().posX, player.worldObj.getSpawnPoint().posY, player.worldObj.getSpawnPoint().posZ);
                player.addChatMessage(new ChatComponentText("\u00A76You have been transported to spawn."));
            } else {
                player.playerNetServerHandler.setPlayerLocation(player.worldObj.getSpawnPoint().posX, player.worldObj.getSpawnPoint().posY, player.worldObj.getSpawnPoint().posZ, player.rotationYaw, player.rotationPitch);
                player.addChatMessage(new ChatComponentText("\u00A76You have been transported to spawn."));
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        if(args.length == 2){
            return net.minecraft.command.CommandBase.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
        }
        return null;
    }
}
