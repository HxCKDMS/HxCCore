package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Configs.Config;
import HxCKDMS.HxCCore.Handlers.NBTFileIO;
import HxCKDMS.HxCCore.HxCCore;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;

import java.io.File;
import java.util.List;

public class CommandSetPerms implements ISubCommand {
    public static CommandSetPerms instance = new CommandSetPerms();
    int permLevel;
    String playerName;
    String CC = "\u00A7";

    String PL0 = (CC + Config.PermLevel0Color + Config.PermLevel0Name);
    String PL1 = (CC + Config.PermLevel1Color + Config.PermLevel1Name);
    String PL2 = (CC + Config.PermLevel2Color + Config.PermLevel2Name);
    String PL3 = (CC + Config.PermLevel3Color + Config.PermLevel3Name);
    String PL4 = (CC + Config.PermLevel4Color + Config.PermLevel4Name);
    String PL5 = (CC + Config.PermLevel5Color + Config.PermLevel5Name);

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
            NBTFileIO.setNbtTagCompound(PermissionsData, "Permissions", Permissions);
            boolean isopped = HxCCore.server.getConfigurationManager().func_152596_g(player.getGameProfile());

            String playername = player.getDisplayName();
            NBTTagCompound PermLevel = Permissions.getCompoundTag("PermLevel");

            int PlayerPerms = (Permissions.getInteger(playername));
            if (PlayerPerms >= 4 || isopped) {
                if (args.length == 3) {
                    playerName = args[1];
                    permLevel = Integer.parseInt(args[2]);
                } else {
                    player.addChatMessage(new ChatComponentText("\u00A74Correct usage is: " + "\u00A72/HxCCore setPerms <Player> <PermLevel(0-5)>"));
                }
                if (permLevel > 5 || permLevel < 0) {
                    player.addChatMessage(new ChatComponentText("\u00A74Correct usage is: " + "\u00A72/HxCCore setPerms <Player> <PermLevel(0-5)>"));
                }

                switch (permLevel) {
                    case 1:
                        PermLevel.setInteger(playerName, permLevel);
                        player.addChatMessage(new ChatComponentText(CC + "6" + playerName + "'s" + CC + "6 Permissions Level was set to " + PL1 + CC + "6."));
                        break;
                    case 2:
                        PermLevel.setInteger(playerName, permLevel);
                        player.addChatMessage(new ChatComponentText(CC + "6" + playerName + "'s" + CC + "6 Permissions Level was set to " + PL2 + CC + "6."));
                        break;
                    case 3:
                        PermLevel.setInteger(playerName, permLevel);
                        player.addChatMessage(new ChatComponentText(CC + "6" + playerName + "'s" + CC + "6 Permissions Level was set to " + PL3 + CC + "6."));
                        break;
                    case 4:
                        PermLevel.setInteger(playerName, permLevel);
                        player.addChatMessage(new ChatComponentText(CC + "6" + playerName + "'s" + CC + "6 Permissions Level was set to " + PL4 + CC + "6."));
                        break;
                    case 5:
                        PermLevel.setInteger(playerName, permLevel);
                        player.addChatMessage(new ChatComponentText(CC + "6" + playerName + "'s" + CC + "6 Permissions Level was set to " + PL5 + CC + "6."));
                        break;
                    default:
                        PermLevel.setInteger(playerName, permLevel);
                        player.addChatMessage(new ChatComponentText(CC + "6" + playerName + "'s" + CC + "6 Permissions Level was set to " + PL0 + CC + "6."));
                        break;
                }

                NBTFileIO.setNbtTagCompound(PermissionsData, "Permissions", Permissions);
            } else {
                player.addChatMessage(new ChatComponentText("\u00A74You do not have permission to use this command."));
            }
        } else {
            File PermissionsData = new File(HxCCore.HxCCoreDir, "HxC-Permissions.dat");
            NBTTagCompound Permissions = NBTFileIO.getNbtTagCompound(PermissionsData, "Permissions");
            NBTTagCompound PermLevel = Permissions.getCompoundTag("PermLevel");
            if (!(sender instanceof EntityLiving)) {
                if (args.length == 3) {
                    playerName = args[1];
                    permLevel = Integer.parseInt(args[2]);
                } else {
                    sender.addChatMessage(new ChatComponentText("Correct usage is: /HxCCore setPerms <Player> <PermLevel(0-5)>"));
                }
                if (permLevel > 5 || permLevel < 0) {
                    sender.addChatMessage(new ChatComponentText("Correct usage is: /HxCCore setPerms <Player> <PermLevel(0-5)>"));
                }

                switch (permLevel) {
                    case 1:
                        PermLevel.setInteger(playerName, permLevel);
                        sender.addChatMessage(new ChatComponentText(playerName + "'s Permissions Level was set to " + Config.PermLevel1Name + "."));
                        break;
                    case 2:
                        PermLevel.setInteger(playerName, permLevel);
                        sender.addChatMessage(new ChatComponentText(playerName + "'s Permissions Level was set to " + Config.PermLevel2Name + "."));
                        break;
                    case 3:
                        PermLevel.setInteger(playerName, permLevel);
                        sender.addChatMessage(new ChatComponentText(playerName + "'s Permissions Level was set to " + Config.PermLevel3Name + "."));
                        break;
                    case 4:
                        PermLevel.setInteger(playerName, permLevel);
                        sender.addChatMessage(new ChatComponentText(playerName + "'s Permissions Level was set to " + Config.PermLevel4Name + "."));
                        break;
                    case 5:
                        PermLevel.setInteger(playerName, permLevel);
                        sender.addChatMessage(new ChatComponentText(playerName + "'s Permissions Level was set to " + Config.PermLevel5Name + "."));
                        break;
                    default:
                        PermLevel.setInteger(playerName, permLevel);
                        sender.addChatMessage(new ChatComponentText(playerName + "'s Permissions Level was set to " + Config.PermLevel0Name + "."));
                        break;
                }
                NBTFileIO.setNbtTagCompound(PermissionsData, "Permissions", Permissions);
            }else{
                sender.addChatMessage(new ChatComponentText("Please set your permissions from console or ask a(n) " + Config.PermLevel5Name + " to set your perms."));
            }
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        return null;
    }
}
