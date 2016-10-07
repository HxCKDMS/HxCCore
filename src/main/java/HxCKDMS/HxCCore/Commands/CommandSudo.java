package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Configs.CommandsConfig;
import HxCKDMS.HxCCore.HxCCore;
import HxCKDMS.HxCCore.api.Command.HxCCommand;
import HxCKDMS.HxCCore.api.Command.ISubCommand;
import HxCKDMS.HxCCore.api.Handlers.CommandsHandler;
import HxCKDMS.HxCCore.api.Handlers.PermissionsHandler;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;

import java.util.List;

@HxCCommand(defaultPermission = 4, mainCommand = CommandsHandler.class, isEnabled = true)
public class CommandSudo implements ISubCommand {
    public static CommandSudo instance = new CommandSudo();

    @Override
    public String getCommandName() {
        return "Sudo";
    }

    @Override
    public int[] getCommandRequiredParams() {
        return new int[]{2, 2, 2};
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] args, boolean isPlayer) {
        if (isPlayer) {
            EntityPlayerMP player = (EntityPlayerMP)sender;
            boolean CanSend = PermissionsHandler.canUseCommand(CommandsConfig.CommandPermissions.get(getCommandName()), player);
            if (CanSend) {
                EntityPlayerMP target = CommandsHandler.getPlayer(sender, args[1]);
                String arr = "";
                for (int i = 2; i < args.length; i++) {
                    arr = arr + args[i] + " ";
                }
                HxCCore.server.getCommandManager().executeCommand(target, arr.trim());
            } else player.addChatMessage(new ChatComponentText("Insufficient Permissions."));
        } else {
            EntityPlayerMP target = CommandsHandler.getPlayer(sender, args[1]);
            String arr = "";
            for (int i = 2; i < args.length; i++) {
                arr = arr + args[i] + " ";
            }
            HxCCore.server.getCommandManager().executeCommand(target, arr.trim());
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        return null;
    }
}
