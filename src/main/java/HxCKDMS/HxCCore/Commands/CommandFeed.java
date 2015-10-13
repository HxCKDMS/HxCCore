package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Configs.CommandsConfig;
import HxCKDMS.HxCCore.Handlers.CommandsHandler;
import HxCKDMS.HxCCore.Handlers.PermissionsHandler;
import HxCKDMS.HxCCore.api.Command.HxCCommand;
import HxCKDMS.HxCCore.api.Command.ISubCommand;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;

import java.util.List;

@HxCCommand(defaultPermission = 2, mainCommand = CommandsHandler.class, isEnabled = true)
public class CommandFeed implements ISubCommand {

    public static CommandFeed instance = new CommandFeed();

    @Override
    public String getCommandName() {
        return "Feed";
    }

    @Override
    public int[] getCommandRequiredParams() {
        return new int[]{0, 1, -1};
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] args, boolean isPlayer) {
        switch(args.length) {
            case 1:
                if (sender instanceof EntityPlayer) {
                    EntityPlayerMP player = (EntityPlayerMP) sender;
                    boolean CanSend = PermissionsHandler.canUseCommand(CommandsConfig.CommandPermissions.get("Feed"), player);
                    if (CanSend) {
                        player.getFoodStats().addStats(20, 20F);
                        player.addChatMessage(new ChatComponentText("\u00A7bYou suddenly feel well fed."));
                    } else
                        sender.addChatMessage(new ChatComponentText("\u00A74You do not have permission to use this command."));
                } else
                    sender.addChatMessage(new ChatComponentText("\u00A74This command without parameters can only be handleCommandd by a player."));
            break;
            case 2:
                EntityPlayerMP player2 = CommandBase.getPlayer(sender, args[1]);
                float plf = player2.getFoodStats().getSaturationLevel() + player2.getFoodStats().getFoodLevel();
                float nf = 40 - plf;
                player2.getFoodStats().addStats(20, 20F);
                player2.addChatMessage(new ChatComponentText("\u00A7bYou suddenly feel well fed."));
                sender.addChatMessage(new ChatComponentText("\u00A7eYou have shoved " + nf + " grams. of food down " + player2.getDisplayName() + "'s\u00A7e throat."));
            break;
            default: throw new WrongUsageException(StatCollector.translateToLocal("commands." + getCommandName().toLowerCase() + ".usage"));
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
