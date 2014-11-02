package HxCKDMS.HxCCore.Commands;

import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

import java.util.List;

public class CommandHelp implements ISubCommand {

    public static CommandHelp instance = new CommandHelp();

    @Override
    public String getCommandName() {
        return "help";
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] arguments) {
        sender.addChatMessage(new ChatComponentText("commands:"));
        sender.addChatMessage(new ChatComponentText("/HxCCore help: shows all commands with explanation."));
        sender.addChatMessage(new ChatComponentText("/HxCCore heal [player]: used to heal your self or another player."));
        sender.addChatMessage(new ChatComponentText("/HxCCore kill [player]: used to kill your self or another player."));
        sender.addChatMessage(new ChatComponentText("/HxCCore god [player]: used to put your self or another player in god mode."));
        sender.addChatMessage(new ChatComponentText("/HxCCore fly [player]: used to grand the power of flight to your self or another player."));
        sender.addChatMessage(new ChatComponentText("/HxCCore feed [player]: used to feed your self or another player."));
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        return null;
    }
}
