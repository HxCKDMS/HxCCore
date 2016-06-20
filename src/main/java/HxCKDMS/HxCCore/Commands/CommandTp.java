package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Configs.CommandsConfig;
import HxCKDMS.HxCCore.Handlers.CommandsHandler;
import HxCKDMS.HxCCore.Handlers.PermissionsHandler;
import HxCKDMS.HxCCore.HxCCore;
import HxCKDMS.HxCCore.api.Command.HxCCommand;
import HxCKDMS.HxCCore.api.Command.ISubCommand;
import HxCKDMS.HxCCore.api.Utils.Teleporter;
import HxCKDMS.HxCCore.lib.References;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;

import java.util.List;

@HxCCommand(defaultPermission = 0, mainCommand = CommandsHandler.class, isEnabled = true)
public class CommandTp implements ISubCommand {
    public static CommandTp instance = new CommandTp();

    @Override
    public String getCommandName() {
        return "TP";
    }

    @Override
    public int[] getCommandRequiredParams() {
        return new int[]{1, -1, -1};
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] args, boolean isPlayer) throws PlayerNotFoundException, WrongUsageException {
        if (isPlayer) {
            EntityPlayerMP player;
            if (args.length == 2)
                player = (EntityPlayerMP) sender;
            else
                player = CommandsHandler.getPlayer(sender, args[2]);

            boolean CanSend = PermissionsHandler.canUseCommand(CommandsConfig.CommandPermissions.get(getCommandName()), player);
            if (CanSend) {
                EntityPlayerMP player2 = CommandsHandler.getPlayer(sender, args[1]);
                if (player2.dimension != player.dimension)
                    Teleporter.transferPlayerToDimension(player, player2.dimension, Math.round((float)player2.posX), Math.round((float)player2.posY), Math.round((float)player2.posZ));
                else
                    player.playerNetServerHandler.setPlayerLocation(Math.round((float)player2.posX), Math.round((float)player2.posY), Math.round((float)player2.posZ), player2.rotationYaw, player2.rotationPitch);
                player.addChatComponentMessage(new ChatComponentText("Teleported successfully to player: " + player2.getDisplayName() + "."));
            } else throw new WrongUsageException(HxCCore.util.readLangOnServer(References.MOD_ID, "commands.exception.permission"));
        } else throw new WrongUsageException(HxCCore.util.readLangOnServer(References.MOD_ID, "commands.exception.playersonly"));
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        return CommandBase.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
    }
}
