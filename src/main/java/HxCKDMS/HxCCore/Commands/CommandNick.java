package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Configs.Config;
import HxCKDMS.HxCCore.Handlers.NBTFileIO;
import HxCKDMS.HxCCore.Handlers.PermissionsHandler;
import HxCKDMS.HxCCore.HxCCore;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;

import java.io.File;
import java.util.List;

public class CommandNick implements ISubCommand {
    public static CommandNick instance = new CommandNick();

    @Override
    public String getName() {
        return "nick";
    }

    @Override
    public void execute(ICommandSender sender, String[] args) {
        if(sender instanceof EntityPlayerMP){
            EntityPlayerMP player = (EntityPlayerMP)sender;
            boolean CanSend = PermissionsHandler.canUseCommand(Config.NickPL, player);
            if (CanSend) {
                String UUID = player.getUniqueID().toString();
                File CustomPlayerData = new File(HxCCore.HxCCoreDir, "HxC-" + UUID + ".dat");

                if (args.length == 1) {
                    NBTFileIO.setString(CustomPlayerData, "nickname", "");
                    player.addChatMessage(new ChatComponentText("Your nickname has been removed."));
                } else {
                    NBTFileIO.setString(CustomPlayerData, "nickname", args[1]);
                    ChatComponentText msg = new ChatComponentText(("Your nickname has been set to " + args[1]).replace("&", "\u00A7"));
                    player.addChatMessage(msg);
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
