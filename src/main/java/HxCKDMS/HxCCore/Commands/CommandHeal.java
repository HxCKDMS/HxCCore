package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Configs.Configurations;
import HxCKDMS.HxCCore.Handlers.PermissionsHandler;
import HxCKDMS.HxCCore.api.Command.HxCCommand;
import HxCKDMS.HxCCore.api.Command.ISubCommand;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;

import java.util.List;

@HxCCommand(defaultPermission = 2, mainCommand = CommandMain.class)
public class CommandHeal implements ISubCommand {

    public static CommandHeal instance = new CommandHeal();

    @Override
    public String getCommandName() {
        return "heal";
    }

    public void handleCommand(ICommandSender sender, String[] args) throws PlayerNotFoundException, WrongUsageException {
        switch(args.length){
            case 1:
                if(sender instanceof EntityPlayer) {
                    EntityPlayerMP player = (EntityPlayerMP) sender;
                    boolean CanSend = PermissionsHandler.canUseCommand(Configurations.commands.get("Heal"), player);
                    if (CanSend) {
                        player.setHealth(player.getMaxHealth());
                        sender.addChatMessage(new ChatComponentText("\u00A76Healed."));
                    } else throw new WrongUsageException(StatCollector.translateToLocal("commands.exception.permission"));
                } else throw new WrongUsageException(StatCollector.translateToLocal("commands.exception.playersonly"));
                break;
            case 2:
                EntityPlayerMP player2 = CommandBase.getPlayer(sender, args[1]);
                player2.setHealth(player2.getMaxHealth());
                player2.addChatMessage(new ChatComponentText("\u00A76You have received some divine intervention."));
                sender.addChatMessage(new ChatComponentText("\u00A76Healed " + player2.getDisplayName() + "."));
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
