package HxCKDMS.HxCCore.Commands;

import cpw.mods.fml.common.event.FMLServerStartingEvent;
import gnu.trove.map.TMap;
import gnu.trove.map.hash.THashMap;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;

import java.util.ArrayList;
import java.util.List;

public class CommandBase extends net.minecraft.command.CommandBase {
    
    public static CommandBase instance = new CommandBase();
    private static TMap<String, ISubCommand> commands = new THashMap<String, ISubCommand>();
    
    static {
        registerSubCommand(CommandHeal.instance);
        registerSubCommand(CommandHelp.instance);
        registerSubCommand(CommandFeed.instance);
        registerSubCommand(CommandKill.instance);
        registerSubCommand(CommandFly.instance);
        registerSubCommand(CommandGod.instance);
        registerSubCommand(CommandBurn.instance);
        registerSubCommand(CommandExtinguish.instance);
        registerSubCommand(CommandSetHome.instance);
        registerSubCommand(CommandHome.instance);
        registerSubCommand(CommandSmite.instance);
        registerSubCommand(CommandRepair.instance);
        registerSubCommand(CommandRepairAll.instance);
        registerSubCommand(CommandWarp.instance);
        registerSubCommand(CommandSetWarp.instance);
//        registerSubCommand(CommandColor.instance);
    }
    
    public static void initCommands(FMLServerStartingEvent event) {
        event.registerServerCommand(instance);
    }
    
    @Override
    public String getCommandName() {
        return "HxCCore";
    }
    
    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/" + getCommandName() + " help";
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 4;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length > 0) {
            String k = args[0].toLowerCase();
            if (commands.containsKey(k)) {
                commands.get(k).handleCommand(sender, args);
            } else {
                throw new WrongUsageException("Type '" + getCommandUsage(sender) + "' for help.");
            }
        } else {
            throw new WrongUsageException("Type '" + getCommandUsage(sender) + "' for help.");
        }
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List getCommandAliases() {
        List aliases = new ArrayList();
        aliases.add("HxCCore");
        aliases.add("HxC");
        return aliases;
    }
    
    public static boolean registerSubCommand(ISubCommand subCommand) {
        String k = subCommand.getCommandName().toLowerCase();
        
        if (!commands.containsKey(k)) {
            commands.put(k, subCommand);
            return true;
        }
        return false;
    }
    
    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args) {
        
        if (args.length == 1) {
            return getListOfStringsFromIterableMatchingLastWord(args, commands.keySet());
        } else if (commands.containsKey(args[0])) {
            return commands.get(args[0]).addTabCompletionOptions(sender, args);
        }
        return null;
    }
}
