package HxCKDMS.HxCCore.api.Handlers;

import HxCKDMS.HxCCore.HxCCore;
import HxCKDMS.HxCCore.api.Command.AbstractCommandMain;
import HxCKDMS.HxCCore.api.Command.ISubCommand;
import HxCKDMS.HxCCore.api.Utils.LogHelper;
import HxCKDMS.HxCCore.lib.References;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.*;

public class CommandsHandler extends AbstractCommandMain {
    public static HashMap<String, ISubCommand> commands = new HashMap<>();
    public static AbstractCommandMain instance = new CommandsHandler();
    //TODO: attempt to move ALL strings to localization before v2....
    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    public static void initCommands(FMLServerStartingEvent event) {
        event.registerServerCommand(instance);
    }

    public static String getSubName(String name) {
        return commands.get(name).getCommandName();
    }
    
    @Override
    public String getCommandName() {
        //Changed since more commonly used and since not only the core uses this now...
        return "HxC";
    }
    
    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/" + getCommandName() + " help";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws WrongUsageException, NumberInvalidException, PlayerNotFoundException {
        if (args.length > 0) {
            String k = args[0].toLowerCase();
            if (HxCCore.instance.HxCRules.get("LogCommands").equals("true"))
                LogHelper.info(sender.getCommandSenderName() + " Tried to send command /HxC " +
                        Arrays.asList(args).toString().replace(",", "").substring(1,
                                Arrays.asList(args).toString().replace(",", "").length()-1), References.MOD_NAME);
            // 0 is client / 1 is server / 2 is command block
            if (commands.containsKey(k)) {
                if ((commands.get(k).getCommandRequiredParams()[0] != -1) && (sender instanceof EntityPlayer) && args.length >= commands.get(k).getCommandRequiredParams()[0])
                    commands.get(k).handleCommand(sender, args, true);
                else if ((commands.get(k).getCommandRequiredParams()[1] != -1) && !(sender instanceof EntityPlayer) && sender.getCommandSenderName().equals(HxCCore.server.getCommandSenderName()) && args.length >= commands.get(k).getCommandRequiredParams()[1])
                    commands.get(k).handleCommand(sender, args, false);
                else if ((commands.get(k).getCommandRequiredParams()[2] != -1) && !sender.getCommandSenderName().equals(HxCCore.server.getCommandSenderName()) && args.length >= commands.get(k).getCommandRequiredParams()[2])
                    commands.get(k).handleCommand(sender, args, false);
                else
                    throw new WrongUsageException(HxCCore.util.getTranslation((sender instanceof EntityPlayerMP) ? ((EntityPlayerMP)sender).getUniqueID() : UUID.randomUUID(), "commands." + commands.get(k).getCommandName() + ".usage"));
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
        aliases.add("hxccore");
        aliases.add("hxC");
        aliases.add("hxc");
        aliases.add("Hxc");
        aliases.add("hxc");
        aliases.add("HXC");
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