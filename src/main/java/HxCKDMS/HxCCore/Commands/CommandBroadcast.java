package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Configs.Configurations;
import HxCKDMS.HxCCore.Handlers.NickHandler;
import HxCKDMS.HxCCore.Handlers.PermissionsHandler;
import HxCKDMS.HxCCore.api.ISubCommand;
import HxCKDMS.HxCCore.lib.References;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;

import java.util.List;

@SuppressWarnings("unchecked")
public class CommandBroadcast implements ISubCommand {
    public static CommandBroadcast instance = new CommandBroadcast();

    @Override
    public String getCommandName() {
        return "broadcast";
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] args) throws WrongUsageException {
        if(sender instanceof EntityPlayerMP){
            EntityPlayerMP player = (EntityPlayerMP)sender;
            boolean CanSend = PermissionsHandler.canUseCommand(Configurations.commands.get("Broadcast"), player);
            String tmp = "";
            for (int i = 1; i < args.length; i++) {
                tmp = tmp + " " + args[i];
            }
            if (CanSend) {
                String formattedMessage = Configurations.formats.get("BroadcastVariable").replace("SENDER", NickHandler.getColouredNick(player)).replace("MESSAGE", tmp).replaceAll("&", References.CC);
                for (EntityPlayer p : (List<EntityPlayer>)player.worldObj.playerEntities)
                    p.addChatComponentMessage(new ChatComponentText(formattedMessage));
            }
        } else throw new WrongUsageException(StatCollector.translateToLocal("command.exception.playersonly"));
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        return null;
    }
}
