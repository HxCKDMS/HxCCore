package hxckdms.hxccore.commands;

import hxckdms.hxccore.api.command.AbstractMultiCommand;
import hxckdms.hxccore.api.command.CommandState;
import hxckdms.hxccore.api.command.HxCCommand;
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
    public String getName() {
        return "HxC";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return String.format("/%s help for help", getName());
    }

    @Override
    public void registerSubCommands(FMLPreInitializationEvent event) {
        super.registerSubCommands(event);
        subCommands.values().forEach(subCommand -> commands.computeIfAbsent(subCommand.getName(), key -> new SubCommandConfigHandler(subCommand.getPermissionLevel(), subCommand.getCommandState().isUsageAllowed())));

        commands.forEach((k, v) -> {
            subCommands.get(k.toLowerCase()).setPermissionLevel(v.permissionLevel);
            subCommands.get(k.toLowerCase()).setCommandState(v.enable ? CommandState.ENABLED : CommandState.DISABLED);
        });
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("HxCCore", "HxC", "hxccore", "hxC", "hxc", "Hxc", "HXC");
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        if (args.length == 1) {
            return getListOfStringsMatchingLastWord(args, subCommands.entrySet().parallelStream()
                    .filter(entry -> entry.getValue().getCommandState().isUsageAllowed())
                    .filter(entry -> PermissionHandler.canUseSubCommand(sender, entry.getValue()))
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList()));
        } else if (subCommands.containsKey(args[0])) {
            LinkedList<String> argList = new LinkedList<>(Arrays.asList(args));
            argList.removeFirst();
            //noinspection unchecked
            return subCommands.get(args[0]).getTabCompletions(server, sender, argList, targetPos);
        }
        return super.getTabCompletions(server, sender, args, targetPos);
    }
}
