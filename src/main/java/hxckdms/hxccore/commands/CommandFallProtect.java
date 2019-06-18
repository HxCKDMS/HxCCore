package hxckdms.hxccore.commands;

import hxckdms.hxccore.api.command.AbstractSubCommand;
import hxckdms.hxccore.api.command.HxCCommand;
import hxckdms.hxccore.libraries.GlobalVariables;
import hxckdms.hxccore.utilities.HxCPlayerInfoHandler;
import hxckdms.hxccore.utilities.ServerTranslationHelper;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@HxCCommand
public class CommandFallProtect extends AbstractSubCommand<CommandHxC> {
    {
        permissionLevel = 3;
    }

    @Override
    public String getCommandName() {
        return "fallprotect";
    }

    @Override
    public void execute(ICommandSender sender, LinkedList<String> args) throws CommandException {
        if (sender instanceof EntityPlayerMP) {
            switch (args.size()) {
                case 0:
                    EntityPlayerMP player = (EntityPlayerMP) sender;
                    HxCPlayerInfoHandler.setInteger(player, "FallProtect", 1);

                    sender.addChatMessage(ServerTranslationHelper.getTranslation(player, HxCPlayerInfoHandler.getInteger(player, "FallProtect") > 0 ? "You won't take fall damage the next " + HxCPlayerInfoHandler.getInteger(player, "FallProtect") + " times." : "You will continue taking fall damage.").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GOLD)));
                    break;
                case 1:
                    if (isInteger(args.get(0))) {
                        player = (EntityPlayerMP) sender;
                        HxCPlayerInfoHandler.setInteger(player, "FallProtect", Integer.parseInt(args.get(0)));

                        player.addChatMessage(ServerTranslationHelper.getTranslation(player, HxCPlayerInfoHandler.getInteger(player, "FallProtect") > 0 ? "You won't take fall damage the next " + HxCPlayerInfoHandler.getInteger(player, "FallProtect") + " times." : "You will continue taking fall damage.").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GOLD)));
                        break;
                    } else {
                        EntityPlayerMP target = CommandBase.getPlayer(sender, args.get(0));
                        HxCPlayerInfoHandler.setInteger(target, "FallProtect", 1);

                        target.addChatMessage(ServerTranslationHelper.getTranslation(target, HxCPlayerInfoHandler.getInteger(target, "FallProtect") > 0 ? "You won't take fall damage the next " + HxCPlayerInfoHandler.getInteger(target, "FallProtect") + " times." : "You will continue taking fall damage.").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GOLD)));
                    }
                    break;
                case 2:
                    EntityPlayerMP target = CommandBase.getPlayer(sender, args.get(0));
                    HxCPlayerInfoHandler.setInteger(target, "FallProtect", isInteger(args.get(1)) ? Integer.parseInt(args.get(1)) : 1);

                    target.addChatMessage(ServerTranslationHelper.getTranslation(target, HxCPlayerInfoHandler.getInteger(target, "FallProtect") > 0 ? "You won't take fall damage the next " + HxCPlayerInfoHandler.getInteger(target, "FallProtect") + " times." : "You will continue taking fall damage.").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GOLD)));
                    break;
            }
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, LinkedList<String> args) {
        return args.size() == 1 ? CommandBase.getListOfStringsMatchingLastWord(args.toArray(new String[args.size()]), GlobalVariables.server.getAllUsernames()) : args.size() == 2 ? Collections.singletonList(String.valueOf(5)) : Collections.emptyList();
    }

    public static boolean isInteger(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        if (length == 0) {
            return false;
        }
        int i = 0;
        if (str.charAt(0) == '-') {
            if (length == 1) {
                return false;
            }
            i = 1;
        }
        for (; i < length; i++) {
            char c = str.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }
}
