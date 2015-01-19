package HxCKDMS.HxCCore.Commands;

import gnu.trove.map.TMap;
import gnu.trove.map.hash.THashMap;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

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
        registerSubCommand(CommandSetPerms.instance);
        registerSubCommand(CommandServerInfo.instance);
        registerSubCommand(CommandSpawn.instance);
        registerSubCommand(CommandPlayerInfo.instance);
        registerSubCommand(CommandModList.instance);
        registerSubCommand(CommandTpa.instance);
        registerSubCommand(CommandNick.instance);
    }
    
    public static void initCommands(FMLServerStartingEvent event) {
        event.registerServerCommand(instance);
    }
    
    @Override
    public String getName() {
        return "HxCCore";
    }
    
    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/" + getName() + " help";
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 0;
    }

    @Override
    public void execute(ICommandSender sender, String[] args) throws WrongUsageException, PlayerNotFoundException {
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
    public List getAliases() {
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
    public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        
        if (args.length == 1) {
            return func_175762_a(args, commands.keySet());
        } else if (commands.containsKey(args[0])) {
            return commands.get(args[0]).addTabCompletionOptions(sender, args);
        }
        return null;
    }
}
