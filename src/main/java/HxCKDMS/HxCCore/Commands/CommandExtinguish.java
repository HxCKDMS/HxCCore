package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Configs.Configurations;
import HxCKDMS.HxCCore.Handlers.PermissionsHandler;
import HxCKDMS.HxCCore.api.Command.HxCCommand;
import HxCKDMS.HxCCore.api.Command.ISubCommand;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;

import java.util.List;

@HxCCommand(defaultPermission = 2, mainCommand = CommandMain.class)
public class CommandExtinguish implements ISubCommand {
    public static CommandExtinguish instance = new CommandExtinguish();

    @Override
    public String getCommandName() {
        return "extinguish";
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] args) throws PlayerNotFoundException, WrongUsageException {
        switch(args.length) {
            case 1:
                if(sender instanceof EntityPlayerMP) {
                    EntityPlayerMP player = (EntityPlayerMP) sender;
                    boolean CanSend = PermissionsHandler.canUseCommand(Configurations.commands.get("Extinguish"), player);
                    if (CanSend) {
                        player.extinguish();
                        player.addChatMessage(new ChatComponentText("\u00A7bYou suddenly feel refreshed."));
                    } else throw new WrongUsageException(StatCollector.translateToLocal("command.exception.playersonly"));
                }
            break;
            case 2:
                EntityPlayerMP player2 = CommandBase.getPlayer(sender, args[1]);
                player2.extinguish();
                player2.addChatMessage(new ChatComponentText("\u00A7bYou suddenly feel refreshed."));
                sender.addChatMessage(new ChatComponentText("\u00A7eYou have extinguished " + player2.getDisplayName()));
            break;
            default: throw new WrongUsageException(StatCollector.translateToLocal("commands." + getCommandName() + ".usage"));
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
