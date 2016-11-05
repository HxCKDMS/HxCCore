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
public class CommandGod extends AbstractSubCommand<CommandHxC> {
    {
        permissionLevel = 3;
    }

    @Override
    public String getCommandName() {
        return "god";
    }

    @Override
    public void execute(ICommandSender sender, LinkedList<String> args) throws CommandException {
        switch (args.size()) {
            case 0:
                EntityPlayerMP player = (EntityPlayerMP) sender;
                HxCPlayerInfoHandler.setBoolean(player, "GodMode", !HxCPlayerInfoHandler.getBoolean(player, "GodMode"));
                player.capabilities.disableDamage = HxCPlayerInfoHandler.getBoolean(player, "GodMode");

                if (player.capabilities.disableDamage) {
                    player.heal(player.getMaxHealth() - player.getHealth());
                    player.getFoodStats().addStats(20, 1F);
                }

                sender.addChatMessage(ServerTranslationHelper.getTranslation(player, HxCPlayerInfoHandler.getBoolean(player, "GodMode") ? "commands.god.self.enabled" : "commands.god.self.disabled").setChatStyle(new ChatStyle().setColor(HxCPlayerInfoHandler.getBoolean(player, "GodMode") ? EnumChatFormatting.GREEN : EnumChatFormatting.YELLOW)));
                break;
            case 1:
                EntityPlayerMP target = CommandBase.getPlayer(sender, args.get(0));
                HxCPlayerInfoHandler.setBoolean(target, "GodMode", !HxCPlayerInfoHandler.getBoolean(target, "GodMode"));
                target.capabilities.disableDamage = HxCPlayerInfoHandler.getBoolean(target, "GodMode");

                if (target.capabilities.disableDamage) {
                    target.heal(target.getMaxHealth() - target.getHealth());
                    target.getFoodStats().addStats(20, 1F);
                }

                sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, HxCPlayerInfoHandler.getBoolean(target, "GodMode") ? "commands.god.other.sender.enabled" : "commands.god.other.sender.disabled", sender.getCommandSenderName()).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GRAY)));
                target.addChatMessage(ServerTranslationHelper.getTranslation(target, HxCPlayerInfoHandler.getBoolean(target, "GodMode") ? "commands.god.other.target.enabled" : "commands.god.other.target.disabled", target.getDisplayName()).setChatStyle(new ChatStyle().setColor(HxCPlayerInfoHandler.getBoolean(target, "GodMode") ? EnumChatFormatting.GOLD : EnumChatFormatting.RED)));
                break;
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, LinkedList<String> args) {
        return args.size() == 1 ? CommandBase.getListOfStringsMatchingLastWord(args.toArray(new String[args.size()]), GlobalVariables.server.getAllUsernames()) : Collections.emptyList();
    }
}
