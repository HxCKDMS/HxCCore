package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Configs.Configurations;
import HxCKDMS.HxCCore.Handlers.PermissionsHandler;
import HxCKDMS.HxCCore.api.ISubCommand;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;

import java.util.List;

public class CommandExtinguish implements ISubCommand {
    public static CommandExtinguish instance = new CommandExtinguish();

    @Override
    public String getName() {
        return "extinguish";
    }

    @Override
    public void execute(ICommandSender sender, String[] args) throws PlayerNotFoundException, WrongUsageException {
        switch(args.length){
            case 1: {
                if(sender instanceof EntityPlayerMP){
                    EntityPlayerMP player = (EntityPlayerMP)sender;
                    boolean CanSend = PermissionsHandler.canUseCommand(Configurations.commands.get("Extinguish"), player);
                    if (CanSend) {
                        player.extinguish();
                        player.addChatMessage(new ChatComponentText("\u00A7bYou suddenly feel refreshed."));
                    }
                }else{
                    sender.addChatMessage(new ChatComponentText("\u00A74This command without parameters can only be executed by a player."));
                }
            }
            break;
            case 2: {
                EntityPlayerMP player2 = CommandBase.getPlayer(sender, args[1]);
                player2.extinguish();
                player2.addChatMessage(new ChatComponentText("\u00A7bYou suddenly feel refreshed."));
                sender.addChatMessage(new ChatComponentText("\u00A7eYou have extinguished " + player2.getDisplayName()));
            }
            break;
            default: {
                throw new WrongUsageException("Correct usage is: /"+getName()+" [player]");

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
