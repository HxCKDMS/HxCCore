package hxckdms.hxccore.utilities;

import hxckdms.hxccore.api.command.AbstractSubCommand;
import hxckdms.hxccore.libraries.GlobalVariables;
import hxckdms.hxccore.registry.CommandRegistry;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

public class PermissionHandler {
    public static boolean canUseCommand(ICommandSender sender, ICommand command) {
        int permLevel = command instanceof CommandBase ? ((CommandBase) command).getRequiredPermissionLevel() : 4;
        Optional<Map.Entry<String, Integer>> commandOptional = CommandRegistry.CommandConfig.vanillaPermissionOverride.entrySet().parallelStream().filter(entry -> entry.getKey().toLowerCase().equals(command.getCommandName().toLowerCase())).findAny();
        return getPermissionLevel(sender) == 5 || (commandOptional.isPresent() ? getPermissionLevel(sender) >= commandOptional.get().getValue() : sender.canCommandSenderUseCommand(permLevel, command.getCommandName()));
    }

    public static boolean canUseSubCommand(ICommandSender sender, AbstractSubCommand subCommand) {
        return (FMLCommonHandler.instance().getSide() == Side.CLIENT && sender.getEntityWorld().getWorldInfo().areCommandsAllowed())  || getPermissionLevel(sender) == - 1 || getPermissionLevel(sender) >= subCommand.getPermissionLevel();
    }

    public static int getPermissionLevel(ICommandSender sender) {
        if (sender instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) sender;
            return Arrays.asList(player.mcServer.getPlayerList().getOppedPlayerNames()).contains(player.getName()) ? 5 : GlobalVariables.permissionData.getInteger(player.getUniqueID().toString());
        } else {
            return 5;
        }
    }
}
