package HxCKDMS.HxCCore.Commands;

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

import static HxCKDMS.HxCCore.lib.References.PERM_COLOURS;
import static HxCKDMS.HxCCore.lib.References.PERM_NAMES;
import static HxCKDMS.HxCCore.lib.References.CC;

public class CommandSetPerms implements ISubCommand {
    public static CommandSetPerms instance = new CommandSetPerms();
    int permLevel;
    String playerName;

    String PL0 = (CC + PERM_COLOURS[0] + PERM_NAMES[0]);
    String PL1 = (CC + PERM_COLOURS[1] + PERM_NAMES[1]);
    String PL2 = (CC + PERM_COLOURS[2] + PERM_NAMES[2]);
    String PL3 = (CC + PERM_COLOURS[3] + PERM_NAMES[3]);
    String PL4 = (CC + PERM_COLOURS[4] + PERM_NAMES[4]);
    String PL5 = (CC + PERM_COLOURS[5] + PERM_NAMES[5]);

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
                        sender.addChatMessage(new ChatComponentText(playerName + "'s Permissions Level was set to " + PL1 + "."));
                        break;
                    case 2:
                        Permissions.setInteger(playerName, permLevel);
                        sender.addChatMessage(new ChatComponentText(playerName + "'s Permissions Level was set to " + PL2 + "."));
                        break;
                    case 3:
                        Permissions.setInteger(playerName, permLevel);
                        sender.addChatMessage(new ChatComponentText(playerName + "'s Permissions Level was set to " + PL3 + "."));
                        break;
                    case 4:
                        Permissions.setInteger(playerName, permLevel);
                        sender.addChatMessage(new ChatComponentText(playerName + "'s Permissions Level was set to " + PL4 + "."));
                        break;
                    case 5:
                        Permissions.setInteger(playerName, permLevel);
                        sender.addChatMessage(new ChatComponentText(playerName + "'s Permissions Level was set to " + PL5 + "."));
                        break;
                    default:
                        Permissions.setInteger(playerName, permLevel);
                        sender.addChatMessage(new ChatComponentText(playerName + "'s Permissions Level was set to " + PL0 + "."));
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
