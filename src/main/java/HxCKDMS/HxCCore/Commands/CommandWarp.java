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
    public void handleCommand(ICommandSender sender, String[] args) {
        if(sender instanceof EntityPlayerMP){
            EntityPlayerMP player = (EntityPlayerMP)sender;
            File HxCWorldData = new File(HxCCore.HxCCoreDir, "HxCWorld.dat");

            String wName = args.length == 1 ? "default" : args[1];
            NBTTagCompound warpDir = NBTFileIO.getNbtTagCompound(HxCWorldData, "warp");
            if(!warpDir.hasKey(wName)){
                throw new WrongUsageException("the warp named: '" + wName + "' does not exist.");
            }
            NBTTagCompound warp = warpDir.getCompoundTag(wName);

            if(player.dimension != warp.getInteger("dim"))
                Teleporter.transferPlayerToDimension(player, warp.getInteger("dim"), player.mcServer.getConfigurationManager(), warp.getInteger("x"), warp.getInteger("y"), warp.getInteger("z"));
            else
                player.playerNetServerHandler.setPlayerLocation(warp.getInteger("x"), warp.getInteger("y"), warp.getInteger("z"), player.rotationYaw, player.rotationPitch);

        }else{
            sender.addChatMessage(new ChatComponentText("\u00A74This command can only be executed by a player."));
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        return null;
    }
}
