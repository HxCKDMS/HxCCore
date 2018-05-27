package hxckdms.hxccore.commands;

import hxckdms.hxccore.api.command.AbstractSubCommand;
import hxckdms.hxccore.api.command.HxCCommand;
import hxckdms.hxccore.libraries.GlobalVariables;
import hxckdms.hxccore.utilities.ServerTranslationHelper;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@HxCCommand
public class CommandWarpList extends AbstractSubCommand<CommandHxC> {
    private static DecimalFormat posFormat = new DecimalFormat("#.###");

    {
        permissionLevel = 1;
    }

    @Override
    public String getName() {
        return "warpList";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, LinkedList<String> args) throws CommandException {
        NBTTagCompound warps = GlobalVariables.customWorldData.getTagCompound("warps", new NBTTagCompound());
        int warpAmount = warps.getKeySet().size();
        int warpsPerPage = 7;
        int pages = (int) Math.ceil((float) warpAmount / (float) warpsPerPage);

        int page = args.size() == 1 ? CommandBase.parseInt(args.get(0), 1, pages) - 1 : 0;
        int min = Math.min(page * warpsPerPage, warpAmount);

        sender.sendMessage(ServerTranslationHelper.getTranslation(sender, "commands.warp.list.header", page + 1, pages).setStyle(new Style().setColor(TextFormatting.AQUA)));
        LinkedList<String> warpList = new LinkedList<>(warps.getKeySet());
        warpList.sort(String::compareTo);

        for (int i = page * warpsPerPage; i < warpsPerPage + min; i++) {
            if (i >= warpAmount) break;
            String name = warpList.get(i);
            NBTTagCompound warp = warps.getCompoundTag(name);

            sender.sendMessage(ServerTranslationHelper.getTranslation(sender, "commands.warp.list.format", name, posFormat.format(warp.getDouble("x")), posFormat.format(warp.getDouble("y")), posFormat.format(warp.getDouble("z")), warp.getInteger("dimension")).setStyle(new Style().setColor(i % 2 == 0 ? TextFormatting.DARK_AQUA : TextFormatting.AQUA)));
        }
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, LinkedList<String> args, @Nullable BlockPos targetPos) {
        NBTTagCompound warps = GlobalVariables.customWorldData.getTagCompound("warps", new NBTTagCompound());
        int warpAmount = warps.getKeySet().size();
        int warpsPerPage = 7;
        int pages = (int) Math.ceil((float) warpAmount / (float) warpsPerPage);
        return IntStream.rangeClosed(1, Math.max(1, pages)).mapToObj(Integer::toString).collect(Collectors.toList());
    }
}
