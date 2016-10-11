package hxckdms.hxccore.commands;

import hxckdms.hxccore.api.command.ISubCommand;
import hxckdms.hxccore.libraries.GlobalVariables;
import hxckdms.hxccore.registry.command.CommandRegistry;
import hxckdms.hxccore.registry.command.HxCCommand;
import hxckdms.hxccore.utilities.ColorHelper;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.util.*;

@HxCCommand(defaultPermission = 5, mainCommand = CommandRegistry.CommandHxC.class, isEnabled = true)
public class CommandSetPermission implements ISubCommand {
    @Override
    public String getCommandName() {
        return "SetPermission";
    }

    @Override
    public void execute(ICommandSender sender, LinkedList<String> args) throws CommandException {
        boolean hasTarget = Arrays.asList(GlobalVariables.server.getPlayerList().getAllUsernames()).contains(args.get(0));
        EntityPlayerMP target = hasTarget ? CommandBase.getPlayer(GlobalVariables.server, sender, args.get(0)) : (EntityPlayerMP) sender;

        Optional<Integer> optional = CommandRegistry.CommandConfig.commandPermissions.keySet().stream().max(Comparator.naturalOrder());
        if (!optional.isPresent()) throw new NullPointerException(CommandRegistry.CommandConfig.commandPermissions.toString() + " is empty");

        System.out.println(optional.get());
        int level = CommandBase.parseInt(args.get(hasTarget ? 1 : 0), 1, optional.get());

        if (hasTarget) {
            TextComponentTranslation msgS = new TextComponentTranslation("commands.setpermissions.sender", target.getDisplayName(), ColorHelper.getPermissionTag(level));
            msgS.getStyle().setColor(TextFormatting.GREEN);
            sender.addChatMessage(msgS);

            TextComponentTranslation msgT = new TextComponentTranslation("commands.setpermissions.target", sender.getDisplayName(), ColorHelper.getPermissionTag(level));
            msgT.getStyle().setColor(TextFormatting.YELLOW);
            target.addChatMessage(msgT);
        } else {
            TextComponentTranslation msgS = new TextComponentTranslation("commands.setpermissions.self", ColorHelper.getPermissionTag(level));
            msgS.getStyle().setColor(TextFormatting.GREEN);
            sender.addChatMessage(msgS);
        }

        GlobalVariables.permissionData.setInteger(target.getUniqueID().toString(), level);
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, LinkedList<String> args, @Nullable BlockPos pos) {
        if (args.size() == 1) return CommandBase.getListOfStringsMatchingLastWord(args.toArray(new String[args.size()]), GlobalVariables.server.getAllUsernames());
        else if (args.size() == 2) CommandRegistry.CommandConfig.commandPermissions.keySet();
        return Collections.emptyList();
    }
}