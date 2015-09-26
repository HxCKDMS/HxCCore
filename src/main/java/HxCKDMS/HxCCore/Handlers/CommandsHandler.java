package HxCKDMS.HxCCore.Handlers;

import HxCKDMS.HxCCore.api.Command.AbstractCommandMain;
import HxCKDMS.HxCCore.api.Command.ISubCommand;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.WrongUsageException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommandsHandler extends AbstractCommandMain {
    public static HashMap<String, ISubCommand> commands = new HashMap<>();
    public static AbstractCommandMain instance = new CommandsHandler();
    //TODO: attempt to move ALL strings to localization before 1.9.x....
    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
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
    public void processCommand(ICommandSender sender, String[] args) throws WrongUsageException, NumberInvalidException, PlayerNotFoundException {
        if (args.length > 0) {
            String k = args[0].toLowerCase();
            // 0 is client / 1 is server / 2 is command block
            if (commands.containsKey(k)) {
                if (args.length >= commands.get(k).getCommandRequiredParams()[0]) {
                    commands.get(k).handleCommandFromClient(sender, args);
                } else if (args.length >= commands.get(k).getCommandRequiredParams()[1]) {
                    commands.get(k).handleCommandFromServer(sender, args);
                } else {
                    commands.get(k).handleCommandFromServer(sender, args);
                }
            } else {
                throw new WrongUsageException("Type '" + getCommandUsage(sender) + "' for help.");
            }
        } else {
            throw new WrongUsageException("Type '" + getCommandUsage(sender) + "' for help.");
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List getCommandAliases() {
        List aliases = new ArrayList();
        aliases.add("HxCCore");
        aliases.add("HxC");
        return aliases;
    }
    
    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args) {
        if (args.length == 1) {
            return getListOfStringsMatchingLastWord(args, commands.keySet().toString().replace('[', ' ').replace(']', ' ').trim().split(", "));
        } else if (commands.containsKey(args[0])) {
            return commands.get(args[0]).addTabCompletionOptions(sender, args);
        }
        return null;
    }

    public boolean registerSubCommand(ISubCommand subCommand){
        String k = subCommand.getCommandName().toLowerCase();

        if (!commands.containsKey(k)) {
            commands.put(k, subCommand);
            return true;
        }
        return false;
    }

    @Override
    public AbstractCommandMain getInstance() {
        return instance;
    }
}
