package hxckdms.hxccore.commands;

import hxckdms.hxccore.api.command.AbstractSubCommand;
import hxckdms.hxccore.api.command.HxCCommand;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@HxCCommand
public class CommandHeal extends AbstractSubCommand<CommandHxC> {
    {
        permissionLevel = 2;
    }

    @Override
    public String getName() {
        return "heal";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, LinkedList<String> args) throws CommandException {
        switch (args.size()) {
            case 0:
                if (sender instanceof EntityPlayerMP) {
                    EntityPlayerMP player = (EntityPlayerMP) sender;
                    player.setHealth(player.getMaxHealth());
                    sender.sendMessage(new TextComponentTranslation("commands.heal.self").setStyle(new Style().setColor(TextFormatting.GREEN)));
                }
                break;
            case 1:
                EntityPlayerMP target = CommandBase.getPlayer(server, sender, args.removeFirst());
                target.setHealth(target.getMaxHealth());
                sender.sendMessage(new TextComponentTranslation("commands.heal.other.sender", target.getDisplayName()).setStyle(new Style().setColor(TextFormatting.GRAY)));
                sender.sendMessage(new TextComponentTranslation("commands.heal.other.target", sender.getDisplayName()).setStyle(new Style().setColor(TextFormatting.GOLD)));
                break;
        }
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, LinkedList<String> args, @Nullable BlockPos targetPos) {
        return args.size() == 1 ? CommandBase.getListOfStringsMatchingLastWord(args.toArray(new String[0]), server.getOnlinePlayerNames()) : Collections.emptyList();
    }
}
