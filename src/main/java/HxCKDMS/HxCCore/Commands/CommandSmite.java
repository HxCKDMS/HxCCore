package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Configs.Configurations;
import HxCKDMS.HxCCore.Handlers.PermissionsHandler;
import HxCKDMS.HxCCore.api.ISubCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;

import java.util.List;

public class CommandSmite implements ISubCommand {
    public static CommandSmite instance = new CommandSmite();

    @Override
    public String getCommandName() {
        return "smite";
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] args) throws PlayerNotFoundException {
        if(sender instanceof EntityPlayerMP){
            EntityPlayerMP player = (EntityPlayerMP) sender;
            boolean CanSend = PermissionsHandler.canUseCommand(Configurations.PermLevels.get(14), player);
            if (CanSend) {
                if (args.length == 2) smite(CommandBase.getPlayer(sender, args[1]));
                else smite(player);
            } else sender.addChatMessage(new ChatComponentText("\u00A74You do not have permission to use this command."));
        } else {
            if (args.length == 2) smite(CommandBase.getPlayer(sender, args[1]));
            else sender.addChatMessage(new ChatComponentText("\u00A74This command without parameters can only be executed by a player."));
        }
    }

    public void smite(EntityPlayer target) {
        target.worldObj.spawnEntityInWorld(new EntityLightningBolt(target.worldObj, target.posX, target.posY+100, target.posZ));
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        if(args.length == 2){
            return net.minecraft.command.CommandBase.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
        }
        return null;
    }
}
