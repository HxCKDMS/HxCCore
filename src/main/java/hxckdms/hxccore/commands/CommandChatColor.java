package hxckdms.hxccore.commands;

import hxckdms.hxccore.api.command.AbstractSubCommand;
import hxckdms.hxccore.api.command.HxCCommand;
import hxckdms.hxccore.api.command.TranslatedCommandException;
import hxckdms.hxccore.libraries.GlobalVariables;
import hxckdms.hxccore.utilities.HxCPlayerInfoHandler;
import hxckdms.hxccore.utilities.ServerTranslationHelper;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@HxCCommand
public class CommandChatColor extends AbstractSubCommand<CommandHxC> {
    {
        permissionLevel = 1;
    }

    @Override
    public String getCommandName() {
        return "chatColor";
    }

    @Override
    public void execute(ICommandSender sender, LinkedList<String> args) throws CommandException {
        switch (args.size()) {
            case 0:
                if (sender instanceof EntityPlayerMP) {
                    HxCPlayerInfoHandler.setString((EntityPlayerMP) sender, "ChatColor", "");
                    sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.chatColor.removed.self").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.YELLOW)));
                }
                break;
            default:
                if (Arrays.asList(GlobalVariables.server.getConfigurationManager().getAllUsernames()).contains(args.get(0))) {
                    EntityPlayerMP target = CommandBase.getPlayer(sender, args.getFirst());
                    boolean removing = args.size() == 1 || args.get(1) == null || args.get(1).isEmpty();
                    EnumChatFormatting color = removing ? EnumChatFormatting.WHITE : Arrays.stream(EnumChatFormatting.values()).filter(format -> format.getFormattingCode() == args.get(1).charAt(0)).filter(EnumChatFormatting::isColor).findAny().orElseThrow(() -> new TranslatedCommandException(sender, "commands.error.noColor", args.get(0)));
                    HxCPlayerInfoHandler.setString(target, "ChatColor", !removing ? Character.toString(color.getFormattingCode()) : "");

                    sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.chatColor." + (!removing ? "set" : "removed") + ".other.sender", target.getDisplayName(), new ChatComponentText(color.getFriendlyName()).setChatStyle(new ChatStyle().setColor(color))).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GRAY)));
                    target.addChatMessage(ServerTranslationHelper.getTranslation(target, "commands.chatColor." + (!removing ? "set" : "removed") + ".other.target", sender.getCommandSenderName(), new ChatComponentText(color.getFriendlyName()).setChatStyle(new ChatStyle().setColor(color))).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.YELLOW)));
                } else if (sender instanceof EntityPlayerMP) {
                    EnumChatFormatting color = Arrays.stream(EnumChatFormatting.values()).filter(format -> format.getFormattingCode() == args.get(0).charAt(0)).filter(EnumChatFormatting::isColor).findAny().orElseThrow(() -> new TranslatedCommandException(sender, "commands.error.noColor", args.get(0)));
                    HxCPlayerInfoHandler.setString((EntityPlayer) sender, "ChatColor", Character.toString(color.getFormattingCode()));

                    sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.chatColor.set.self", new ChatComponentText(color.getFriendlyName()).setChatStyle(new ChatStyle().setColor(color))).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GREEN)));
                }
                break;
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, LinkedList<String> args) {
        if (args.size() == 1) return CommandBase.getListOfStringsMatchingLastWord(args.toArray(new String[args.size()]), GlobalVariables.server.getAllUsernames());
        else if (args.size() == 2) return Arrays.stream(EnumChatFormatting.values()).filter(EnumChatFormatting::isColor).map(EnumChatFormatting -> Character.toString(EnumChatFormatting.getFormattingCode())).collect(Collectors.toList());
        else return Collections.emptyList();
    }
}
