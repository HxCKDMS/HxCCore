package hxckdms.hxccore.api.command;

import hxckdms.hxccore.registry.CommandRegistry;
import hxckdms.hxccore.utilities.PermissionHandler;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import javax.annotation.Nullable;
import java.util.*;

public abstract class AbstractMultiCommand extends CommandBase implements IMultiCommand {
    protected HashMap<String, AbstractSubCommand> subCommands = new HashMap<>();

    @Override
    public void executeSubCommand(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 0) throw new WrongUsageException("Type '" + getUsage(sender) + "' for help.");
        String commandName = args[0].toLowerCase();
        if (!subCommands.containsKey(commandName)) throw new CommandException("commands.sub.exception.notFound");//TranslatedCommandException(sender, "commands.sub.exception.notFound");
        AbstractSubCommand command = subCommands.get(commandName);

        if (!command.getCommandState().isUsageAllowed()) throw new CommandException(command.getCommandState().getErrorText()); //new CommandException(ServerTranslationHelper.getTranslation(sender, command.getCommandState().getErrorText()).getUnformattedText());
        if (!PermissionHandler.canUseSubCommand(sender, command)) throw new CommandException("commands.generic.permission");//TranslatedCommandException(sender, "commands.generic.permission");

        LinkedList<String> subArgs = new LinkedList<>(Arrays.asList(args));
        subArgs.removeFirst();

        command.execute(server, sender, subArgs);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void registerSubCommands(FMLPreInitializationEvent event) {
        Set<ASMDataTable.ASMData> asmDataTable = event.getAsmData().getAll(HxCCommand.class.getCanonicalName());

        if (!asmDataTable.isEmpty()) {
            for (ASMDataTable.ASMData data : asmDataTable) {
                try  {
                    if (Class.forName(data.getClassName()).getSuperclass() != AbstractSubCommand.class) continue;
                    Class<? extends AbstractSubCommand> clazz = (Class<? extends AbstractSubCommand>) Class.forName(data.getClassName());

                    AbstractSubCommand subCommand = clazz.newInstance();
                    if (subCommand.getParentCommand() == getClass()) subCommands.put(subCommand.getName().toLowerCase(), subCommand);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static HashMap<String, AbstractSubCommand> getSubCommands(String name) {
        return (HashMap<String, AbstractSubCommand>) CommandRegistry.getCommandForName(name).subCommands.clone();
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        return super.getTabCompletions(server, sender, args, targetPos);
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        executeSubCommand(server, sender, args);
    }
}
