package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Handlers.NBTFileIO;
import HxCKDMS.HxCCore.HxCCore;
import HxCKDMS.HxCCore.Utils.Teleporter;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;

import java.io.File;
import java.util.List;


public class CommandHome implements ISubCommand {
    public static CommandHome instance = new CommandHome();

    @Override
    public String getCommandName() {
        return "home";
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] args) {
        if(sender instanceof EntityPlayerMP){
            EntityPlayerMP player = (EntityPlayerMP) sender;

            File PermissionsData = new File(HxCCore.HxCCoreDir, "HxC-Permissions.dat");
            NBTTagCompound Permissions = NBTFileIO.getNbtTagCompound(PermissionsData, "Permissions");
            int SenderPermLevel = Permissions.getInteger(player.getDisplayName());
            boolean isopped = HxCCore.server.getConfigurationManager().func_152596_g(player.getGameProfile());
            if (SenderPermLevel >= 0 || isopped) {
                String UUID = player.getUniqueID().toString();
                File CustomPlayerData = new File(HxCCore.HxCCoreDir, "HxC-" + UUID + ".dat");

                String hName = args.length == 1 ? "default" : args[1];
                NBTTagCompound homeDir = NBTFileIO.getNbtTagCompound(CustomPlayerData, "home");
                if(!homeDir.hasKey(hName)){
                    throw new WrongUsageException("The home named: '" + hName + "' does not exist.");
                }
                NBTTagCompound home = homeDir.getCompoundTag(hName);
                if(player.dimension != home.getInteger("dim")) {
                    Teleporter.transferPlayerToDimension(player, home.getInteger("dim"), player.mcServer.getConfigurationManager(), home.getInteger("x"), home.getInteger("y"), home.getInteger("z"));
                    player.addChatMessage(new ChatComponentText("You have returned to " + hName + "."));
                } else {
                    player.playerNetServerHandler.setPlayerLocation(home.getInteger("x"), home.getInteger("y"), home.getInteger("z"), player.rotationYaw, player.rotationPitch);
                    player.addChatMessage(new ChatComponentText("You have returned to " + hName + "."));
                }
            } else {
                sender.addChatMessage(new ChatComponentText("\u00A74You do not have permission to use this command."));
            }
        }else{
            sender.addChatMessage(new ChatComponentText("\u00A74This command can only be executed by a player."));
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        return null;
    }
}
