package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Configs.CommandsConfig;
import HxCKDMS.HxCCore.Handlers.NBTFileIO;
import HxCKDMS.HxCCore.Handlers.PermissionsHandler;
import HxCKDMS.HxCCore.HxCCore;
import HxCKDMS.HxCCore.api.Command.HxCCommand;
import HxCKDMS.HxCCore.api.Command.ISubCommand;
import HxCKDMS.HxCCore.lib.References;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;

import java.io.File;
import java.util.List;

@HxCCommand(defaultPermission = 1, mainCommand = CommandMain.class)
public class CommandNick implements ISubCommand {
    public static CommandNick instance = new CommandNick();

    @Override
    public String getCommandName() {
        return "Nick";
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] args) throws WrongUsageException {
        if (sender instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) sender;
            boolean CanSend = PermissionsHandler.canUseCommand(CommandsConfig.commands.get("Nick"), player);
            if (CanSend) {
                String UUID = player.getUniqueID().toString();
                File CustomPlayerData = new File(HxCCore.HxCCoreDir, "HxC-" + UUID + ".dat");

                if (args.length == 1) {
                    NBTFileIO.setString(CustomPlayerData, "nickname", "");
                    player.addChatMessage(new ChatComponentText("Your nickname has been removed."));
                } else {
                    String nick = null;

                    for(int i = 1; i < args.length; i++){
                        if(nick == null)
                            nick = args[i];
                        else
                            nick = nick + " " + args[i];
                    }

                    NBTFileIO.setString(CustomPlayerData, "nickname", nick);
                    player.addChatMessage(new ChatComponentText(("Your nickname has been set to " + nick).replace("&", References.CC)));
                }
            } else throw new WrongUsageException(StatCollector.translateToLocal("commands.exception.permission"));
        } else throw new WrongUsageException(StatCollector.translateToLocal("commands.exception.playersonly"));
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        return null;
    }
}
