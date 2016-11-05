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

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@HxCCommand
public class CommandBurn extends AbstractSubCommand<CommandHxC> {
    {
        permissionLevel = 3;
    }

    @Override
    public String getCommandName() {
        return "burn";
    }

    @Override
    public void execute(ICommandSender sender, LinkedList<String> args) throws CommandException {
        switch (args.size()) {
            case 0:
                if (sender instanceof EntityPlayerMP) {
                    EntityPlayerMP player = (EntityPlayerMP) sender;

                    player.setFire(Integer.MAX_VALUE / 20);
                    sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.burn.self", Integer.MAX_VALUE / 20).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
                }
                break;
            case 1:
                if (Arrays.asList(GlobalVariables.server.getConfigurationManager().getAllUsernames()).contains(args.get(0))) {
                    EntityPlayerMP target = CommandBase.getPlayer(sender, args.get(0));

                    target.setFire(Integer.MAX_VALUE / 20);
                    sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.burn.other.sender", target.getDisplayName(), Integer.MAX_VALUE / 20).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GRAY)));
                    target.addChatMessage(ServerTranslationHelper.getTranslation(target, "commands.burn.other.target", sender.getCommandSenderName(), Integer.MAX_VALUE / 20).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
                } else {
                    EntityPlayerMP player = (EntityPlayerMP) sender;
                    int duration = CommandBase.parseIntBounded(sender, args.get(1), 1, Integer.MAX_VALUE / 20);

                    player.setFire(duration);
                    sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.burn.self", duration).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
                }
                break;
            case 2:
                EntityPlayerMP target = CommandBase.getPlayer(sender, args.get(0));
                int duration = CommandBase.parseIntBounded(sender, args.get(1), 1, Integer.MAX_VALUE / 20);

                target.setFire(duration);
                sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.burn.other.sender", target.getDisplayName(), duration).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GRAY)));
                target.addChatMessage(ServerTranslationHelper.getTranslation(target, "commands.burn.other.target", sender.getCommandSenderName(), duration).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
                break;
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, LinkedList<String> args) {
        if (args.size() == 1) return CommandBase.getListOfStringsMatchingLastWord(args.toArray(new String[args.size()]), GlobalVariables.server.getAllUsernames());
        else if (args.size() == 2) return Collections.singletonList(Integer.toString(60));
        else return Collections.emptyList();
    }
}
