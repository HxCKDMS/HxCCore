package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Configs.Config;
import HxCKDMS.HxCCore.Handlers.PermissionsHandler;
import HxCKDMS.HxCCore.api.ISubCommand;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;

import java.util.List;

public class CommandBurn implements ISubCommand {
    public static CommandBurn instance = new CommandBurn();

    @Override
    public String getCommandName() {
        return "burn";
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] args) {
        switch(args.length){
            case 1: {
                if(sender instanceof EntityPlayerMP){
                    EntityPlayerMP player = (EntityPlayerMP)sender;
                    boolean CanSend = PermissionsHandler.canUseCommand(Config.BurnPL, player);
                    if (CanSend) {
                        player.addChatMessage(new ChatComponentText("\u00A79You suddenly feel warmer."));
                        player.setFire(750000000);
                    } else {
                        sender.addChatMessage(new ChatComponentText("\u00A74You do not have permission to use this command."));
                    }
                }else{
                    sender.addChatMessage(new ChatComponentText("\u00A74This command without parameters can only be executed by a player."));
                }
            }
            break;
            case 2: {
                EntityPlayerMP player2 = CommandBase.getPlayer(sender, args[1]);
                player2.addChatMessage(new ChatComponentText("\u00A79You suddenly feel warmer."));
                player2.setFire(750000000);
                sender.addChatMessage(new ChatComponentText(player2.getDisplayName() + " \u00A74has been set on fire for 750000000 ticks."));
            }
            case 3: {
                EntityPlayerMP player2 = CommandBase.getPlayer(sender, args[1]);
                player2.addChatMessage(new ChatComponentText("\u00A79You suddenly feel warmer."));
                player2.setFire(Integer.parseInt(args[2]));
                sender.addChatMessage(new ChatComponentText(player2.getDisplayName() + " \u00A74has been set on fire for " + Integer.parseInt(args[2]) + " ticks."));
            }
            break;
            default: {
                throw new WrongUsageException("Correct usage is: /"+getCommandName()+" [player] [time]");

            }
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
