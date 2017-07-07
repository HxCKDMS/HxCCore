package hxckdms.hxccore.commands;

import hxckdms.hxccore.api.command.AbstractSubCommand;
import hxckdms.hxccore.api.command.HxCCommand;
import hxckdms.hxccore.api.command.TranslatedCommandException;
import hxckdms.hxccore.utilities.HxCPlayerInfoHandler;
import hxckdms.hxccore.utilities.ServerTranslationHelper;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@HxCCommand
public class CommandHomeList extends AbstractSubCommand<CommandHxC> {
    private static DecimalFormat posFormat = new DecimalFormat("#.###");

    {
        permissionLevel = 1;
    }

    @Override
    public String getName() {
        return "homeList";
    }

    @Override
    public void execute(ICommandSender sender, LinkedList<String> args) throws CommandException {
        if (!(sender instanceof EntityPlayerMP)) throw new TranslatedCommandException(sender, "commands.exception.playersOnly");
        NBTTagCompound homes = HxCPlayerInfoHandler.getTagCompound((EntityPlayer) sender, "homes", new NBTTagCompound());
        int homeAmount = homes.getKeySet().size();
        int homesPerPage = 7;
        int pages = (int) Math.ceil((float) homeAmount / (float) homesPerPage);

        int page = args.size() == 1 ? CommandBase.parseInt(args.get(0), 1, pages) - 1 : 0;
        int min = Math.min(page * homesPerPage, homeAmount);

        sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.home.list.header", page + 1, pages).setStyle(new Style().setColor(TextFormatting.AQUA)));
        LinkedList<String> homeList = new LinkedList<>(homes.getKeySet());
        homeList.sort(String::compareTo);

        for (int i = page * homesPerPage; i < homesPerPage + min; i++) {
            if (i >= homeAmount) break;
            String name = homeList.get(i);
            NBTTagCompound warp = homes.getCompoundTag(name);

            sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.home.list.format", name, posFormat.format(warp.getDouble("x")), posFormat.format(warp.getDouble("y")), posFormat.format(warp.getDouble("z")), warp.getInteger("dimension")).setStyle(new Style().setColor(i % 2 == 0 ? TextFormatting.DARK_AQUA : TextFormatting.AQUA)));
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, LinkedList<String> args, @Nullable BlockPos pos) {
        if (!(sender instanceof EntityPlayerMP)) return Collections.emptyList();
        NBTTagCompound warps = HxCPlayerInfoHandler.getTagCompound((EntityPlayer) sender, "homes", new NBTTagCompound());
        int homeAmount = warps.getKeySet().size();
        int homesPerPage = 7;
        int pages = (int) Math.ceil((float) homeAmount / (float) homesPerPage);
        return IntStream.rangeClosed(1, Math.max(1, pages)).mapToObj(Integer::toString).collect(Collectors.toList());
    }
}
