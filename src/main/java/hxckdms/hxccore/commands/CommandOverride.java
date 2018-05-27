package hxckdms.hxccore.commands;

import hxckdms.hxccore.api.command.AbstractSubCommand;
import hxckdms.hxccore.api.command.HxCCommand;
import hxckdms.hxccore.api.command.TranslatedCommandException;
import hxckdms.hxccore.utilities.ServerTranslationHelper;
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
public class CommandOverride extends AbstractSubCommand<CommandHxC> {
    {
        permissionLevel = 5;
    }

    @Override
    public String getName() {
        return "override";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, LinkedList<String> args) throws CommandException {
        if (!(sender instanceof EntityPlayerMP)) throw new TranslatedCommandException(sender, "commands.exception.playersOnly");
        EntityPlayerMP player = (EntityPlayerMP) sender;
        if (CommandProtect.override.contains(player)) CommandProtect.override.remove(player);
        else CommandProtect.override.add(player);
        sender.sendMessage(ServerTranslationHelper.getTranslation(sender, "commands.override." + (CommandProtect.override.contains(player) ? "enabled" : "disabled")).setStyle(new Style().setColor(TextFormatting.YELLOW)));
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, LinkedList<String> args, @Nullable BlockPos targetPos) {
        return Collections.emptyList();
    }
}
