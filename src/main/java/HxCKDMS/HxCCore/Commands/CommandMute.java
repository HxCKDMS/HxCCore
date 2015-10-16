package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Configs.CommandsConfig;
import HxCKDMS.HxCCore.Handlers.CommandsHandler;
import HxCKDMS.HxCCore.api.Handlers.NBTFileIO;
import HxCKDMS.HxCCore.api.Handlers.PermissionsHandler;
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
@HxCCommand(defaultPermission = 3, mainCommand = CommandsHandler.class, isEnabled = true)
public class CommandMute implements ISubCommand {
    public static CommandMute instance = new CommandMute();
    //TODO: Make it unmute muted players without requiring boolean
    @Override
    public String getCommandName() {
        return "Mute";
    }

    @Override
    public int[] getCommandRequiredParams() {
        return new int[]{0, 1, -1};
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] args, boolean isPlayer) throws WrongUsageException, PlayerNotFoundException {
        boolean CanSend = (!(sender instanceof EntityPlayerMP)) || PermissionsHandler.canUseCommand(CommandsConfig.CommandPermissions.get("Mute"), (EntityPlayerMP) sender);
        EntityPlayerMP player;
        File worldData = new File(HxCCore.HxCCoreDir, "HxCWorld.dat");
        NBTTagCompound mutes = NBTFileIO.getNbtTagCompound(worldData, "mutedPlayers");
        if (CanSend) {
            switch(args.length){
                case 1:
                    if (isPlayer) {
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
                /*case 4:
                    player = net.minecraft.command.CommandBase.getPlayer(sender, args[1]);
                    player.addChatMessage(new ChatComponentText("\u00A79You've been muted for " + Integer.parseInt(args[2]) / 60 + " minutes!"));
                    mutes.setBoolean(player.getUniqueID().toString(), true);
                    break;*/ //TODO: Find good way to make timed mute without causing lag with yet another tick counter
                default: throw new WrongUsageException(StatCollector.translateToLocal("commands." + getCommandName().toLowerCase() + ".usage"));
            }
        }  else throw new WrongUsageException(StatCollector.translateToLocal("command.exception.permission"));
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        if(args.length == 2){
            return net.minecraft.command.CommandBase.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
        }
        return null;
    }
}
