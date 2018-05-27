package hxckdms.hxccore.commands;

import hxckdms.hxccore.api.command.AbstractSubCommand;
import hxckdms.hxccore.api.command.HxCCommand;
import hxckdms.hxccore.utilities.PermissionHandler;
import hxckdms.hxccore.utilities.ServerTranslationHelper;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static hxckdms.hxccore.configs.Configuration.doColorizedHelp;

@HxCCommand
public class CommandHelp extends AbstractSubCommand<CommandHxC> {
    {
        permissionLevel = 1;
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, LinkedList<String> args) throws CommandException {
        HashMap<String, AbstractSubCommand> subCommands = CommandHxC.getSubCommands("hxc");

        A: if (args.size() > 0) {
            String name = subCommands.values().stream().map(command-> command.getName()).filter(iName -> iName.equalsIgnoreCase(args.get(0))).findFirst().orElse("");
            if (name.isEmpty()) break A;
            sender.sendMessage(ServerTranslationHelper.getTranslation(sender, "commands.hxc." + name + ".info").setStyle(new Style().setColor(TextFormatting.AQUA)));
            return;
        }
        LinkedList<String> commandList = subCommands.values().stream()
                .filter(command -> PermissionHandler.canUseSubCommand(sender, command))
                .map(command -> command.getName())
                //.map(String::toLowerCase)
                .sorted(String::compareTo)
                .collect(Collectors.toCollection(LinkedList::new));

        int commandAmount = commandList.size();
        int commandsPerPage = 7;
        int pages = (int) Math.ceil((float) commandAmount / (float) commandsPerPage);

        int page = args.size() == 1 ? CommandBase.parseInt(args.get(0), 1, pages) - 1 : 0;
        int min = Math.min(page * commandsPerPage, commandAmount);

        if (doColorizedHelp)
            sender.sendMessage(ServerTranslationHelper.getTranslation(sender, "commands.help.list.header", page + 1, pages).setStyle(new Style().setColor(TextFormatting.AQUA)));
        else
            sender.sendMessage(ServerTranslationHelper.getTranslation(sender, "commands.help.list.header", page + 1, pages));


        for (int i = page * commandsPerPage; i < commandsPerPage + min; i++) {
            if (i >= commandAmount) break;
            String name = commandList.get(i);
            if (doColorizedHelp)
                sender.sendMessage(ServerTranslationHelper.getTranslation(sender, "commands.hxc." + name + ".usage").setStyle(new Style().setColor(i % 2 == 0 ? TextFormatting.DARK_AQUA : TextFormatting.AQUA)));
            else
                sender.sendMessage(ServerTranslationHelper.getTranslation(sender, "commands.hxc." + name + ".usage"));
        }
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, LinkedList<String> args, @Nullable BlockPos targetPos) {
        return Collections.emptyList();
    }
}
