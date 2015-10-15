package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Configs.CommandsConfig;
import HxCKDMS.HxCCore.Handlers.CommandsHandler;
import HxCKDMS.HxCCore.Handlers.NBTFileIO;
import HxCKDMS.HxCCore.Handlers.PermissionsHandler;
import HxCKDMS.HxCCore.HxCCore;
import HxCKDMS.HxCCore.api.Command.HxCCommand;
import HxCKDMS.HxCCore.api.Utils.Teleporter;
import HxCKDMS.HxCCore.api.Command.ISubCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;

import java.io.File;
import java.util.List;

@HxCCommand(defaultPermission = 0, mainCommand = CommandsHandler.class, isEnabled = true)
public class CommandSpawn implements ISubCommand {
    public static CommandSpawn instance = new CommandSpawn();

    @Override
    public String getCommandName() {
        return "Spawn";
    }

    @Override
    public int[] getCommandRequiredParams() {
        return new int[]{0, 1, -1};
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] args, boolean isPlayer) throws PlayerNotFoundException, WrongUsageException {
        if (isPlayer) {
            EntityPlayerMP player = (EntityPlayerMP) sender;
            boolean CanSend = PermissionsHandler.canUseCommand(Integer.valueOf(CommandsConfig.CommandPermissions.get("Spawn")), player);
            if (CanSend) {
                EntityPlayerMP target = args.length == 2 ? CommandsHandler.getPlayer(sender, args[1]) : (EntityPlayerMP) sender;
                int oldx = (int)target.posX, oldy = (int)target.posY, oldz = (int)target.posZ, olddim = target.dimension;
                if (target.dimension != 0) {
                    Teleporter.transferPlayerToDimension(target, 0, player.worldObj.getSpawnPoint().posX, player.worldObj.getSpawnPoint().posY, player.worldObj.getSpawnPoint().posZ);
                    target.addChatMessage(new ChatComponentText("\u00A76You have been transported to spawn."));
                } else {
                    target.playerNetServerHandler.setPlayerLocation(player.worldObj.getSpawnPoint().posX, player.worldObj.getSpawnPoint().posY, player.worldObj.getSpawnPoint().posZ, player.rotationYaw, player.rotationPitch);
                    target.addChatMessage(new ChatComponentText("\u00A76You have been transported to spawn."));
                }
                String UUID = target.getUniqueID().toString();
                File CustomPlayerData = new File(HxCCore.HxCCoreDir, "HxC-" + UUID + ".dat");
                NBTFileIO.setIntArray(CustomPlayerData, "back", new int[]{oldx, oldy, oldz, olddim});
            } else throw new WrongUsageException(StatCollector.translateToLocal("commands.exception.permission"));
        } else throw new WrongUsageException(StatCollector.translateToLocal("commands.exception.playersonly"));
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
