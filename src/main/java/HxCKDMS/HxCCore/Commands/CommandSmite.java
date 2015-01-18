package HxCKDMS.HxCCore.Commands;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;

import java.util.List;

public class CommandSmite implements ISubCommand {
    public static CommandSmite instance = new CommandSmite();

    @Override
    public String getCommandName() {
        return "smite";
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] args) {
        if(sender instanceof EntityPlayerMP){
            EntityPlayerMP player = (EntityPlayerMP) sender;
            player.worldObj.spawnEntityInWorld(new EntityLightningBolt(player.worldObj, player.posX+5, player.posY, player.posZ+5));
        }else{
            sender.addChatMessage(new ChatComponentText("\u00A74This command can only be executed by a player."));
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        return null;
    }
}
