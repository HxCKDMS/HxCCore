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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
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
    public String getName() {
        return "chatColor";
    }

    @Override
    public void execute(ICommandSender sender, LinkedList<String> args) throws CommandException {
        switch (args.size()) {
            case 0:
                if (sender instanceof EntityPlayerMP) {
                    HxCPlayerInfoHandler.setString((EntityPlayerMP) sender, "ChatColor", "");
                    sender.sendMessage(ServerTranslationHelper.getTranslation(sender, "commands.chatColor.removed.self").setStyle(new Style().setColor(TextFormatting.YELLOW)));
                }
                break;
            default:
                if (Arrays.asList(GlobalVariables.server.getPlayerList().getOnlinePlayerNames()).contains(args.get(0))) {
                    EntityPlayerMP target = CommandBase.getPlayer(GlobalVariables.server, sender, args.getFirst());
                    boolean removing = args.size() == 1 || args.get(1) == null || args.get(1).isEmpty();
                    TextFormatting color = removing ? TextFormatting.WHITE : Arrays.stream(TextFormatting.values()).filter(format -> format.formattingCode == args.get(1).charAt(0)).filter(TextFormatting::isColor).findAny().orElseThrow(() -> new TranslatedCommandException(sender, "commands.error.noColor", args.get(0)));
                    HxCPlayerInfoHandler.setString(target, "ChatColor", !removing ? Character.toString(color.formattingCode) : "");

                    sender.sendMessage(ServerTranslationHelper.getTranslation(sender, "commands.chatColor." + (!removing ? "set" : "removed") + ".other.sender", target.getName(), new TextComponentString(color.getFriendlyName()).setStyle(new Style().setColor(color))).setStyle(new Style().setColor(TextFormatting.GRAY)));
                    target.sendMessage(ServerTranslationHelper.getTranslation(target, "commands.chatColor." + (!removing ? "set" : "removed") + ".other.target", sender.getName(), new TextComponentString(color.getFriendlyName()).setStyle(new Style().setColor(color))).setStyle(new Style().setColor(TextFormatting.YELLOW)));
                } else if (sender instanceof EntityPlayerMP) {
                    TextFormatting color = Arrays.stream(TextFormatting.values()).filter(format -> format.formattingCode == args.get(0).charAt(0)).filter(TextFormatting::isColor).findAny().orElseThrow(() -> new TranslatedCommandException(sender, "commands.error.noColor", args.get(0)));
                    HxCPlayerInfoHandler.setString((EntityPlayer) sender, "ChatColor", Character.toString(color.formattingCode));

                    sender.sendMessage(ServerTranslationHelper.getTranslation(sender, "commands.chatColor.set.self", new TextComponentString(color.getFriendlyName()).setStyle(new Style().setColor(color))).setStyle(new Style().setColor(TextFormatting.GREEN)));
                }
                break;
        }
    }

    @Override
    public List<String> addTabCompletions(ICommandSender sender, LinkedList<String> args, @Nullable BlockPos pos) {
        if (args.size() == 1) return CommandBase.getListOfStringsMatchingLastWord(args.toArray(new String[args.size()]), GlobalVariables.server.getOnlinePlayerNames());
        else if (args.size() == 2) return Arrays.stream(TextFormatting.values()).filter(TextFormatting::isColor).map(textFormatting -> Character.toString(textFormatting.formattingCode)).collect(Collectors.toList());
        else return Collections.emptyList();
    }
}
