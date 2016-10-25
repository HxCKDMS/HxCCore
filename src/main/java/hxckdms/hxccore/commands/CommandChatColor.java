package hxckdms.hxccore.commands;

import hxckdms.hxccore.api.command.AbstractMultiCommand;
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
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@HxCCommand
public class CommandChatColor extends AbstractSubCommand {
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
                    sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.chatColor.removed.self").setStyle(new Style().setColor(TextFormatting.YELLOW)));
                }
                break;
            default:
                if (Arrays.asList(GlobalVariables.server.getPlayerList().getAllUsernames()).contains(args.get(0))) {
                    EntityPlayerMP target = CommandBase.getPlayer(GlobalVariables.server, sender, args.getFirst());
                    boolean removing = args.size() == 1 || args.get(1) == null || args.get(1).isEmpty();

                    String color = removing ? "" : args.get(1);
                    if (!color.isEmpty() && Arrays.stream(TextFormatting.values()).filter(format -> format.formattingCode == color.charAt(0)).noneMatch(TextFormatting::isColor)) throw new TranslatedCommandException(sender, "commands.error.noColor");
                    HxCPlayerInfoHandler.setString(target, "ChatColor", color);

                    sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.chatColor." + (!removing ? "set" : "removed") + ".other.sender").setStyle(new Style().setColor(TextFormatting.GRAY)));
                    target.addChatMessage(ServerTranslationHelper.getTranslation(target, "commands.chatColor." + (!removing ? "set" : "removed") + ".other.target").setStyle(new Style().setColor(TextFormatting.YELLOW)));
                } else if (sender instanceof EntityPlayerMP) {
                    String color = args.get(0);
                    if (!color.isEmpty() && Arrays.stream(TextFormatting.values()).filter(format -> format.formattingCode == color.charAt(0)).noneMatch(TextFormatting::isColor)) throw new TranslatedCommandException(sender, "commands.error.noColor");
                    HxCPlayerInfoHandler.setString((EntityPlayer) sender, "ChatColor", color);

                    sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.chatColor.set.self").setStyle(new Style().setColor(TextFormatting.GREEN)));
                }
                break;
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, LinkedList<String> args, @Nullable BlockPos pos) {
        if (args.size() == 1) return CommandBase.getListOfStringsMatchingLastWord(args.toArray(new String[args.size()]), GlobalVariables.server.getAllUsernames());
        else if (args.size() == 2) return Arrays.stream(TextFormatting.values()).filter(TextFormatting::isColor).map(textFormatting -> Character.toString(textFormatting.formattingCode)).collect(Collectors.toList());
        else return Collections.emptyList();
    }

    @Override
    public Class<? extends AbstractMultiCommand> getParentCommand() {
        return CommandHxC.class;
    }
}
