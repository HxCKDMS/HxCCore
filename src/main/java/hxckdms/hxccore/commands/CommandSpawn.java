package hxckdms.hxccore.commands;

import hxckdms.hxccore.api.command.AbstractSubCommand;
import hxckdms.hxccore.api.command.HxCCommand;
import hxckdms.hxccore.libraries.GlobalVariables;
import hxckdms.hxccore.utilities.ColorHelper;
import hxckdms.hxccore.utilities.ServerTranslationHelper;
import hxckdms.hxccore.utilities.TeleportHelper;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@HxCCommand
public class CommandSpawn extends AbstractSubCommand<CommandHxC> {
    {
        permissionLevel = 1;
    }

    @Override
    public String getName() {
        return "spawn";
    }

    @Override
    public void execute(ICommandSender sender, LinkedList<String> args) throws CommandException {
        switch (args.size()) {
            case 0:
                if (sender instanceof EntityPlayerMP) {
                    EntityPlayerMP player = (EntityPlayerMP) sender;

                    BlockPos pos = player.getEntityWorld().getSpawnPoint();
                    TeleportHelper.teleportEntityToDimension(player, pos, 0);

                    sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.spawn.self", ColorHelper.handleNick((EntityPlayer) sender, false)).setStyle(new Style().setColor(TextFormatting.GREEN)));
                }
                break;
            case 1:
                EntityPlayerMP target = CommandBase.getPlayer(GlobalVariables.server, sender, args.get(0));

                BlockPos pos = target.getEntityWorld().getSpawnPoint();
                TeleportHelper.teleportEntityToDimension(target, pos, 0);

                sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.spawn.other.sender", sender.getDisplayName()).setStyle(new Style().setColor(TextFormatting.GRAY)));
                sender.addChatMessage(ServerTranslationHelper.getTranslation(target, "commands.spawn.other.target", target.getDisplayName()).setStyle(new Style().setColor(TextFormatting.GOLD)));
                break;
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, LinkedList<String> args, @Nullable BlockPos pos) {
        return args.size() == 1 ? CommandBase.getListOfStringsMatchingLastWord(args.toArray(new String[args.size()]), GlobalVariables.server.getAllUsernames()) : Collections.emptyList();
    }
}
