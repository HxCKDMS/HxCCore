package HxCKDMS.HxCCore.Commands;

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
    public int getRequiredPermissionLevel()
    {
        return 0;
    }
    
    @Override
    public void handleCommand(ICommandSender sender, String[] arguments) {
        sender.addChatMessage(new ChatComponentText("\u00A71Commands:"));
        
        boolean b = false;
        final int len = 35;
        for (String line : LINES) {
            ChatComponentText message = new ChatComponentText(line);
            message.getChatStyle().setColor((b = !b) ? EnumChatFormatting.BLUE : EnumChatFormatting.DARK_AQUA);
            sender.addChatMessage(message);
        }
        
        //sender.addChatMessage(new ChatComponentText("\u00A73/HxCCore warp: used to return to a warp(Public WayPoint)."));
        //sender.addChatMessage(new ChatComponentText("\u00A79/HxCCore setWarp: used to return to a warp(Public WayPoint)."));
    }
    
    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        return null;
    }
    
    /** TODO: THESE SHOULD ALL BE LOADED FROM A LANGUAGE FILE **/
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
            "/HxCCore setHome: used to set a home (waypoint) at your location.",
            "/HxCCore home: used to return to a home (waypoint).",
            "/HxCCore repair: used to return to a home (waypoint).",
            "/HxCCore repairAll: used to return to a home (waypoint)." };
}
