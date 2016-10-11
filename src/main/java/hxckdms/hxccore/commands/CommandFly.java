package hxckdms.hxccore.commands;

import hxckdms.hxccore.api.command.ISubCommand;
import hxckdms.hxccore.libraries.GlobalVariables;
import hxckdms.hxccore.registry.command.CommandRegistry;
import hxckdms.hxccore.registry.command.HxCCommand;
import hxckdms.hxccore.utilities.HxCPlayerInfoHandler;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@HxCCommand(defaultPermission = 1, mainCommand = CommandRegistry.CommandHxC.class, isEnabled = true)
public class CommandFly implements ISubCommand {
    @Override
    public String getCommandName() {
        return "fly";
    }

    @Override
    public void execute(ICommandSender sender, LinkedList<String> args) throws CommandException {
        if (sender instanceof EntityPlayerMP) {
            EntityPlayerMP target = args.isEmpty() ? (EntityPlayerMP) sender : CommandBase.getPlayer(GlobalVariables.server, sender, args.get(0));

            target.capabilities.allowFlying = !target.capabilities.allowFlying;
            target.capabilities.isFlying = !target.capabilities.isFlying;
            HxCPlayerInfoHandler.setBoolean(target, "AllowFlying", target.capabilities.allowFlying);
            target.sendPlayerAbilities();

            TextComponentTranslation msg = new TextComponentTranslation(target.capabilities.allowFlying ? "commands.fly.enabled" : "commands.fly.disabled");
            msg.getStyle().setColor(target.capabilities.allowFlying ? TextFormatting.GREEN : TextFormatting.YELLOW);

            target.addChatComponentMessage(msg);
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, LinkedList<String> args, @Nullable BlockPos pos) {
        return args.size() == 1 ? CommandBase.getListOfStringsMatchingLastWord(args.toArray(new String[args.size()]), GlobalVariables.server.getAllUsernames()) : Collections.emptyList();
    }
}