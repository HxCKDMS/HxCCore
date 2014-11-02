package HxCKDMS.HxCCore.Commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
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
                if(sender instanceof EntityPlayer){
                    EntityPlayerMP player = (EntityPlayerMP) sender;
                    player.setFire(72000);
                }else{
                    sender.addChatMessage(new ChatComponentText("the burn command without arguments can only be executed from a player."));
                }
            }
            break;
            case 2: {
                EntityPlayerMP player2 = CommandBase.getPlayer(sender, args[1]);
                player2.setFire(72000);
            }
            break;
            default: {
                throw new WrongUsageException("Correct usage is: /"+getCommandName()+" [player]");

            }
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        if(args.length == 2){
            return net.minecraft.command.CommandBase.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
        }
        return null;
    }
}
