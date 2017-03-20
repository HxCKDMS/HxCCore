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
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

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
    public String getCommandName() {
        return "back";
    }

    @Override
    public void execute(ICommandSender sender, LinkedList<String> args) throws CommandException {
        if (!(sender instanceof EntityPlayerMP))
            throw new TranslatedCommandException(sender, "commands.exception.playersOnly");
        EntityPlayerMP player = (EntityPlayerMP) sender;
        NBTTagCompound backCompound = HxCPlayerInfoHandler.getTagCompound(player, "backLocation");
        if (backCompound == null)
            throw new TranslatedCommandException(sender, "commands.back.error.noBack");
        TeleportHelper.teleportEntityToDimension(player, backCompound.getInteger("x"), backCompound.getDouble("y"), backCompound.getDouble("z"), backCompound.getInteger("dimension"));
        sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.back.successful", posFormat.format(backCompound.getDouble("x")), posFormat.format(backCompound.getDouble("y")), posFormat.format(backCompound.getDouble("z")), backCompound.getInteger("dimension")).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.BLUE)));
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, LinkedList<String> args) {
        return Collections.emptyList();
    }
}
