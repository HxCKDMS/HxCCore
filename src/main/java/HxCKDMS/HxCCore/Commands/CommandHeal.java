package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Configs.Config;
import HxCKDMS.HxCCore.Handlers.PermissionsHandler;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;

import java.util.List;

public class CommandHeal implements ISubCommand {

    public static CommandHeal instance = new CommandHeal();

    @Override
    public String getName() {
        return "heal";
    }

    public void execute(ICommandSender sender, String[] args) throws PlayerNotFoundException, WrongUsageException {
        switch(args.length){
            case 1: {
                if(sender instanceof EntityPlayer){
                    EntityPlayerMP player = (EntityPlayerMP) sender;
                    boolean CanSend = PermissionsHandler.canUseCommand(Config.HealPL, player);
                    if (CanSend) {
                        player.setHealth(player.getMaxHealth());
                        sender.addChatMessage(new ChatComponentText("\u00A76Healed."));
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
                player2.setHealth(player2.getMaxHealth());
                player2.addChatMessage(new ChatComponentText("\u00A76You have received some divine intervention."));
                sender.addChatMessage(new ChatComponentText("\u00A76Healed " + player2.getName() + "."));
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
