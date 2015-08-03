package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Configs.Configurations;
import HxCKDMS.HxCCore.Handlers.PermissionsHandler;
import HxCKDMS.HxCCore.api.Utils.Teleporter;
import HxCKDMS.HxCCore.api.ISubCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;

import java.util.List;

public class CommandSpawn implements ISubCommand {
    public static CommandSpawn instance = new CommandSpawn();

    @Override
    public String getName() {
        return "spawn";
    }

    @Override
    public void execute(ICommandSender sender, String[] args) throws PlayerNotFoundException {
        if(sender instanceof EntityPlayerMP){
            EntityPlayerMP player = (EntityPlayerMP) sender;
            boolean CanSend = PermissionsHandler.canUseCommand(Configurations.commands.get("Spawn"), player);
            if (CanSend) {
                EntityPlayerMP target = args.length == 2 ? CommandBase.getPlayer(sender, args[1]) : (EntityPlayerMP) sender;
                if (player.dimension != 0) {
                    Teleporter.transferPlayerToDimension(player, 0, player.worldObj.getSpawnPoint());
                    player.addChatMessage(new ChatComponentText("\u00A76You have been transported to spawn."));
                } else {
                    player.playerNetServerHandler.setPlayerLocation(player.worldObj.getSpawnPoint().getX(), player.worldObj.getSpawnPoint().getY(), player.worldObj.getSpawnPoint().getZ(), player.rotationYaw, player.rotationPitch);
                    player.addChatMessage(new ChatComponentText("\u00A76You have been transported to spawn."));
                }
            } else sender.addChatMessage(new ChatComponentText("\u00A74You do not have permission to use this command."));
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
