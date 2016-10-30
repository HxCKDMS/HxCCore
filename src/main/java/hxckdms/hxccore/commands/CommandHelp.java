package hxckdms.hxccore.commands;

import hxckdms.hxccore.api.command.AbstractSubCommand;
import hxckdms.hxccore.api.command.HxCCommand;
import hxckdms.hxccore.utilities.PermissionHandler;
import hxckdms.hxccore.utilities.ServerTranslationHelper;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

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
            sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.hxc." + name + ".info").setStyle(new Style().setColor(TextFormatting.AQUA)));
            return;
        }

        int modAmount = subCommands.keySet().size();
        int modsPerPage = 7;
        int pages = (int) Math.ceil((float) modAmount / (float) modsPerPage);

        int page = args.size() == 1 ? CommandBase.parseInt(args.get(0), 1, pages) - 1 : 0;
        int min = Math.min(page * modsPerPage, modAmount);

        sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.warp.list.header", page + 1, pages).setStyle(new Style().setColor(TextFormatting.AQUA)));
        LinkedList<String> commandList = new LinkedList<>(subCommands.values().stream().filter(command -> PermissionHandler.canUseSubCommand(sender, command)).map(command -> command.getCommandName()).sorted(String::compareTo).collect(Collectors.toList()));

        for (int i = page * modsPerPage; i < modsPerPage + min; i++) {
            if (i >= modAmount) break;
            String name = commandList.get(i);
            sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.hxc." + name + ".usage").setStyle(new Style().setColor(i % 2 == 0 ? TextFormatting.DARK_AQUA : TextFormatting.AQUA)));
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, LinkedList<String> args, @Nullable BlockPos pos) {
        return Collections.emptyList();
    }
}
