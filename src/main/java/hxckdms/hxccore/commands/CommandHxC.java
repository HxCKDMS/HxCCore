package hxckdms.hxccore.commands;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import hxckdms.hxccore.api.command.AbstractMultiCommand;
import hxckdms.hxccore.api.command.CommandState;
import hxckdms.hxccore.api.command.HxCCommand;
import hxckdms.hxccore.api.command.SubCommandConfigHandler;
import hxckdms.hxccore.utilities.PermissionHandler;
import net.minecraft.command.ICommandSender;

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
        subCommands.values().forEach(subCommand -> commands.computeIfAbsent(subCommand.getCommandName(), key -> new SubCommandConfigHandler(subCommand.getPermissionLevel(), subCommand.getCommandState().isUsageAllowed())));

        commands.forEach((k, v) -> {
            subCommands.get(k.toLowerCase()).setPermissionLevel(v.permissionLevel);
            subCommands.get(k.toLowerCase()).setCommandState(v.enable ? CommandState.ENABLED : CommandState.DISABLED);
        });
    }

    @Override
    public List<String> getCommandAliases() {
        return Arrays.asList("HxCCore", "HxC", "hxccore", "hxC", "hxc", "Hxc", "HXC");
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        if (args.length == 1) {
            return getListOfStringsFromIterableMatchingLastWord(args, subCommands.entrySet().parallelStream().filter(entry -> entry.getValue().getCommandState().isUsageAllowed()).filter(entry -> PermissionHandler.canUseSubCommand(sender, entry.getValue())).map(Map.Entry::getKey).collect(Collectors.toList()));
        } else if (subCommands.containsKey(args[0])) {
            LinkedList<String> lArgs = new LinkedList<>(Arrays.asList(args));
            lArgs.removeFirst();
            return subCommands.get(args[0]).addTabCompletionOptions(sender, lArgs);
        }
        return super.addTabCompletionOptions(sender, args);
    }
}
