package hxckdms.hxccore.commands;

import hxckdms.hxccore.api.command.AbstractMultiCommand;
import hxckdms.hxccore.api.command.AbstractSubCommand;
import hxckdms.hxccore.api.command.HxCCommand;
import hxckdms.hxccore.libraries.GlobalVariables;
import hxckdms.hxccore.utilities.ServerTranslationHelper;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@HxCCommand
public class CommandWarpList extends AbstractSubCommand {
    @Override
    public String getCommandName() {
        return "warpList";
    }

    @Override
    public void execute(ICommandSender sender, LinkedList<String> args) throws CommandException {
        NBTTagCompound warps = GlobalVariables.customWorldData.hasTag("warps") ? GlobalVariables.customWorldData.getTagCompound("warps") : new NBTTagCompound();
        int warpAmount = warps.getKeySet().size();
        int warpsPerPage = 7;
        int pages = (int) Math.ceil((float) warpAmount / (float) warpsPerPage);

        int page = args.size() == 1 ? CommandBase.parseInt(args.get(0), 1, pages) - 1 : 0;
        int min = Math.min(page * warpsPerPage, warpAmount);

        sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.warp.list.header", page + 1, pages).setStyle(new Style().setColor(TextFormatting.AQUA)));
        LinkedList<String> warpList = new LinkedList<>(warps.getKeySet());
        warpList.sort(String::compareTo);

        for (int i = page * warpsPerPage; i < warpsPerPage + min; i++) {
            if (i >= warpAmount) break;
            String name = warpList.get(i);
            NBTTagCompound warp = warps.getCompoundTag(name);

            sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.warp.list.format", name, warp.getInteger("x"), warp.getInteger("y"), warp.getInteger("z"), warp.getInteger("dimension")).setStyle(new Style().setColor(i % 2 == 0 ? TextFormatting.DARK_AQUA : TextFormatting.AQUA)));
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, LinkedList<String> args, @Nullable BlockPos pos) {
        return Collections.singletonList(Integer.toString(1));
    }

    @Override
    public Class<? extends AbstractMultiCommand> getParentCommand() {
        return CommandHxC.class;
    }
}
