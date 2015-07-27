package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Configs.Configurations;
import HxCKDMS.HxCCore.Handlers.NBTFileIO;
import HxCKDMS.HxCCore.Handlers.PermissionsHandler;
import HxCKDMS.HxCCore.HxCCore;
import HxCKDMS.HxCCore.api.Utils.Teleporter;
import HxCKDMS.HxCCore.api.ISubCommand;
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
    public void handleCommand(ICommandSender sender, String[] args) {
        if(sender instanceof EntityPlayerMP){
            EntityPlayerMP player = (EntityPlayerMP)sender;
            boolean CanSend = PermissionsHandler.canUseCommand(Configurations.PermLevels.get(15), player);
            if (CanSend) {
                File HxCWorldData = new File(HxCCore.HxCCoreDir, "HxCWorld.dat");

                String wName = args.length == 1 ? "default" : args[1];
                NBTTagCompound warpDir = NBTFileIO.getNbtTagCompound(HxCWorldData, "warp");
                if(!warpDir.hasKey(wName)) throw new WrongUsageException("The warp named: '" + wName + "' does not exist.");
                NBTTagCompound warp = warpDir.getCompoundTag(wName);
                if(player.dimension != warp.getInteger("dim")) {
                    Teleporter.transferPlayerToDimension(player, warp.getInteger("dim"), warp.getInteger("x"), warp.getInteger("y"), warp.getInteger("z"));
                    player.addChatMessage(new ChatComponentText("You have teleported to " + wName + "."));
                } else {
                    player.playerNetServerHandler.setPlayerLocation(warp.getInteger("x"), warp.getInteger("y"), warp.getInteger("z"), player.rotationYaw, player.rotationPitch);
                    player.addChatMessage(new ChatComponentText("You have teleported to " + wName + "."));
                }
            } else sender.addChatMessage(new ChatComponentText("\u00A74You do not have permission to use this command."));
        } else sender.addChatMessage(new ChatComponentText("\u00A74This command can only be executed by a player."));
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        return null;
    }
}
