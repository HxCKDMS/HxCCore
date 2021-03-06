package hxckdms.hxccore.commands;

import hxckdms.hxccore.api.command.AbstractMultiCommand;
import hxckdms.hxccore.api.command.HxCCommand;
import hxckdms.hxccore.api.command.CommandState;
import hxckdms.hxccore.api.command.SubCommandConfigHandler;
import hxckdms.hxccore.utilities.PermissionHandler;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static hxckdms.hxccore.registry.CommandRegistry.CommandConfig.commands;

@HxCCommand
public class CommandHxC extends AbstractMultiCommand {
    @Override
    public String getCommandName() {
        return "HxC";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return String.format("/%s help for help", getCommandName());
    }

    @Override
    public void registerSubCommands(FMLPreInitializationEvent event) {
        super.registerSubCommands(event);
        subCommands.values().forEach(subCommand -> commands.computeIfAbsent(subCommand.getName(), key -> new SubCommandConfigHandler(subCommand.getPermissionLevel(), subCommand.getState().isUsageAllowed())));

        commands.forEach((k, v) -> {
            subCommands.get(k.toLowerCase()).setPermissionLevel(v.permissionLevel);
            subCommands.get(k.toLowerCase()).setState(v.enable ? CommandState.ENABLED : CommandState.DISABLED);
        });
    }

    @Override
    public List<String> getCommandAliases() {
        return Arrays.asList("HxCCore", "HxC", "hxccore", "hxC", "hxc", "Hxc", "HXC");
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
        if (args.length == 1) {
            return getListOfStringsMatchingLastWord(args, subCommands.entrySet().parallelStream().filter(entry -> entry.getValue().getState().isUsageAllowed()).filter(entry -> PermissionHandler.canUseSubCommand(sender, entry.getValue())).map(Map.Entry::getKey).collect(Collectors.toList()));
        } else if (subCommands.containsKey(args[0])) {
            LinkedList<String> lArgs = new LinkedList<>(Arrays.asList(args));
            lArgs.removeFirst();
            return subCommands.get(args[0]).addTabCompletionOptions(sender, lArgs, pos);
        }
        return super.getTabCompletionOptions(server, sender, args, pos);
    }
}
