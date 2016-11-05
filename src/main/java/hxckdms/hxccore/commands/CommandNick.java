package hxckdms.hxccore.commands;

import hxckdms.hxccore.api.command.AbstractSubCommand;
import hxckdms.hxccore.api.command.HxCCommand;
import hxckdms.hxccore.libraries.GlobalVariables;
import hxckdms.hxccore.utilities.ColorHelper;
import hxckdms.hxccore.utilities.HxCPlayerInfoHandler;
import hxckdms.hxccore.utilities.ServerTranslationHelper;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

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

                    sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.nick.removed.self").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.YELLOW)));
                }
                break;
            default:
                if (Arrays.asList(GlobalVariables.server.getConfigurationManager().getAllUsernames()).contains(args.get(0))) {
                    EntityPlayerMP target = CommandBase.getPlayer(sender, args.removeFirst());
                    String nick = args.stream().collect(Collectors.joining(" "));

                    HxCPlayerInfoHandler.setString(target, "NickName", nick);

                    ChatComponentTranslation msgS = nick.isEmpty() ? ServerTranslationHelper.getTranslation(sender, "commands.nick.removed.other.sender", target.getDisplayName()) : ServerTranslationHelper.getTranslation(sender, "commands.nick.set.other.sender", target.getDisplayName(), ColorHelper.handleNick(target, false));
                    msgS.getChatStyle().setColor(nick.isEmpty() ? EnumChatFormatting.DARK_GRAY : EnumChatFormatting.GRAY);
                    sender.addChatMessage(msgS);

                    ChatComponentTranslation msgT = nick.isEmpty() ? ServerTranslationHelper.getTranslation(target, "commands.nick.removed.other.target", sender.getCommandSenderName()) : ServerTranslationHelper.getTranslation(target, "commands.nick.set.other.target", sender.getCommandSenderName(), ColorHelper.handleNick(target, false));
                    msgT.getChatStyle().setColor(EnumChatFormatting.YELLOW);
                    target.addChatMessage(msgT);
                } else if (sender instanceof EntityPlayerMP) {
                    String nick = args.stream().collect(Collectors.joining(" "));

                    HxCPlayerInfoHandler.setString((EntityPlayerMP) sender, "NickName", nick);
                    sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.nick.set.self", ColorHelper.handleNick((EntityPlayer) sender, false)).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GREEN)));
                }
                break;
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, LinkedList<String> args) {
        return args.size() == 1 ? CommandBase.getListOfStringsMatchingLastWord(args.toArray(new String[args.size()]), GlobalVariables.server.getAllUsernames()) : Collections.emptyList();
    }
}
