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
import net.minecraft.util.StatCollector;

import java.io.File;
import java.util.Arrays;
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
            boolean CanSend = PermissionsHandler.canUseCommand(Configurations.commands.get("Warp"), player);
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
            } else throw new WrongUsageException(StatCollector.translateToLocal("commands.exception.permission"));
        } else throw new WrongUsageException(StatCollector.translateToLocal("commands.exception.playersonly"));
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        if (args.length == 2) {
            File CustomPlayerData = new File(HxCCore.HxCCoreDir, "HxCWorld.dat");
            NBTTagCompound warp = NBTFileIO.getNbtTagCompound(CustomPlayerData, "warp");
            if (warp.getString("warpsList") != null) return Arrays.asList(warp.getString("warpsList").split(", "));
            else return null;
        }
        return null;
    }
}
