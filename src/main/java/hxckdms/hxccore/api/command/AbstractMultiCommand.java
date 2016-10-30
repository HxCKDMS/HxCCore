package hxckdms.hxccore.api.command;

import hxckdms.hxccore.registry.CommandRegistry;
import hxckdms.hxccore.utilities.PermissionHandler;
import hxckdms.hxccore.utilities.ServerTranslationHelper;
import net.minecraft.command.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import javax.annotation.Nullable;
import java.util.*;

@SuppressWarnings("unchecked")
public abstract class AbstractMultiCommand extends CommandBase implements IMultiCommand {
    protected HashMap<String, AbstractSubCommand> subCommands = new HashMap<>();

    @Override
    public void executeSubCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 0) throw new WrongUsageException("Type '" + getCommandUsage(sender) + "' for help.");
        String commandName = args[0].toLowerCase();
        if (!subCommands.containsKey(commandName)) throw new CommandNotFoundException(ServerTranslationHelper.getTranslation(sender, "commands.sub.exception.notFound").getUnformattedText());
        AbstractSubCommand command = subCommands.get(commandName);

        if (!command.getCommandState().isUsageAllowed()) throw new CommandException(ServerTranslationHelper.getTranslation(sender, command.getCommandState().getErrorText()).getUnformattedText());
        if (!PermissionHandler.canUseSubCommand(sender, command)) throw new CommandException(ServerTranslationHelper.getTranslation(sender, "commands.generic.permission").getUnformattedText());

        LinkedList<String> subArgs = new LinkedList<>(Arrays.asList(args));
        subArgs.removeFirst();

        command.execute(sender, subArgs);
    }

    @Override
    public void registerSubCommands(FMLPreInitializationEvent event) {
        Set<ASMDataTable.ASMData> asmDataTable = event.getAsmData().getAll(HxCCommand.class.getCanonicalName());

        if (asmDataTable != null && !asmDataTable.isEmpty()) {
            for (ASMDataTable.ASMData data : asmDataTable) {
                try {
                    if (Class.forName(data.getClassName()).getSuperclass() != AbstractSubCommand.class) continue;
                    Class<? extends AbstractSubCommand> clazz = (Class<? extends AbstractSubCommand>) Class.forName(data.getClassName());

                    AbstractSubCommand subCommand = clazz.newInstance();
                    if (subCommand.getParentCommand() == this.getClass()) subCommands.put(subCommand.getCommandName().toLowerCase(), subCommand);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static HashMap<String, AbstractSubCommand> getSubCommands(String name) {
        return (HashMap<String, AbstractSubCommand>) CommandRegistry.getCommandForName(name).subCommands.clone();
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
        return super.getTabCompletionOptions(server, sender, args, pos);
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        executeSubCommand(sender, args);
    }
}
