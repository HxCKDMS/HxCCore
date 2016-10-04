package hxckdms.hxccore.commands;

import hxckdms.hxccore.api.command.ISubCommand;
import hxckdms.hxccore.libraries.GlobalVariables;
import hxckdms.hxccore.registry.command.CommandRegistry;
import hxckdms.hxccore.registry.command.HxCCommand;
import hxckdms.hxccore.utilities.HxCPlayerInfoHandler;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@HxCCommand(defaultPermission = 1, mainCommand = CommandRegistry.CommandHxC.class, isEnabled = true)
public class CommandNick implements ISubCommand {
    @Override
    public String getCommandName() {
        return "nick";
    }

    @Override
    public int[] getCommandRequiredParams() {
        return new int[0];
    }

    @Override
    public void handleCommand(ICommandSender sender, LinkedList<String> args, boolean isPlayer) throws CommandException {
        if (args.size() == 0 && sender instanceof EntityPlayerMP) {
            HxCPlayerInfoHandler.setString((EntityPlayerMP) sender, "NickName", "");
        } else {
            if (Arrays.asList(GlobalVariables.server.getPlayerList().getAllUsernames()).contains(args.get(0))) {
                EntityPlayerMP target = CommandBase.getPlayer(GlobalVariables.server, sender, args.removeFirst());
                String nick = args.stream().collect(Collectors.joining(" "));

                HxCPlayerInfoHandler.setString(target, "NickName", nick);
            } else if(sender instanceof EntityPlayerMP) {
                String nick = args.stream().collect(Collectors.joining(" "));

                HxCPlayerInfoHandler.setString((EntityPlayerMP) sender, "NickName", nick);
            }
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        return null;
    }
}
