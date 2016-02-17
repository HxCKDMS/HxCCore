package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Handlers.CommandsHandler;
import HxCKDMS.HxCCore.api.Command.HxCCommand;
import HxCKDMS.HxCCore.api.Command.ISubCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.WrongUsageException;

import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import java.util.List;

@HxCCommand(defaultPermission = 0, mainCommand = CommandsHandler.class, isEnabled = true)
public class CommandHelp implements ISubCommand {
    public static CommandHelp instance = new CommandHelp();
    //TODO: Make a /HxC help [command].... and get the help for specified command..
    @Override
    public String getCommandName() {
        return "Help";
    }

    @Override
    public int[] getCommandRequiredParams() {
        return new int[]{0, 0, -1};
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] args, boolean isPlayer) throws WrongUsageException, PlayerNotFoundException{
        if (args.length == 2 && Integer.valueOf(args[1]) == null) {
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.AQUA + StatCollector.translateToLocal("commands." + CommandsHandler.commands.get(args[1].toLowerCase()).getCommandName() + ".usage")));
        } else {
            int commandsPerPage = 7;
            int pages = (int) Math.ceil(CommandsHandler.commands.size() / (float) commandsPerPage);

            int page = args.length == 1 ? 0 : Integer.parseInt(args[1]) - 1;
            int min = Math.min(page * commandsPerPage, CommandsHandler.commands.size());

            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.AQUA + String.format("Help page: %1$d/%2$d.", page + 1, pages)));
            boolean b = false;
            for (int i = page * commandsPerPage; i < commandsPerPage + min; i++) {
                if (i >= CommandsHandler.commands.size()) break;

                String line = ((ISubCommand) CommandsHandler.commands.values().toArray()[i]).getCommandName();
                line = "commands." + line.toLowerCase() + ".usage";
                String info = line.replace("usage", "info");
                line = StatCollector.translateToLocal(line);
                info = StatCollector.translateToLocal(info);
                line = "/HxC " + line + " : used to " + info;
                ChatComponentText message = new ChatComponentText(line);
                message.getChatStyle().setColor((b = !b) ? EnumChatFormatting.BLUE : EnumChatFormatting.DARK_AQUA);
                sender.addChatMessage(message);
            }
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        return null;
    }
}