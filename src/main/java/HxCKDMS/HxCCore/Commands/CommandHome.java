package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Configs.Configurations;
import HxCKDMS.HxCCore.Handlers.NBTFileIO;
import HxCKDMS.HxCCore.Handlers.PermissionsHandler;
import HxCKDMS.HxCCore.HxCCore;
import HxCKDMS.HxCCore.api.ISubCommand;
import HxCKDMS.HxCCore.api.Utils.Teleporter;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


public class CommandHome implements ISubCommand {
    public static CommandHome instance = new CommandHome();

    @Override
    public String getCommandName() {
        return "home";
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] args) throws WrongUsageException {
        if (sender instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) sender;
            boolean CanSend = PermissionsHandler.canUseCommand(Configurations.commands.get("Home"), player);
            if (CanSend) {
                int oldx = (int)player.posX, oldy = (int)player.posY, oldz = (int)player.posZ, olddim = player.dimension;
                String UUID = player.getUniqueID().toString();
                File CustomPlayerData = new File(HxCCore.HxCCoreDir, "HxC-" + UUID + ".dat");

                String hName = args.length == 1 ? "default" : args[1];
                NBTTagCompound homeDir = NBTFileIO.getNbtTagCompound(CustomPlayerData, "home");
                if(homeDir.getKeySet().isEmpty()) throw new WrongUsageException(StatCollector.translateToLocal("commands.exception.noHomes"));
                if(!homeDir.hasKey(hName)){
                    throw new WrongUsageException("\u00a74\u00a7oThe home named: '" + hName + "' does not exist.");
                }
                NBTTagCompound home = homeDir.getCompoundTag(hName);
                if(player.dimension != home.getInteger("dim")) {
                    Teleporter.transferPlayerToDimension(player, home.getInteger("dim"), home.getInteger("x"), home.getInteger("y"), home.getInteger("z"));
                    player.addChatMessage(new ChatComponentText("You have returned to " + hName + "."));
                } else {
                    player.playerNetServerHandler.setPlayerLocation(home.getInteger("x"), home.getInteger("y"), home.getInteger("z"), player.rotationYaw, player.rotationPitch);
                    player.addChatMessage(new ChatComponentText("You have returned to " + hName + "."));
                }
                NBTFileIO.setIntArray(CustomPlayerData, "back", new int[]{oldx, oldy, oldz, olddim});
            } else throw new WrongUsageException(StatCollector.translateToLocal("commands.exception.permission"));
        } else throw new WrongUsageException(StatCollector.translateToLocal("commands.exception.playersonly"));
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        if (args.length == 2) {
            EntityPlayerMP player = (EntityPlayerMP) sender;
            String UUID = player.getUniqueID().toString();
            File CustomPlayerData = new File(HxCCore.HxCCoreDir, "HxC-" + UUID + ".dat");
            NBTTagCompound home = NBTFileIO.getNbtTagCompound(CustomPlayerData, "home");
            return new LinkedList<>((Set<String>) home.getKeySet());
        }
        return null;
    }
}
