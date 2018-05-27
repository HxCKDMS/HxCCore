package hxckdms.hxccore.commands;

import hxckdms.hxccore.api.command.AbstractSubCommand;
import hxckdms.hxccore.api.command.HxCCommand;
import hxckdms.hxccore.utilities.ServerTranslationHelper;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@HxCCommand
public class CommandFeed extends AbstractSubCommand<CommandHxC> {
    {
        permissionLevel = 2;
    }

    @Override
    public String getName() {
        return "feed";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, LinkedList<String> args) throws CommandException {
        switch (args.size()) {
            case 0:
                if (sender instanceof EntityPlayerMP) {
                    EntityPlayerMP player = (EntityPlayerMP) sender;
                    player.getFoodStats().addStats(20, 1F);

                    sender.sendMessage(ServerTranslationHelper.getTranslation(player, "commands.feed.self").setStyle(new Style().setColor(TextFormatting.GREEN)));
                }
                break;
            case 1:
                EntityPlayerMP target = CommandBase.getPlayer(server, sender, args.removeFirst());
                target.getFoodStats().addStats(20, 1F);

                sender.sendMessage(ServerTranslationHelper.getTranslation(sender, "commands.feed.other.sender", sender.getName()).setStyle(new Style().setColor(TextFormatting.GRAY)));
                sender.sendMessage(ServerTranslationHelper.getTranslation(target, "commands.feed.other.target", target.getDisplayName()).setStyle(new Style().setColor(TextFormatting.GOLD)));
                break;
        }
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, LinkedList<String> args, @Nullable BlockPos targetPos) {
        return args.size() == 1 ? CommandBase.getListOfStringsMatchingLastWord(args.toArray(new String[0]), server.getOnlinePlayerNames()) : Collections.emptyList();
    }
}
