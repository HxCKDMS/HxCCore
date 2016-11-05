package hxckdms.hxccore.commands;

import hxckdms.hxccore.api.command.AbstractSubCommand;
import hxckdms.hxccore.api.command.HxCCommand;
import hxckdms.hxccore.libraries.GlobalVariables;
import hxckdms.hxccore.registry.CommandRegistry;
import hxckdms.hxccore.utilities.ColorHelper;
import hxckdms.hxccore.utilities.ServerTranslationHelper;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

import java.util.*;
import java.util.stream.Collectors;

@HxCCommand
public class CommandSetPermission extends AbstractSubCommand<CommandHxC> {
    {
        permissionLevel = 5;
    }

    @Override
    public String getCommandName() {
        return "setPermission";
    }

    @Override
    public void execute(ICommandSender sender, LinkedList<String> args) throws CommandException {
        boolean hasTarget = Arrays.asList(GlobalVariables.server.getConfigurationManager().getAllUsernames()).contains(args.get(0));
        EntityPlayerMP target = hasTarget ? CommandBase.getPlayer(sender, args.get(0)) : (EntityPlayerMP) sender;

        Optional<Integer> optional = CommandRegistry.CommandConfig.commandPermissions.keySet().stream().max(Comparator.naturalOrder());
        if (!optional.isPresent()) throw new NullPointerException(CommandRegistry.CommandConfig.commandPermissions.toString() + " is empty");

        int level = CommandBase.parseIntBounded(sender, args.get(hasTarget ? 1 : 0), 1, optional.get());

        if (hasTarget) {
            sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.setpermissions.sender", target.getDisplayName(), ColorHelper.handlePermission(level)).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GRAY)));
            target.addChatMessage(ServerTranslationHelper.getTranslation(target, "commands.setpermissions.target", sender.getCommandSenderName(), ColorHelper.handlePermission(level)).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.YELLOW)));
        } else sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.setpermissions.self", ColorHelper.handlePermission(level).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GREEN))));


        GlobalVariables.permissionData.setInteger(target.getUniqueID().toString(), level);
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, LinkedList<String> args) {
        if (args.size() == 1) return CommandBase.getListOfStringsMatchingLastWord(args.toArray(new String[args.size()]), GlobalVariables.server.getAllUsernames());
        else if (args.size() == 2) return CommandRegistry.CommandConfig.commandPermissions.keySet().stream().map(i -> Integer.toString(i)).collect(Collectors.toCollection(LinkedList::new));
        return Collections.emptyList();
    }
}
