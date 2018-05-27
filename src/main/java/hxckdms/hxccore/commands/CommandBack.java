package hxckdms.hxccore.commands;

import hxckdms.hxccore.api.command.AbstractSubCommand;
import hxckdms.hxccore.api.command.HxCCommand;
import hxckdms.hxccore.api.command.TranslatedCommandException;
import hxckdms.hxccore.utilities.HxCPlayerInfoHandler;
import hxckdms.hxccore.utilities.ServerTranslationHelper;
import hxckdms.hxccore.utilities.TeleportHelper;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@HxCCommand
public class CommandBack extends AbstractSubCommand<CommandHxC> {
    private static DecimalFormat posFormat = new DecimalFormat("#.###");

    {
        permissionLevel = 1;
    }

    @Override
    public String getName() {
        return "back";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, LinkedList<String> args) throws CommandException {
        if (!(sender instanceof EntityPlayerMP))
            throw new TranslatedCommandException(sender, "commands.exception.playersOnly");
        EntityPlayerMP player = (EntityPlayerMP) sender;
        NBTTagCompound backCompound = HxCPlayerInfoHandler.getTagCompound(player, "backLocation");
        if (backCompound == null)
            throw new TranslatedCommandException(sender, "commands.back.error.noBack");
        TeleportHelper.teleportEntityToDimension(player, backCompound.getInteger("x"), backCompound.getDouble("y"), backCompound.getDouble("z"), backCompound.getInteger("dimension"));
        sender.sendMessage(ServerTranslationHelper.getTranslation(sender, "commands.back.successful", posFormat.format(backCompound.getDouble("x")), posFormat.format(backCompound.getDouble("y")), posFormat.format(backCompound.getDouble("z")), backCompound.getInteger("dimension")).setStyle(new Style().setColor(TextFormatting.BLUE)));
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, LinkedList<String> args, @Nullable BlockPos targetPos) {
        return Collections.emptyList();
    }
}
