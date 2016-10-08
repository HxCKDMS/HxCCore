package hxckdms.hxccore.utilities;

import hxckdms.hxccore.api.command.ISubCommand;
import hxckdms.hxccore.libraries.GlobalVariables;
import hxckdms.hxccore.registry.command.CommandRegistry;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import static hxckdms.hxccore.registry.command.CommandRegistry.CommandConfig.commands;

public class PermissionHandler {
    public static boolean canUseCommand(ICommandSender sender, ICommand command) {
        Optional<Map.Entry<String, Integer>> commandOptional = CommandRegistry.CommandConfig.vanillaPermissionOverride.entrySet().parallelStream().filter(entry -> entry.getKey().toLowerCase().equals(command.getCommandName().toLowerCase())).findAny();
        return commandOptional.isPresent() ? getPermissionLevel(sender) >= commandOptional.get().getValue() : command.checkPermission(GlobalVariables.server, sender);
    }

    public static boolean canUseSubCommand(ICommandSender sender, ISubCommand command) {
        return getPermissionLevel(sender) == - 1 || getPermissionLevel(sender) >= commands.get(command.getCommandName()).permissionLevel;
    }

    public static int getPermissionLevel(ICommandSender sender) {
        if (sender instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) sender;
            return Arrays.asList(player.mcServer.getPlayerList().getOppedPlayerNames()).contains(player.getName()) ? -1 : GlobalVariables.permissionData.getInteger(player.getUniqueID().toString());
        } else {
            return -1;
        }
    }
}
