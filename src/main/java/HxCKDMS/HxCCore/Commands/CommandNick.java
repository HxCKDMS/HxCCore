package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Handlers.NBTFileIO;
import HxCKDMS.HxCCore.HxCCore;
import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;

import java.io.File;
import java.util.List;

public class CommandNick implements ISubCommand {
    public static CommandNick instance = new CommandNick();

    @Override
    public String getCommandName() {
        return "nick";
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] args) {
        if(sender instanceof EntityPlayerMP){
            EntityPlayerMP player = (EntityPlayerMP)sender;

            File PermissionsData = new File(HxCCore.HxCCoreDir, "HxC-Permissions.dat");
            NBTTagCompound Permissions = NBTFileIO.getNbtTagCompound(PermissionsData, "Permissions");
            int SenderPermLevel = Permissions.getInteger(player.getName());
            boolean isopped = HxCCore.server.getConfigurationManager().canSendCommands(player.getGameProfile());
            if (SenderPermLevel >= 1 || isopped) {
                String UUID = player.getUniqueID().toString();
                File CustomPlayerData = new File(HxCCore.HxCCoreDir, "HxC-" + UUID + ".dat");

                if (args.length == 1) {
                    NBTFileIO.setString(CustomPlayerData, "nickname", "");
                    player.addChatMessage(new ChatComponentText("Your nickname has been removed."));
                } else {
                    NBTFileIO.setString(CustomPlayerData, "nickname", args[1]);
                    player.addChatMessage(new ChatComponentText("Your nickname has been set to " + args[1]));
                }
            } else {
                sender.addChatMessage(new ChatComponentText("\u00A74You don't have permission to use this command."));
            }
        } else {
            sender.addChatMessage(new ChatComponentText("Only a player can use this command."));
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
