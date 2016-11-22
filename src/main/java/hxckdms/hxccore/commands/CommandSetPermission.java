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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

@HxCCommand
public class CommandSetPermission extends AbstractSubCommand<CommandHxC> {
    {
        permissionLevel = 5;
    }

    @Override
    public String getName() {
        return "setPermission";
    }

    @Override
    public void execute(ICommandSender sender, LinkedList<String> args) throws CommandException {
        boolean hasTarget = Arrays.asList(GlobalVariables.server.getPlayerList().getOnlinePlayerNames()).contains(args.get(0));
        EntityPlayerMP target = hasTarget ? CommandBase.getPlayer(GlobalVariables.server, sender, args.get(0)) : (EntityPlayerMP) sender;

        Optional<Integer> optional = CommandRegistry.CommandConfig.commandPermissions.keySet().stream().max(Comparator.naturalOrder());
        if (!optional.isPresent()) throw new NullPointerException(CommandRegistry.CommandConfig.commandPermissions.toString() + " is empty");

        int level = CommandBase.parseInt(args.get(hasTarget ? 1 : 0), 1, optional.get());

        if (hasTarget) {
            sender.sendMessage(ServerTranslationHelper.getTranslation(sender, "commands.setpermissions.sender", target.getDisplayName(), ColorHelper.handlePermission(level)).setStyle(new Style().setColor(TextFormatting.GRAY)));
            target.sendMessage(ServerTranslationHelper.getTranslation(target, "commands.setpermissions.target", sender.getDisplayName(), ColorHelper.handlePermission(level)).setStyle(new Style().setColor(TextFormatting.YELLOW)));
        } else sender.sendMessage(ServerTranslationHelper.getTranslation(sender, "commands.setpermissions.self", ColorHelper.handlePermission(level).setStyle(new Style().setColor(TextFormatting.GREEN))));


        GlobalVariables.permissionData.setInteger(target.getUniqueID().toString(), level);
    }

    @Override
    public List<String> addTabCompletions(ICommandSender sender, LinkedList<String> args, @Nullable BlockPos pos) {
        if (args.size() == 1) return CommandBase.getListOfStringsMatchingLastWord(args.toArray(new String[args.size()]), GlobalVariables.server.getOnlinePlayerNames());
        else if (args.size() == 2) return CommandRegistry.CommandConfig.commandPermissions.keySet().stream().map(i -> Integer.toString(i)).collect(Collectors.toCollection(LinkedList::new));
        return Collections.emptyList();
    }
}
