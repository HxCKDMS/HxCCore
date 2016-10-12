package hxckdms.hxccore.commands;

import hxckdms.hxccore.api.command.*;
import hxckdms.hxccore.libraries.GlobalVariables;
import hxckdms.hxccore.utilities.ColorHelper;
import hxckdms.hxccore.utilities.HxCPlayerInfoHandler;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@HxCCommand
public class CommandNick extends AbstractSubCommand {
    {
        permissionLevel = 1;
        state = CommandState.ENABLED;
    }

    @Override
    public String getCommandName() {
        return "nick";
    }

    @Override
    public void execute(ICommandSender sender, LinkedList<String> args) throws CommandException {
        if (args.size() == 0 && sender instanceof EntityPlayerMP) {
            HxCPlayerInfoHandler.setString((EntityPlayerMP) sender, "NickName", "");

            TextComponentTranslation msg = new TextComponentTranslation("commands.nick.removed.self");
            msg.getStyle().setColor(TextFormatting.YELLOW);
            sender.addChatMessage(msg);
        } else {
            if (Arrays.asList(GlobalVariables.server.getPlayerList().getAllUsernames()).contains(args.get(0))) {
                EntityPlayerMP target = CommandBase.getPlayer(GlobalVariables.server, sender, args.removeFirst());
                String nick = args.stream().collect(Collectors.joining(" "));

                HxCPlayerInfoHandler.setString(target, "NickName", nick);

                TextComponentTranslation msgS = nick.isEmpty() ? new TextComponentTranslation("commands.nick.removed.other.sender", target.getDisplayName()) : new TextComponentTranslation("commands.nick.set.other.sender", target.getDisplayName(), ColorHelper.handleNick(target, false));
                msgS.getStyle().setColor(nick.isEmpty() ? TextFormatting.YELLOW : TextFormatting.GREEN);
                sender.addChatMessage(msgS);

                TextComponentTranslation msgT = nick.isEmpty() ? new TextComponentTranslation("commands.nick.removed.other.target", sender.getDisplayName()) : new TextComponentTranslation("commands.nick.set.other.target", sender.getDisplayName(), ColorHelper.handleNick(target, false));
                msgT.getStyle().setColor(TextFormatting.YELLOW);
                target.addChatMessage(msgT);
            } else if (sender instanceof EntityPlayerMP) {
                String nick = args.stream().collect(Collectors.joining(" "));

                HxCPlayerInfoHandler.setString((EntityPlayerMP) sender, "NickName", nick);

                TextComponentTranslation msg = new TextComponentTranslation("commands.nick.set.self", ColorHelper.handleNick((EntityPlayer) sender, false));
                msg.getStyle().setColor(TextFormatting.GREEN);
                sender.addChatMessage(msg);
            }
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, LinkedList<String> args, @Nullable BlockPos pos) {
        return args.size() == 1 ? CommandBase.getListOfStringsMatchingLastWord(args.toArray(new String[args.size()]), GlobalVariables.server.getAllUsernames()) : Collections.emptyList();
    }

    @Override
    public Class<? extends AbstractMultiCommand> getParentCommand() {
        return CommandHxC.class;
    }
}