package hxckdms.hxccore.commands;

import hxckdms.hxccore.api.command.ISubCommand;
import hxckdms.hxccore.registry.command.CommandRegistry;
import hxckdms.hxccore.registry.command.HxCCommand;
import hxckdms.hxccore.utilities.HxCPlayerInfoHandler;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentTranslation;

import java.util.LinkedList;
import java.util.List;

@HxCCommand(defaultPermission = 1, mainCommand = CommandRegistry.CommandHxC.class, isEnabled = true)
public class CommandFly implements ISubCommand {
    @Override
    public String getCommandName() {
        return "fly";
    }

    @Override
    public int[] getCommandRequiredParams() {
        return new int[]{0, 1};
    }

    @Override
    public void handleCommand(ICommandSender sender, LinkedList<String> args, boolean isPlayer) throws CommandException {
        if (sender instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) sender;

            player.capabilities.allowFlying = !player.capabilities.allowFlying;
            player.capabilities.isFlying = !player.capabilities.isFlying;
            HxCPlayerInfoHandler.setBoolean(player, "AllowFlying", player.capabilities.allowFlying);
            player.sendPlayerAbilities();

            player.addChatComponentMessage(new TextComponentTranslation(player.capabilities.allowFlying ? "command.fly.enabled" : "command.fly.disabled"));
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        return null;
    }
}
