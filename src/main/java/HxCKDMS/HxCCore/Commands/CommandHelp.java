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
    public void handleCommand(ICommandSender sender, String[] arguments) {
        sender.addChatMessage(new ChatComponentText("\u00A71Commands:"));

        boolean b = false;
        for (String line : References.COMMANDS) {
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