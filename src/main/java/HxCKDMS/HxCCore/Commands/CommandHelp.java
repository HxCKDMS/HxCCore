package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.api.ISubCommand;
import HxCKDMS.HxCCore.lib.References;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import java.util.List;

public class CommandHelp implements ISubCommand {

    public static CommandHelp instance = new CommandHelp();

    @Override
    public String getCommandName() {
        return "help";
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] args) {
        int commandsPerPage = 7;
        int pages = (int)Math.ceil(References.COMMANDS.length / (float)commandsPerPage);

        int page = args.length == 1 ? 0 : Integer.parseInt(args[1])-1;
        int min = Math.min(page * commandsPerPage, References.COMMANDS.length);

        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.AQUA + String.format("Help page: %1$d/%2$d.", page + 1, pages)));
        boolean b = false;
        for (int i = page * commandsPerPage; i < commandsPerPage + min; i++) {
            if(i >= References.COMMANDS.length) break;

            String line = References.COMMANDS[i];
            line = "commands." + line + ".help";
            line = StatCollector.translateToLocal(line);
            line = "/HxC " + line;
            ChatComponentText message = new ChatComponentText(line);
            message.getChatStyle().setColor((b = !b) ? EnumChatFormatting.BLUE : EnumChatFormatting.DARK_AQUA);
            sender.addChatMessage(message);
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        return null;
    }
}