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

public class CommandExtinguish implements ISubCommand {
    public static CommandExtinguish instance = new CommandExtinguish();

    @Override
    public String getName() {
        return "extinguish";
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 4;
    }

    @Override
    public void execute(ICommandSender sender, String[] args) throws CommandException {
        switch(args.length){
            case 1: {
                if(sender instanceof EntityPlayer){
                    EntityPlayerMP player = (EntityPlayerMP) sender;
                    player.extinguish();
                }else{
                    sender.addChatMessage(new ChatComponentText("the extinguish command without arguments can only be executed from a player."));
                }
            }
            break;
            case 2: {
                EntityPlayerMP player2 = CommandBase.getPlayer(sender, args[1]);
                player2.extinguish();
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
            return net.minecraft.command.CommandBase.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
        }
        return null;
    }
}
