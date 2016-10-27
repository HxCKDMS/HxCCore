package hxckdms.hxccore.commands;

import hxckdms.hxccore.api.command.*;
import hxckdms.hxccore.libraries.GlobalVariables;
import hxckdms.hxccore.utilities.ColorHelper;
import hxckdms.hxccore.utilities.HxCPlayerInfoHandler;
import hxckdms.hxccore.utilities.ServerTranslationHelper;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@HxCCommand
public class CommandNick extends AbstractSubCommand<CommandHxC> {
    {
        permissionLevel = 1;
    }

    @Override
    public String getCommandName() {
        return "nick";
    }

    @Override
    public void execute(ICommandSender sender, LinkedList<String> args) throws CommandException {
        switch (args.size()) {
            case 0:
                if (sender instanceof EntityPlayerMP) {
                    HxCPlayerInfoHandler.setString((EntityPlayerMP) sender, "NickName", "");

                    sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.nick.removed.self").setStyle(new Style().setColor(TextFormatting.YELLOW)));
                }
                break;
            default:
                if (Arrays.asList(GlobalVariables.server.getPlayerList().getAllUsernames()).contains(args.get(0))) {
                    EntityPlayerMP target = CommandBase.getPlayer(GlobalVariables.server, sender, args.removeFirst());
                    String nick = args.stream().collect(Collectors.joining(" "));

                    HxCPlayerInfoHandler.setString(target, "NickName", nick);

                    TextComponentTranslation msgS = nick.isEmpty() ? ServerTranslationHelper.getTranslation(sender, "commands.nick.removed.other.sender", target.getDisplayName()) : ServerTranslationHelper.getTranslation(sender, "commands.nick.set.other.sender", target.getDisplayName(), ColorHelper.handleNick(target, false));
                    msgS.getStyle().setColor(nick.isEmpty() ? TextFormatting.DARK_GRAY : TextFormatting.GRAY);
                    sender.addChatMessage(msgS);

                    TextComponentTranslation msgT = nick.isEmpty() ? ServerTranslationHelper.getTranslation(target, "commands.nick.removed.other.target", sender.getDisplayName()) : ServerTranslationHelper.getTranslation(target, "commands.nick.set.other.target", sender.getDisplayName(), ColorHelper.handleNick(target, false));
                    msgT.getStyle().setColor(TextFormatting.YELLOW);
                    target.addChatMessage(msgT);
                } else if (sender instanceof EntityPlayerMP) {
                    String nick = args.stream().collect(Collectors.joining(" "));

                    HxCPlayerInfoHandler.setString((EntityPlayerMP) sender, "NickName", nick);
                    sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.nick.set.self", ColorHelper.handleNick((EntityPlayer) sender, false)).setStyle(new Style().setColor(TextFormatting.GREEN)));
                }
                break;
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, LinkedList<String> args, @Nullable BlockPos pos) {
        return args.size() == 1 ? CommandBase.getListOfStringsMatchingLastWord(args.toArray(new String[args.size()]), GlobalVariables.server.getAllUsernames()) : Collections.emptyList();
    }
}
