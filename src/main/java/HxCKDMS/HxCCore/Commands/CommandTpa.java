package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Configs.CommandsConfig;
import HxCKDMS.HxCCore.Configs.Configurations;
import HxCKDMS.HxCCore.Handlers.CommandsHandler;
import HxCKDMS.HxCCore.Handlers.PermissionsHandler;
import HxCKDMS.HxCCore.HxCCore;
import HxCKDMS.HxCCore.api.Command.HxCCommand;
import HxCKDMS.HxCCore.api.Command.ISubCommand;
import HxCKDMS.HxCCore.api.Utils.Teleporter;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import java.util.HashMap;
import java.util.List;

@HxCCommand(defaultPermission = 0, mainCommand = CommandsHandler.class, isEnabled = true)
public class CommandTpa implements ISubCommand {
    public static CommandTpa instance = new CommandTpa();

    @Override
    public String getCommandName() {
        return "TPA";
    }

    @Override
    public int[] getCommandRequiredParams() {
        return new int[]{1, -1, -1};
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] args, boolean isPlayer) throws PlayerNotFoundException, WrongUsageException {
        if (isPlayer) {
            EntityPlayerMP player = (EntityPlayerMP) sender;
            EntityPlayerMP PlayerThatTPs = null;
            boolean CanSend = PermissionsHandler.canUseCommand(CommandsConfig.CommandPermissions.get("TPA"), player);
            if (CanSend) {
                for (HashMap.Entry<EntityPlayerMP, EntityPlayerMP> entry : HxCCore.tpaRequestList.entrySet()) {
                    if(entry.getValue() == player) PlayerThatTPs = entry.getKey();
                }

                switch (args[1]) {
                    case "accept":
                        if (HxCCore.TpaTimeoutList.containsKey(PlayerThatTPs) && PlayerThatTPs != null) {
                            HxCCore.tpaRequestList.remove(PlayerThatTPs);
                            HxCCore.TpaTimeoutList.remove(PlayerThatTPs);
                            player.addChatComponentMessage(new ChatComponentText("You accepted: " + PlayerThatTPs.getDisplayName() + "'s tp request."));
                            PlayerThatTPs.addChatComponentMessage(new ChatComponentText(player.getDisplayName() + " accepted your tp request."));

                            if (PlayerThatTPs.dimension != player.dimension)
                                Teleporter.transferPlayerToDimension(PlayerThatTPs, player.dimension, (int)Math.round(player.posX), (int)Math.round(player.posY), (int)Math.round(player.posZ));
                            else
                                PlayerThatTPs.playerNetServerHandler.setPlayerLocation(player.posX, player.posY, player.posZ, player.rotationYaw, player.rotationPitch);
                        } else {
                            player.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.RED + "There is no tpa request to accept."));
                        }
                        break;
                    case "deny":
                        if (HxCCore.TpaTimeoutList.containsKey(PlayerThatTPs) && PlayerThatTPs != null) {
                            HxCCore.tpaRequestList.remove(PlayerThatTPs);
                            HxCCore.TpaTimeoutList.remove(PlayerThatTPs);
                            player.addChatComponentMessage(new ChatComponentText("You denied: " + PlayerThatTPs.getDisplayName() + "'s tp request."));
                            PlayerThatTPs.addChatComponentMessage(new ChatComponentText(player.getDisplayName() + " denied your tp request."));
                        } else {
                            player.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.RED + "There is no tpa request to deny."));
                        }
                        break;
                    default:
                        EntityPlayerMP player2 = CommandsHandler.getPlayer(sender, args[1]);
                        if (PlayerThatTPs == null) {
                            HxCCore.tpaRequestList.put(player, player2);
                            HxCCore.TpaTimeoutList.put(player, Configurations.TpaTimeout);
                            player2.addChatComponentMessage(new ChatComponentText("Player: " + player.getDisplayName() + " wants to teleport to you."));
                            player.addChatComponentMessage(new ChatComponentText("Teleport request successfully sent to player: " + player2.getDisplayName() + "."));
                        } else {
                            player.addChatComponentMessage(new ChatComponentText("Player is already being tpa'd to."));
                        }
                        break;
                }
            } else throw new WrongUsageException(StatCollector.translateToLocal("commands.exception.permission"));
        } else throw new WrongUsageException(StatCollector.translateToLocal("commands.exception.playersonly"));
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        return CommandBase.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
    }
}
