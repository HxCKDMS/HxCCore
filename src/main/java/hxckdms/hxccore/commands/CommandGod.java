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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@HxCCommand
public class CommandGod extends AbstractSubCommand<CommandHxC> {
    {
        permissionLevel = 3;
    }

    @Override
    public String getName() {
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

                sender.sendMessage(ServerTranslationHelper.getTranslation(player, HxCPlayerInfoHandler.getBoolean(player, "GodMode") ? "commands.god.self.enabled" : "commands.god.self.disabled").setStyle(new Style().setColor(HxCPlayerInfoHandler.getBoolean(player, "GodMode") ? TextFormatting.GREEN : TextFormatting.YELLOW)));
                break;
            case 1:
                EntityPlayerMP target = CommandBase.getPlayer(GlobalVariables.server, sender, args.get(0));
                HxCPlayerInfoHandler.setBoolean(target, "GodMode", !HxCPlayerInfoHandler.getBoolean(target, "GodMode"));
                target.capabilities.disableDamage = HxCPlayerInfoHandler.getBoolean(target, "GodMode");

                if (target.capabilities.disableDamage) {
                    target.heal(target.getMaxHealth() - target.getHealth());
                    target.getFoodStats().addStats(20, 1F);
                }

                sender.sendMessage(ServerTranslationHelper.getTranslation(sender, HxCPlayerInfoHandler.getBoolean(target, "GodMode") ? "commands.god.other.sender.enabled" : "commands.god.other.sender.disabled", sender.getDisplayName()).setStyle(new Style().setColor(TextFormatting.GRAY)));
                target.sendMessage(ServerTranslationHelper.getTranslation(target, HxCPlayerInfoHandler.getBoolean(target, "GodMode") ? "commands.god.other.target.enabled" : "commands.god.other.target.disabled", target.getDisplayName()).setStyle(new Style().setColor(HxCPlayerInfoHandler.getBoolean(target, "GodMode") ? TextFormatting.GOLD : TextFormatting.RED)));
                break;
        }
    }

    @Override
    public List<String> addTabCompletions(ICommandSender sender, LinkedList<String> args, @Nullable BlockPos pos) {
        return args.size() == 1 ? CommandBase.getListOfStringsMatchingLastWord(args.toArray(new String[args.size()]), GlobalVariables.server.getOnlinePlayerNames()) : Collections.emptyList();
    }
}
