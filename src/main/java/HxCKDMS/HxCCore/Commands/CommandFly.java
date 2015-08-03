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

public class CommandFly implements ISubCommand {
    public static CommandFly instance = new CommandFly();

    @Override
    public String getName() {
        return "fly";
    }

    @Override
    public void execute(ICommandSender sender, String[] args) throws WrongUsageException, PlayerNotFoundException {
        switch(args.length){
            case 1:
                if(sender instanceof EntityPlayerMP){
                    EntityPlayerMP player = (EntityPlayerMP) sender;
                    boolean CanSend = PermissionsHandler.canUseCommand(Configurations.commands.get("Fly"), player);
                    if (CanSend) {
                        player.capabilities.allowFlying = !player.capabilities.allowFlying;
                        player.capabilities.isFlying = !player.capabilities.isFlying;
                        player.sendPlayerAbilities();
                        player.addChatComponentMessage(new ChatComponentText((player.capabilities.allowFlying ? "\u00A76Enabled" : "\u00A76Disabled")+" flight."));
                    } else sender.addChatMessage(new ChatComponentText("\u00A74You do not have permission to use this command."));
                } else sender.addChatMessage(new ChatComponentText("\u00A74This command without parameters can only be executed by a player."));
            break;
            case 2:
                EntityPlayerMP player = (EntityPlayerMP) sender;
                EntityPlayerMP player2 = CommandBase.getPlayer(sender, args[1]);
                player2.capabilities.allowFlying = !player2.capabilities.allowFlying;
                player2.capabilities.isFlying = !player2.capabilities.isFlying;
                player2.sendPlayerAbilities();
                player2.addChatMessage(new ChatComponentText(player2.capabilities.allowFlying ? "\u00A7bYou feel lighter." : "\u00A7bYou feel heavier."));
                player.addChatComponentMessage(new ChatComponentText((player2.capabilities.allowFlying ? "\u00A76Enabled" : "\u00A76Disabled") + " flight, for player " + player2.getDisplayName() + "."));
            break;
            default: throw new WrongUsageException("Correct usage is: /"+getName()+" [player]");
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
