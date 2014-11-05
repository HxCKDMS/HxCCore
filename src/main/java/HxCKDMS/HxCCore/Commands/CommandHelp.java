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
        sender.addChatMessage(new ChatComponentText("\u00A71commands:"));
        sender.addChatMessage(new ChatComponentText("\u00A79/HxCCore help: shows all commands with explanation."));
        sender.addChatMessage(new ChatComponentText("\u00A73/HxCCore heal [player]: used to heal your self or another player."));
        sender.addChatMessage(new ChatComponentText("\u00A79/HxCCore kill [player]: used to kill your self or another player."));
        sender.addChatMessage(new ChatComponentText("\u00A73/HxCCore god [player]: used to put your self or another player in god mode."));
        sender.addChatMessage(new ChatComponentText("\u00A79/HxCCore fly [player]: used to grand the power of flight to your self or another player."));
        sender.addChatMessage(new ChatComponentText("\u00A73/HxCCore feed [player]: used to feed your self or another player."));
        sender.addChatMessage(new ChatComponentText("\u00A79/HxCCore burn [player]: used to burn your self or another player."));
        sender.addChatMessage(new ChatComponentText("\u00A73/HxCCore extinguish [player]: used to extinguish your self or another player."));
        sender.addChatMessage(new ChatComponentText("\u00A79/HxCCore smite [player]: used to smite your self or another player."));
        sender.addChatMessage(new ChatComponentText("\u00A73/HxCCore setHome: used to set a home(WayPoint) at your location."));
        sender.addChatMessage(new ChatComponentText("\u00A79/HxCCore home: used to return to a home(WayPoint)."));
        sender.addChatMessage(new ChatComponentText("\u00A73/HxCCore repair: used to return to a home(WayPoint)."));
        sender.addChatMessage(new ChatComponentText("\u00A79/HxCCore repairAll: used to return to a home(WayPoint)."));
//        sender.addChatMessage(new ChatComponentText("\u00A73/HxCCore warp: used to return to a warp(Public WayPoint)."));
//        sender.addChatMessage(new ChatComponentText("\u00A79/HxCCore setWarp: used to return to a warp(Public WayPoint)."));
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        return null;
    }
}
