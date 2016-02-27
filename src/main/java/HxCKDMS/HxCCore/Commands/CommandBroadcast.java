package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Configs.CommandsConfig;
import HxCKDMS.HxCCore.Configs.Configurations;
import HxCKDMS.HxCCore.Handlers.CommandsHandler;
import HxCKDMS.HxCCore.Handlers.NickHandler;
import HxCKDMS.HxCCore.Handlers.PermissionsHandler;
import HxCKDMS.HxCCore.HxCCore;
import HxCKDMS.HxCCore.api.Command.HxCCommand;
import HxCKDMS.HxCCore.api.Command.ISubCommand;
import HxCKDMS.HxCCore.lib.References;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.WrongUsageException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;

import java.util.List;

@SuppressWarnings("unchecked")
@HxCCommand(defaultPermission = 4, mainCommand = CommandsHandler.class, isEnabled = true)
public class CommandBroadcast implements ISubCommand {
    public static CommandBroadcast instance = new CommandBroadcast();

    @Override
    public String getCommandName() {
        return "Broadcast";
    }

    @Override
    public int[] getCommandRequiredParams() {
        return new int[]{1, 1, 1};
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] args, boolean isPlayer) throws WrongUsageException, PlayerNotFoundException{
        if(isPlayer){
            EntityPlayerMP player = (EntityPlayerMP)sender;
            boolean CanSend = PermissionsHandler.canUseCommand(CommandsConfig.CommandPermissions.get("Broadcast"), player);
            String tmp = "";
            for (int i = 1; i < args.length; i++)
                tmp = tmp + " " + args[i];

            tmp = tmp.trim();
            if (CanSend && !tmp.isEmpty()) {
                String formattedMessage = Configurations.formats.get("BroadcastVariable").replace("SENDER", NickHandler.getColouredNick(player)).replace("MESSAGE", tmp).replaceAll("&", References.CC);
                for (EntityPlayer p : (List<EntityPlayer>) HxCCore.server.getEntityWorld().playerEntities)
                    p.addChatComponentMessage(new ChatComponentText(formattedMessage));
            }
        } else {
            String tmp = "";
            for (int i = 1; i < args.length; i++)
                tmp = tmp + " " + args[i];

            tmp = tmp.trim();
            if (!tmp.isEmpty()) {
                String formattedMessage = Configurations.formats.get("BroadcastVariable").replace("SENDER", "").replace("MESSAGE", tmp).replaceAll("&", References.CC);
                for (EntityPlayer p : (List<EntityPlayer>) HxCCore.server.getEntityWorld().playerEntities)
                    p.addChatComponentMessage(new ChatComponentText(formattedMessage));
            }
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        return null;
    }
}
