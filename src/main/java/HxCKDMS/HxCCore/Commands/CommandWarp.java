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


public class CommandWarp implements ISubCommand {
    public static CommandWarp instance = new CommandWarp();

    @Override
    public String getCommandName() {
        return "warp";
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] args) throws WrongUsageException {
        if(sender instanceof EntityPlayerMP){
            EntityPlayerMP player = (EntityPlayerMP)sender;
            File PermissionsData = new File(HxCCore.HxCCoreDir, "HxC-Permissions.dat");
            NBTTagCompound Permissions = NBTFileIO.getNbtTagCompound(PermissionsData, "Permissions");
            int SenderPermLevel = Permissions.getInteger(player.getName());
            boolean isopped = HxCCore.server.getConfigurationManager().canSendCommands(player.getGameProfile());
            if (SenderPermLevel >= 0 || isopped) {
                File HxCWorldData = new File(HxCCore.HxCCoreDir, "HxCWorld.dat");

                String wName = args.length == 1 ? "default" : args[1];
                NBTTagCompound warpDir = NBTFileIO.getNbtTagCompound(HxCWorldData, "warp");
                if(!warpDir.hasKey(wName)){
                    throw new WrongUsageException("the warp named: '" + wName + "' does not exist.");
                }
                NBTTagCompound warp = warpDir.getCompoundTag(wName);
                if(player.dimension != warp.getInteger("dim")) {
                    Teleporter.transferPlayerToDimension(player, warp.getInteger("dim"), player.mcServer.getConfigurationManager(), warp.getInteger("x"), warp.getInteger("y"), warp.getInteger("z"));
                    player.addChatMessage(new ChatComponentText("You have teleported to " + wName + "."));
                } else {
                    player.playerNetServerHandler.setPlayerLocation(warp.getInteger("x"), warp.getInteger("y"), warp.getInteger("z"), player.rotationYaw, player.rotationPitch);
                    player.addChatMessage(new ChatComponentText("You have teleported to " + wName + "."));
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
