package hxckdms.hxccore.commands;

import hxckdms.hxccore.api.command.AbstractSubCommand;
import hxckdms.hxccore.api.command.HxCCommand;
import hxckdms.hxccore.utilities.ServerTranslationHelper;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;

import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@HxCCommand
public class CommandModList extends AbstractSubCommand<CommandHxC> {
    {
        permissionLevel = 1;
    }

    @Override
    public String getName() {
        return "modList";
    }

    @Override
    public void execute(ICommandSender sender, LinkedList<String> args) throws CommandException {
        int listSize = Loader.instance().getModList().size();
        int modsPerPage = 7;
        int pages = (int) Math.ceil((float) listSize / (float) modsPerPage);

        int page = args.size() == 1 ? CommandBase.parseInt(args.get(0), 1, pages) - 1 : 0;
        int min = Math.min(page * modsPerPage, listSize);

        sender.sendMessage(ServerTranslationHelper.getTranslation(sender, "commands.modList.header", page + 1, pages).setStyle(new Style().setColor(TextFormatting.AQUA)));

        for (int i = page * modsPerPage; i < modsPerPage + min; i++) {
            if (i >= listSize) break;

            ModContainer mod = Loader.instance().getModList().get(i);
            sender.sendMessage(ServerTranslationHelper.getTranslation(sender, "commands.modList.format", mod.getName(), mod.getVersion()).setStyle(new Style().setColor(i % 2 == 0 ? TextFormatting.DARK_AQUA : TextFormatting.AQUA)));
        }
    }

    @Override
    public List<String> addTabCompletions(ICommandSender sender, LinkedList<String> args, @Nullable BlockPos pos) {
        int listSize = Loader.instance().getModList().size();
        int modsPerPage = 7;
        int pages = (int) Math.ceil((float) listSize / (float) modsPerPage);
        return IntStream.rangeClosed(1, pages).mapToObj(Integer::toString).collect(Collectors.toList());
    }
}
