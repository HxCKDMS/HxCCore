package hxckdms.hxccore.commands;

import hxckdms.hxccore.api.command.AbstractSubCommand;
import hxckdms.hxccore.api.command.HxCCommand;
import hxckdms.hxccore.libraries.GlobalVariables;
import hxckdms.hxccore.utilities.ServerTranslationHelper;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.*;

@HxCCommand
public class CommandSudo extends AbstractSubCommand<CommandHxC> {
    {
        permissionLevel = 4;
    }

    @Override
    public String getCommandName() {
        return "sudo";
    }

    @Override
    public void execute(ICommandSender sender, LinkedList<String> args) throws CommandException {
        EntityPlayerMP target = CommandBase.getPlayer(sender, args.removeFirst());
        boolean force = CommandBase.parseBoolean(sender, args.removeFirst());
        ICommand command = ((Map<String, ICommand>) GlobalVariables.server.getCommandManager().getCommands()).get(args.removeFirst());

        boolean allowed = (force && command.canCommandSenderUseCommand(sender)) || command.canCommandSenderUseCommand(target);

        if (allowed) command.processCommand(target, args.toArray(new String[args.size()]));
        else sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.sudo.target.insufficientPermission", target.getDisplayName(), command.getCommandName()));


    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, LinkedList<String> args) {
        if (args.size() == 1) return CommandBase.getListOfStringsMatchingLastWord(args.toArray(new String[args.size()]), GlobalVariables.server.getAllUsernames());
        else if (args.size() == 2) return Arrays.asList("true", "false");
        else if (args.size() == 3) return new LinkedList<>(GlobalVariables.server.getCommandManager().getCommands().keySet());
        else if (args.size() >= 4) {
            ICommand command = ((Map<String, ICommand>) GlobalVariables.server.getCommandManager().getCommands()).get(args.get(2));
            args.removeFirst(); args.removeFirst(); args.removeFirst();
            return command.addTabCompletionOptions(sender, args.toArray(new String[args.size()]));
        } else return Collections.emptyList();
    }
}
