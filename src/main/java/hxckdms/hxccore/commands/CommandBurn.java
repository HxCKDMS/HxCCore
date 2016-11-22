package hxckdms.hxccore.commands;

import hxckdms.hxccore.api.command.AbstractSubCommand;
import hxckdms.hxccore.api.command.HxCCommand;
import hxckdms.hxccore.libraries.GlobalVariables;
import hxckdms.hxccore.utilities.ServerTranslationHelper;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
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
    public String getName() {
        return "burn";
    }

    @Override
    public void execute(ICommandSender sender, LinkedList<String> args) throws CommandException {
        switch (args.size()) {
            case 0:
                if (sender instanceof EntityPlayerMP) {
                    EntityPlayerMP player = (EntityPlayerMP) sender;

                    player.setFire(Integer.MAX_VALUE / 20);
                    sender.sendMessage(ServerTranslationHelper.getTranslation(sender, "commands.burn.self", Integer.MAX_VALUE / 20).setStyle(new Style().setColor(TextFormatting.RED)));
                }
                break;
            case 1:
                if (Arrays.asList(GlobalVariables.server.getPlayerList().getOnlinePlayerNames()).contains(args.get(0))) {
                    EntityPlayerMP target = CommandBase.getPlayer(GlobalVariables.server, sender, args.get(0));

                    target.setFire(Integer.MAX_VALUE / 20);
                    sender.sendMessage(ServerTranslationHelper.getTranslation(sender, "commands.burn.other.sender", target.getDisplayName(), Integer.MAX_VALUE / 20).setStyle(new Style().setColor(TextFormatting.GRAY)));
                    target.sendMessage(ServerTranslationHelper.getTranslation(target, "commands.burn.other.target", sender.getDisplayName(), Integer.MAX_VALUE / 20).setStyle(new Style().setColor(TextFormatting.RED)));
                } else {
                    EntityPlayerMP player = (EntityPlayerMP) sender;
                    int duration = CommandBase.parseInt(args.get(1), 1, Integer.MAX_VALUE / 20);

                    player.setFire(duration);
                    sender.sendMessage(ServerTranslationHelper.getTranslation(sender, "commands.burn.self", duration).setStyle(new Style().setColor(TextFormatting.RED)));
                }
                break;
            case 2:
                EntityPlayerMP target = CommandBase.getPlayer(GlobalVariables.server, sender, args.get(0));
                int duration = CommandBase.parseInt(args.get(1), 1, Integer.MAX_VALUE / 20);

                target.setFire(duration);
                sender.sendMessage(ServerTranslationHelper.getTranslation(sender, "commands.burn.other.sender", target.getDisplayName(), duration).setStyle(new Style().setColor(TextFormatting.GRAY)));
                target.sendMessage(ServerTranslationHelper.getTranslation(target, "commands.burn.other.target", sender.getDisplayName(), duration).setStyle(new Style().setColor(TextFormatting.RED)));
                break;
        }
    }

    @Override
    public List<String> addTabCompletions(ICommandSender sender, LinkedList<String> args, @Nullable BlockPos pos) {
        if (args.size() == 1) return CommandBase.getListOfStringsMatchingLastWord(args.toArray(new String[args.size()]), GlobalVariables.server.getOnlinePlayerNames());
        else if (args.size() == 2) return Collections.singletonList(Integer.toString(60));
        else return Collections.emptyList();
    }
}
