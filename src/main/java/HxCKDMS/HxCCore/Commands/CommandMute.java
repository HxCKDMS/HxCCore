package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Configs.Configurations;
import HxCKDMS.HxCCore.Handlers.NBTFileIO;
import HxCKDMS.HxCCore.Handlers.PermissionsHandler;
import HxCKDMS.HxCCore.HxCCore;
import HxCKDMS.HxCCore.api.Command.HxCCommand;
import HxCKDMS.HxCCore.api.Command.ISubCommand;
import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;

import java.io.File;
import java.util.List;

@SuppressWarnings("unchecked")
@HxCCommand(defaultPermission = 3, mainCommand = CommandMain.class)
public class CommandMute implements ISubCommand {
    public static CommandMute instance = new CommandMute();

    @Override
    public String getCommandName() {
        return "Mute";
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] args) throws WrongUsageException, PlayerNotFoundException {
        boolean CanSend = (!(sender instanceof EntityPlayerMP)) || PermissionsHandler.canUseCommand(Configurations.commands.get("Mute"), (EntityPlayerMP) sender);
        EntityPlayerMP player;
        File worldData = new File(HxCCore.HxCCoreDir, "HxCWorld.dat");
        NBTTagCompound mutes = NBTFileIO.getNbtTagCompound(worldData, "mutedPlayers");
        if (CanSend) {
            switch(args.length){
                case 1:
                    if(sender instanceof EntityPlayerMP){
                        player = (EntityPlayerMP)sender;
                        player.addChatMessage(new ChatComponentText("\u00A79You've been muted!"));
                        mutes.setBoolean(player.getUniqueID().toString(), true);
                        NBTFileIO.setNbtTagCompound(worldData, "mutedPlayers", mutes);
                    } else
                        throw new WrongUsageException(StatCollector.translateToLocal("command.exception.playersonly"));
                    break;
                case 2:
                    player = net.minecraft.command.CommandBase.getPlayer(sender, args[1]);
                    player.addChatMessage(new ChatComponentText("\u00A79You've been muted!"));
                    mutes.setBoolean(player.getUniqueID().toString(), true);
                    NBTFileIO.setNbtTagCompound(worldData, "mutedPlayers", mutes);
                    break;
                case 3:
                    player = net.minecraft.command.CommandBase.getPlayer(sender, args[1]);
                    player.addChatMessage(new ChatComponentText("\u00A79You've been " + (Boolean.parseBoolean(args[2]) ? "muted!" : "unmuted!")));
                    mutes.setBoolean(player.getUniqueID().toString(), Boolean.parseBoolean(args[2]));
                    NBTFileIO.setNbtTagCompound(worldData, "mutedPlayers", mutes);
                    break;
                /*case 3:
                    player = net.minecraft.command.CommandBase.getPlayer(sender, args[1]);
                    player.addChatMessage(new ChatComponentText("\u00A79You've been muted for " + Integer.parseInt(args[2]) / 60 + " minutes!"));
                    mutes.setBoolean(player.getUniqueID().toString(), true);
                    break;*/ //TODO: Find good way to make timed mute without causing lag with yet another tick counter
                default: throw new WrongUsageException(StatCollector.translateToLocal("commands." + getCommandName().toLowerCase() + ".usage"));
            }
        } else throw new WrongUsageException(StatCollector.translateToLocal("command.exception.permission"));
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        if(args.length == 2){
            return net.minecraft.command.CommandBase.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
        }
        return null;
    }
}
