package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Configs.CommandsConfig;
import HxCKDMS.HxCCore.HxCCore;
import HxCKDMS.HxCCore.api.Command.HxCCommand;
import HxCKDMS.HxCCore.api.Command.ISubCommand;
import HxCKDMS.HxCCore.api.Handlers.CommandsHandler;
import HxCKDMS.HxCCore.api.Handlers.NBTFileIO;
import HxCKDMS.HxCCore.api.Handlers.PermissionsHandler;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;

import java.io.File;
import java.util.List;

@HxCCommand(defaultPermission = 1, mainCommand = CommandsHandler.class, isEnabled = true)
public class CommandFly implements ISubCommand {
    public static CommandFly instance = new CommandFly();

    @Override
    public String getCommandName() {
        return "Fly";
    }

    @Override
    public int[] getCommandRequiredParams() {
        return new int[]{0, 1, -1};
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] args, boolean isPlayer) {
        switch(args.length){
            case 1:
                if (isPlayer) {
                    EntityPlayerMP player = (EntityPlayerMP) sender;
                    boolean CanSend = PermissionsHandler.canUseCommand(CommandsConfig.CommandPermissions.get(getCommandName()), player);
                    if (CanSend) {
                        player.addChatMessage(new ChatComponentText("\u00A76" + toggleFlightForPlayer(player) + " flight."));
                    } else throw new WrongUsageException(HxCCore.util.getTranslation((isPlayer ? ((EntityPlayerMP) sender).getUniqueID() : java.util.UUID.randomUUID()), "commands.exception.permission"));
                } else throw new WrongUsageException(HxCCore.util.getTranslation((isPlayer ? ((EntityPlayerMP) sender).getUniqueID() : java.util.UUID.randomUUID()), "commands.exception.playersonly"));
            break;
            case 2:
                if (isPlayer) {
                    EntityPlayerMP player = (EntityPlayerMP) sender;
                    boolean CanSend = PermissionsHandler.canUseCommand(CommandsConfig.CommandPermissions.get(getCommandName()), player);
                    if (CanSend) {
                        EntityPlayerMP player2 = CommandBase.getPlayer(sender, args[1]);
                        player.addChatMessage(new ChatComponentText("\u00A76" + toggleFlightForPlayer(player2) + " flight, for player " + player2.getDisplayName() + "."));
                        player2.addChatMessage(new ChatComponentText(player2.capabilities.allowFlying ? "\u00A7bYou feel like the wind can carry you." : "\u00A7bYou feel like an unmovable boulder."));
                    } else throw new WrongUsageException(HxCCore.util.getTranslation((isPlayer ? ((EntityPlayerMP) sender).getUniqueID() : java.util.UUID.randomUUID()), "commands.exception.permission"));
                } else {
                    EntityPlayerMP player2 = CommandBase.getPlayer(sender, args[1]);
                    sender.addChatMessage(new ChatComponentText((toggleFlightForPlayer(player2) + " flight, for player " + player2.getDisplayName() + ".")));
                    player2.addChatMessage(new ChatComponentText(player2.capabilities.allowFlying ? "\u00A7bYou feel like the wind can carry you." : "\u00A7bYou feel like an unmovable boulder."));
                }
                break;
            default: throw new WrongUsageException(HxCCore.util.getTranslation((isPlayer ? ((EntityPlayerMP) sender).getUniqueID() : java.util.UUID.randomUUID()), "commands." + getCommandName().toLowerCase() + ".usage"));
        }
    }

    public String toggleFlightForPlayer(EntityPlayerMP player) {
        String UUID = player.getUniqueID().toString();
        File CustomPlayerData = new File(HxCCore.HxCCoreDir, "HxC-" + UUID + ".dat");
        NBTFileIO.setBoolean(CustomPlayerData, "fly", !player.capabilities.allowFlying);
        player.capabilities.allowFlying = !player.capabilities.allowFlying;
        player.capabilities.isFlying = !player.capabilities.isFlying;
        player.sendPlayerAbilities();
        return player.capabilities.allowFlying ? "Enabled" : "Disabled";
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        if (args.length == 2)
            return CommandBase.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
        return null;
    }
}
