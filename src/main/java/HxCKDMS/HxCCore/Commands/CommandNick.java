package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Configs.CommandsConfig;
import HxCKDMS.HxCCore.Handlers.CommandsHandler;
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

import java.io.File;
import java.util.List;

@HxCCommand(defaultPermission = 1, mainCommand = CommandsHandler.class, isEnabled = true)
public class CommandNick implements ISubCommand {
    public static CommandNick instance = new CommandNick();

    @Override
    public String getCommandName() {
        return "Nick";
    }

    @Override
    public int[] getCommandRequiredParams() {
        return new int[]{0, -1, -1};
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] args, boolean isPlayer) throws WrongUsageException {
        if (isPlayer) {
            EntityPlayerMP player = (EntityPlayerMP) sender;
            boolean CanSend = PermissionsHandler.canUseCommand(CommandsConfig.CommandPermissions.get(getCommandName()), player);
            if (CanSend) {

                if (args.length == 1) {
                    String UUID = player.getUniqueID().toString();
                    File CustomPlayerData = new File(HxCCore.HxCCoreDir, "HxC-" + UUID + ".dat");
                    NBTFileIO.setString(CustomPlayerData, "nickname", "");
                    player.addChatMessage(new ChatComponentText("Your nickname has been removed."));
                } else if (args.length == 3) {
                    EntityPlayerMP player2 = CommandsHandler.getPlayer(sender, args[1]);
                    String UUID = player2.getUniqueID().toString();
                    File CustomPlayerData = new File(HxCCore.HxCCoreDir, "HxC-" + UUID + ".dat");
                    if (!PermissionsHandler.hasHighestPermissions(player2)) {
                        if (args[2].equalsIgnoreCase("remove")) {
                            NBTFileIO.setString(CustomPlayerData, "nickname", "");
                            player.addChatMessage(new ChatComponentText(player2.getDisplayName() + "'s nickname has been removed."));
                            player2.addChatMessage(new ChatComponentText("Your nickname has been removed by " + player.getDisplayName()));
                        } else {
                            NBTFileIO.setString(CustomPlayerData, "nickname", args[2]);
                            player.addChatMessage(new ChatComponentText(player2.getDisplayName() + "'s nickname has been changed to " + args[2].replaceAll("&",  References.CC)));
                            player2.addChatMessage(new ChatComponentText("Your nickname has been changed to" + args[2].replaceAll("&",  References.CC) + " by " + player.getDisplayName()));
                        }
                    }
                } else {
                    String UUID = player.getUniqueID().toString();
                    File CustomPlayerData = new File(HxCCore.HxCCoreDir, "HxC-" + UUID + ".dat");
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
            } else throw new WrongUsageException(HxCCore.util.readLangOnServer(References.MOD_ID, "commands.exception.permission"));
        } else throw new WrongUsageException(HxCCore.util.readLangOnServer(References.MOD_ID, "commands.exception.playersonly"));
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        return null;
    }
}
