package hxckdms.hxccore.commands;

import hxckdms.hxccore.api.command.AbstractSubCommand;
import hxckdms.hxccore.api.command.HxCCommand;
import hxckdms.hxccore.utilities.ServerTranslationHelper;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.*;

@HxCCommand
public class CommandSudo extends AbstractSubCommand<CommandHxC> {
    {
        permissionLevel = 4;
    }

    @Override
    public String getName() {
        return "sudo";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, LinkedList<String> args) throws CommandException {
        EntityPlayerMP target = CommandBase.getPlayer(server, sender, args.removeFirst());
        boolean force = CommandBase.parseBoolean(args.removeFirst());
        ICommand command = server.getCommandManager().getCommands().get(args.removeFirst());

        boolean allowed = force || command.checkPermission(server, target);

        if (allowed)
            command.execute(server, target, args.toArray(new String[0]));
        else sender.sendMessage(ServerTranslationHelper.getTranslation(sender, "commands.sudo.target.insufficientPermission", target.getDisplayName(), command.getName()));


    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, LinkedList<String> args, @Nullable BlockPos targetPos) {
        if (args.size() == 1) return CommandBase.getListOfStringsMatchingLastWord(args.toArray(new String[0]), server.getOnlinePlayerNames());
        else if (args.size() == 2) return Arrays.asList("true", "false");
        else if (args.size() == 3) return new LinkedList<>(server.getCommandManager().getCommands().keySet());
        else if (args.size() >= 4) {
            ICommand command = server.getCommandManager().getCommands().get(args.get(2));
            args.removeFirst(); args.removeFirst(); args.removeFirst();
            return command.getTabCompletions(server, sender, args.toArray(new String[0]), targetPos);
        } else return Collections.emptyList();
    }
}
