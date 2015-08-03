package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.api.ISubCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

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
        for (String line : LINES) {
            ChatComponentText message = new ChatComponentText(line);
            message.getChatStyle().setColor((b = !b) ? EnumChatFormatting.BLUE : EnumChatFormatting.DARK_AQUA);
            sender.addChatMessage(message);
        }
    }
    
    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        return null;
    }
    
    // TODO: THESE SHOULD ALL BE LOADED FROM A LANGUAGE FILE
    private static final String[] LINES = {
            "/HxCCore help: shows all commands with explanation.",
            "/HxCCore heal [player]: used to heal your self or another player.",
            "/HxCCore kill [player]: used to kill your self or another player.",
            "/HxCCore god [player]: used to put your self or another player in god mode.",
            "/HxCCore fly [player]: used to grand the power of flight to your self or another player.",
            "/HxCCore feed [player]: used to feed your self or another player.",
            "/HxCCore burn [player]: used to burn your self or another player.",
            "/HxCCore extinguish [player]: used to extinguish your self or another player.",
            "/HxCCore smite [player]: used to smite your self or another player.",
            "/HxCCore setHome [home]: used to set a home (waypoint) at your location.",
            "/HxCCore home [home]: used to return to a home (waypoint).",
            "/HxCCore setWarp [warp]: used to set a server warp (waypoint) at your location.",
            "/HxCCore warp [warp]: used to return to a server warp (waypoint).",
            "/HxCCore repair: used to repair currently held item.",
            "/HxCCore repairAll [player]: used to repair all items in target player's inventory.",
            "/HxCCore modlist: used to list all mods installed.",
            "/HxCCore nick <nick>: used to rename yourself.",
            "/HxCCore playerinfo <player>: used to get info about a player.",
            "/HxCCore serverinfo: used to get info about the server.",
            "/HxCCore setperms <player> [permlevel#]: used to set permmision levels for players on your server.",
            "/HxCCore spawn: used to goto spawn.",
            "/HxCCore tpa [accept|deny|player]: used to repair all items in your inventory."
    };
}
