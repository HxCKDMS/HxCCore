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
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

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
        EntityPlayerMP target = CommandBase.getPlayer(GlobalVariables.server, sender, args.removeFirst());
        boolean force = CommandBase.parseBoolean(args.removeFirst());
        ICommand command = GlobalVariables.server.getCommandManager().getCommands().get(args.removeFirst());

        boolean allowed = (force && command.checkPermission(GlobalVariables.server, sender)) || command.checkPermission(GlobalVariables.server, target);

        if (allowed) command.execute(GlobalVariables.server, target, args.toArray(new String[args.size()]));
        else sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.sudo.target.insufficientPermission", target.getDisplayName(), command.getCommandName()));


    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, LinkedList<String> args, @Nullable BlockPos pos) {
        if (args.size() == 1) return CommandBase.getListOfStringsMatchingLastWord(args.toArray(new String[args.size()]), GlobalVariables.server.getAllUsernames());
        else if (args.size() == 2) return Arrays.asList("true", "false");
        else if (args.size() == 3) return new LinkedList<>(GlobalVariables.server.getCommandManager().getCommands().keySet());
        else if (args.size() >= 4) {
            ICommand command = GlobalVariables.server.getCommandManager().getCommands().get(args.get(2));
            args.removeFirst(); args.removeFirst(); args.removeFirst();
            return command.getTabCompletionOptions(GlobalVariables.server, sender, args.toArray(new String[args.size()]), pos);
        } else return Collections.emptyList();
    }
}
