package hxckdms.hxccore.commands;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import hxckdms.hxccore.api.command.AbstractSubCommand;
import hxckdms.hxccore.api.command.HxCCommand;
import hxckdms.hxccore.utilities.ServerTranslationHelper;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

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
    public String getCommandName() {
        return "modList";
    }

    @Override
    public void execute(ICommandSender sender, LinkedList<String> args) throws CommandException {
        int listSize = Loader.instance().getModList().size();
        int modsPerPage = 7;
        int pages = (int) Math.ceil((float) listSize / (float) modsPerPage);

        int page = args.size() == 1 ? CommandBase.parseIntBounded(sender, args.get(0), 1, pages) - 1 : 0;
        int min = Math.min(page * modsPerPage, listSize);

        sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.modList.header", page + 1, pages).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.AQUA)));

        for (int i = page * modsPerPage; i < modsPerPage + min; i++) {
            if (i >= listSize) break;

            ModContainer mod = Loader.instance().getModList().get(i);
            sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.modList.format", mod.getName(), mod.getVersion()).setChatStyle(new ChatStyle().setColor(i % 2 == 0 ? EnumChatFormatting.DARK_AQUA : EnumChatFormatting.AQUA)));
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, LinkedList<String> args) {
        int listSize = Loader.instance().getModList().size();
        int modsPerPage = 7;
        int pages = (int) Math.ceil((float) listSize / (float) modsPerPage);
        return IntStream.rangeClosed(1, pages).mapToObj(Integer::toString).collect(Collectors.toList());
    }
}
