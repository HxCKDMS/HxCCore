package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Configs.Configurations;
import HxCKDMS.HxCCore.Handlers.NBTFileIO;
import HxCKDMS.HxCCore.Handlers.PermissionsHandler;
import HxCKDMS.HxCCore.HxCCore;
import HxCKDMS.HxCCore.api.ISubCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;

import java.io.File;
import java.util.List;

public class CommandSetPerms implements ISubCommand {
    public static CommandSetPerms instance = new CommandSetPerms();
    int permLevel;
    String playerName;
    String CC = "\u00A7";

    String PL0 = (CC + Configurations.PermLevelColor.get(0) + Configurations.PermLevelName.get(0));
    String PL1 = (CC + Configurations.PermLevelColor.get(1) + Configurations.PermLevelName.get(1));
    String PL2 = (CC + Configurations.PermLevelColor.get(2) + Configurations.PermLevelName.get(2));
    String PL3 = (CC + Configurations.PermLevelColor.get(3) + Configurations.PermLevelName.get(3));
    String PL4 = (CC + Configurations.PermLevelColor.get(4) + Configurations.PermLevelName.get(4));
    String PL5 = (CC + Configurations.PermLevelColor.get(5) + Configurations.PermLevelName.get(5));

    @Override
    public String getCommandName() {
        return "setPerms";
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] args) {
        if (sender instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) sender;
            File PermissionsData = new File(HxCCore.HxCCoreDir, "HxC-Permissions.dat");
            NBTTagCompound Permissions = NBTFileIO.getNbtTagCompound(PermissionsData, "Permissions");
            boolean CanUse = PermissionsHandler.canUseCommand(5, player);
            if (CanUse) {
                if (args.length == 3) {
                    playerName = args[1];
                    permLevel = Integer.parseInt(args[2]);
                    if (permLevel > 5 || permLevel < 0) {
                        player.addChatMessage(new ChatComponentText("\u00A74Correct usage is: " + "\u00A72/HxCCore setPerms <Player> <PermLevel(0-5)>"));
                    }

                    switch (permLevel) {
                        case 1:
                            Permissions.setInteger(playerName, permLevel);
                            player.addChatMessage(new ChatComponentText(CC + "6" + playerName + "'s" + CC + "6 Permissions Level was set to " + PL1 + CC + "6."));
                            break;
                        case 2:
                            Permissions.setInteger(playerName, permLevel);
                            player.addChatMessage(new ChatComponentText(CC + "6" + playerName + "'s" + CC + "6 Permissions Level was set to " + PL2 + CC + "6."));
                            break;
                        case 3:
                            Permissions.setInteger(playerName, permLevel);
                            player.addChatMessage(new ChatComponentText(CC + "6" + playerName + "'s" + CC + "6 Permissions Level was set to " + PL3 + CC + "6."));
                            break;
                        case 4:
                            Permissions.setInteger(playerName, permLevel);
                            player.addChatMessage(new ChatComponentText(CC + "6" + playerName + "'s" + CC + "6 Permissions Level was set to " + PL4 + CC + "6."));
                            break;
                        case 5:
                            Permissions.setInteger(playerName, permLevel);
                            player.addChatMessage(new ChatComponentText(CC + "6" + playerName + "'s" + CC + "6 Permissions Level was set to " + PL5 + CC + "6."));
                            break;
                        default:
                            Permissions.setInteger(playerName, permLevel);
                            player.addChatMessage(new ChatComponentText(CC + "6" + playerName + "'s" + CC + "6 Permissions Level was set to " + PL0 + CC + "6."));
                            break;
                    }
                    NBTFileIO.setNbtTagCompound(PermissionsData, "Permissions", Permissions);
                } else sender.addChatMessage(new ChatComponentText("\u00A74You do not have permission to use this command."));
            } else player.addChatMessage(new ChatComponentText("\u00A74You do not have permission to use this command."));
        } else {
            File PermissionsData = new File(HxCCore.HxCCoreDir, "HxC-Permissions.dat");
            NBTTagCompound Permissions = NBTFileIO.getNbtTagCompound(PermissionsData, "Permissions");
            if (args.length == 3) {
                playerName = args[1];
                permLevel = Integer.parseInt(args[2]);
                if (permLevel > 5 || permLevel < 0) {
                    sender.addChatMessage(new ChatComponentText("Correct usage is: /HxCCore setPerms <Player> <PermLevel(0-5)>"));
                }

                switch (permLevel) {
                    case 1:
                        Permissions.setInteger(playerName, permLevel);
                        sender.addChatMessage(new ChatComponentText(playerName + "'s Permissions Level was set to " + Configurations.PermLevelName.get(1) + "."));
                        break;
                    case 2:
                        Permissions.setInteger(playerName, permLevel);
                        sender.addChatMessage(new ChatComponentText(playerName + "'s Permissions Level was set to " + Configurations.PermLevelName.get(2) + "."));
                        break;
                    case 3:
                        Permissions.setInteger(playerName, permLevel);
                        sender.addChatMessage(new ChatComponentText(playerName + "'s Permissions Level was set to " + Configurations.PermLevelName.get(3) + "."));
                        break;
                    case 4:
                        Permissions.setInteger(playerName, permLevel);
                        sender.addChatMessage(new ChatComponentText(playerName + "'s Permissions Level was set to " + Configurations.PermLevelName.get(4) + "."));
                        break;
                    case 5:
                        Permissions.setInteger(playerName, permLevel);
                        sender.addChatMessage(new ChatComponentText(playerName + "'s Permissions Level was set to " + Configurations.PermLevelName.get(5) + "."));
                        break;
                    default:
                        Permissions.setInteger(playerName, permLevel);
                        sender.addChatMessage(new ChatComponentText(playerName + "'s Permissions Level was set to " + Configurations.PermLevelName.get(0) + "."));
                        break;
                }
                NBTFileIO.setNbtTagCompound(PermissionsData, "Permissions", Permissions);
            } else sender.addChatMessage(new ChatComponentText("Correct usage is: /HxCCore setPerms <Player> <PermLevel(0-5)>"));
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
