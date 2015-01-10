package HxCKDMS.HxCCore.Commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
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
    public void execute(ICommandSender sender, String[] args) throws CommandException {
        switch(args.length){
            case 1: {
                if(sender instanceof EntityPlayer){
                    EntityPlayerMP player = (EntityPlayerMP) sender;
                    player.capabilities.allowFlying = !player.capabilities.allowFlying;
                    player.capabilities.isFlying = !player.capabilities.isFlying;
                    player.sendPlayerAbilities();
                    player.addChatComponentMessage(new ChatComponentText("turned "+ (player.capabilities.allowFlying ? "on" : "off")+" flight"));
                }else{
                    sender.addChatMessage(new ChatComponentText("the fly command without arguments can only be executed from a player."));
                }
            }
            break;
            case 2: {
                EntityPlayerMP player = (EntityPlayerMP) sender;
                EntityPlayerMP player2 = CommandBase.getPlayer(sender, args[1]);
                player2.capabilities.allowFlying = !player2.capabilities.allowFlying;
                player2.capabilities.isFlying = !player2.capabilities.isFlying;
                player2.sendPlayerAbilities();
                player.addChatComponentMessage(new ChatComponentText("turned "+ (player2.capabilities.allowFlying ? "on" : "off")+" flight, for player "+args[1]));

            }
            break;
            default: {
                throw new WrongUsageException("Correct usage is: /"+getName()+" [player]");

            }
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        if(args.length == 2){
            return CommandBase.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
        }
        return null;
    }
}
