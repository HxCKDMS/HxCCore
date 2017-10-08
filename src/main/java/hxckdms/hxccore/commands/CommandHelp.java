package hxckdms.hxccore.commands;

import hxckdms.hxccore.api.command.AbstractSubCommand;
import hxckdms.hxccore.api.command.HxCCommand;
import hxckdms.hxccore.utilities.PermissionHandler;
import hxckdms.hxccore.utilities.ServerTranslationHelper;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

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
    public String getCommandName() {
        return "help";
    }

    @Override
    public void execute(ICommandSender sender, LinkedList<String> args) throws CommandException {
        HashMap<String, AbstractSubCommand> subCommands = CommandHxC.getSubCommands("hxc");

        A: if (args.size() > 0) {
            String name = subCommands.values().stream().map(command-> command.getCommandName()).filter(iName -> iName.equalsIgnoreCase(args.get(0))).findFirst().orElse("");
            if (name.isEmpty()) break A;
            sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.hxc." + name + ".info").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.AQUA)));
            return;
        }
        LinkedList<String> commandList = new LinkedList<>(subCommands.values().stream().filter(command -> PermissionHandler.canUseSubCommand(sender, command)).map(command -> command.getCommandName()).sorted(String::compareTo).collect(Collectors.toList()));

        int commandAmount = commandList.size();
        int commandsPerPage = 7;
        int pages = (int) Math.ceil((float) commandAmount / (float) commandsPerPage);

        int page = args.size() == 1 ? CommandBase.parseIntBounded(sender, args.get(0), 1, pages) - 1 : 0;
        int min = Math.min(page * commandsPerPage, commandAmount);

        if (doColorizedHelp)
            sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.help.list.header", page + 1, pages).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.AQUA)));
        else
            sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.help.list.header", page + 1, pages));


        for (int i = page * commandsPerPage; i < commandsPerPage + min; i++) {
            if (i >= commandAmount) break;
            String name = commandList.get(i);
            if (doColorizedHelp)
                sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.hxc." + name + ".usage").setChatStyle(new ChatStyle().setColor(i % 2 == 0 ? EnumChatFormatting.DARK_AQUA : EnumChatFormatting.AQUA)));
            else
                sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.hxc." + name + ".usage"));
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, LinkedList<String> args) {
        return Collections.emptyList();
    }
}
