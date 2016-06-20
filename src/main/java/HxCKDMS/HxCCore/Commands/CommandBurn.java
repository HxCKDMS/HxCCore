package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Configs.CommandsConfig;
import HxCKDMS.HxCCore.api.Handlers.CommandsHandler;
import HxCKDMS.HxCCore.api.Handlers.PermissionsHandler;
import HxCKDMS.HxCCore.HxCCore;
import HxCKDMS.HxCCore.api.Command.HxCCommand;
import HxCKDMS.HxCCore.api.Command.ISubCommand;
import HxCKDMS.HxCCore.lib.References;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;

import java.util.List;

@SuppressWarnings("unchecked")
@HxCCommand(defaultPermission = 3, mainCommand = CommandsHandler.class, isEnabled = true)
public class CommandBurn implements ISubCommand {
    public static CommandBurn instance = new CommandBurn();

    @Override
    public String getCommandName() {
        return "Burn";
    }

    @Override
    public int[] getCommandRequiredParams() {
        return new int[]{0, 1, 1};
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] args, boolean isPlayer) {
        boolean CanSend = !isPlayer || PermissionsHandler.canUseCommand(CommandsConfig.CommandPermissions.get(getCommandName()), (EntityPlayerMP) sender);
        if (CanSend) {
            switch(args.length) {
                case 1:
                    if (isPlayer) {
                        EntityPlayerMP player = (EntityPlayerMP)sender;
                        player.addChatMessage(new ChatComponentText("\u00A79You suddenly feel warmer."));
                        player.setFire(750000000);
                    } else throw new WrongUsageException(HxCCore.util.readLangOnServer(References.MOD_ID, "commands.exception.playersonly"));
                    break;
                case 2:
                    EntityPlayerMP player2 = net.minecraft.command.CommandBase.getPlayer(sender, args[1]);
                    player2.addChatMessage(new ChatComponentText("\u00A79You suddenly feel warmer."));
                    player2.setFire(750000000);
                    sender.addChatMessage(new ChatComponentText(player2.getDisplayName() + " \u00A74has been set on fire for 750000000 ticks."));
                    break;
                case 3:
                    player2 = net.minecraft.command.CommandBase.getPlayer(sender, args[1]);
                    player2.addChatMessage(new ChatComponentText("\u00A79You suddenly feel warmer."));
                    player2.setFire(Integer.parseInt(args[2]));
                    sender.addChatMessage(new ChatComponentText(player2.getDisplayName() + " \u00A74has been set on fire for " + Integer.parseInt(args[2]) + " ticks."));
                    break;
                default: throw new WrongUsageException(HxCCore.util.readLangOnServer(References.MOD_ID, "commands." + getCommandName().toLowerCase() + ".usage"));
            }
        } else throw new WrongUsageException(HxCCore.util.readLangOnServer(References.MOD_ID, "commands.exception.permission"));
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        if(args.length == 2)
            return CommandBase.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
        return null;
    }
}
