package hxckdms.hxccore.commands;

import hxckdms.hxccore.api.command.AbstractSubCommand;
import hxckdms.hxccore.api.command.HxCCommand;
import hxckdms.hxccore.libraries.GlobalVariables;
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
public class CommandHeal extends AbstractSubCommand<CommandHxC> {
    {
        permissionLevel = 2;
    }

    @Override
    public String getCommandName() {
        return "heal";
    }

    @Override
    public void execute(ICommandSender sender, LinkedList<String> args) throws CommandException {
        switch (args.size()) {
            case 0:
                if (sender instanceof EntityPlayerMP) {
                    EntityPlayerMP player = (EntityPlayerMP) sender;
                    player.setHealth(player.getMaxHealth());
                    sender.addChatMessage(ServerTranslationHelper.getTranslation(player, "commands.heal.self").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GREEN)));
                }
                break;
            case 1:
                EntityPlayerMP target = CommandBase.getPlayer(sender, args.removeFirst());
                target.setHealth(target.getMaxHealth());
                sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.heal.other.sender", target.getDisplayName()).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GRAY)));
                target.addChatMessage(ServerTranslationHelper.getTranslation(target, "commands.heal.other.target", sender.getCommandSenderName()).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GOLD)));
                break;
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, LinkedList<String> args) {
        return  args.size() == 1 ? CommandBase.getListOfStringsMatchingLastWord(args.toArray(new String[args.size()]), GlobalVariables.server.getAllUsernames()) : Collections.emptyList();
    }
}
