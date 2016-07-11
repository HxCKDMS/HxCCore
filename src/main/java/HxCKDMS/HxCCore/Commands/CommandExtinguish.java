package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Configs.CommandsConfig;
import HxCKDMS.HxCCore.HxCCore;
import HxCKDMS.HxCCore.api.Command.HxCCommand;
import HxCKDMS.HxCCore.api.Command.ISubCommand;
import HxCKDMS.HxCCore.api.Handlers.CommandsHandler;
import HxCKDMS.HxCCore.api.Handlers.PermissionsHandler;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;

import java.util.List;

@HxCCommand(defaultPermission = 2, mainCommand = CommandsHandler.class, isEnabled = true)
public class CommandExtinguish implements ISubCommand {
    public static CommandExtinguish instance = new CommandExtinguish();

    @Override
    public String getCommandName() {
        return "Extinguish";
    }

    @Override
    public int[] getCommandRequiredParams() {
        return new int[]{0, 1, -1};
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] args, boolean isPlayer) {
        switch(args.length) {
            case 1:
                if (isPlayer) {
                    EntityPlayerMP player = (EntityPlayerMP) sender;
                    boolean CanSend = PermissionsHandler.canUseCommand(CommandsConfig.CommandPermissions.get(getCommandName()), player);
                    if (CanSend) {
                        player.extinguish();
                        player.addChatMessage(new ChatComponentText("\u00A7bYou suddenly feel refreshed."));
                    } else throw new WrongUsageException(HxCCore.util.getTranslation((isPlayer ? ((EntityPlayerMP) sender).getUniqueID() : java.util.UUID.randomUUID()), "commands.exception.playersonly"));
                }
            break;
            case 2:
                EntityPlayerMP player2 = CommandBase.getPlayer(sender, args[1]);
                player2.extinguish();
                player2.addChatMessage(new ChatComponentText("\u00A7bYou suddenly feel refreshed."));
                sender.addChatMessage(new ChatComponentText("\u00A7eYou have extinguished " + player2.getDisplayName()));
            break;
            default: throw new WrongUsageException(HxCCore.util.getTranslation((isPlayer ? ((EntityPlayerMP) sender).getUniqueID() : java.util.UUID.randomUUID()), "commands." + getCommandName().toLowerCase() + ".usage"));
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        if(args.length == 2){
            return CommandBase.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
        }
        return null;
    }
}
