package hxckdms.hxccore.registry.command;

import hxckdms.hxcconfig.Config;
import hxckdms.hxcconfig.HxCConfig;
import hxckdms.hxcconfig.handlers.SpecialHandlers;
import hxckdms.hxccore.api.command.AbstractCommandMain;
import hxckdms.hxccore.api.command.ISubCommand;
import hxckdms.hxccore.utilities.PermissionHandler;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

import javax.annotation.Nullable;
import java.util.*;

import static hxckdms.hxcconfig.Flags.RETAIN_ORIGINAL_VALUES;
import static hxckdms.hxccore.libraries.Constants.MOD_ID;
import static hxckdms.hxccore.libraries.GlobalVariables.modConfigDir;
import static hxckdms.hxccore.registry.command.CommandRegistry.CommandConfig.commands;

@SuppressWarnings({"unchecked", "WeakerAccess"})
public class CommandRegistry {
    private static HashMap<String, ISubCommand> subCommands = new HashMap<>();

    public static void registerCommands(FMLPreInitializationEvent event) {
        SpecialHandlers.registerSpecialClass(SubCommandsHandler.class);
        SpecialHandlers.registerSpecialClass(SubPermissions.class);

        HxCConfig commandConfig = new HxCConfig(CommandConfig.class, "HxCCommands", modConfigDir, "cfg", MOD_ID);
        commandConfig.initConfiguration();

        Set<ASMDataTable.ASMData> asmDataTable = event.getAsmData().getAll(HxCCommand.class.getCanonicalName());

        if (asmDataTable != null && !asmDataTable.isEmpty()) {
            for (ASMDataTable.ASMData data : asmDataTable) {
                try {
                    Class<ISubCommand> clazz = (Class<ISubCommand>) Class.forName(data.getClassName());
                    Class<? extends AbstractCommandMain> mainClazz = clazz.getAnnotation(HxCCommand.class).mainCommand();

                    if (mainClazz == CommandHxC.class) {
                        ISubCommand subCommand = clazz.newInstance();
                        SubCommandsHandler subCommandsHandler = commands.getOrDefault(subCommand.getCommandName(), new SubCommandsHandler());

                        if (!commands.containsKey(subCommand.getCommandName())) {
                            subCommandsHandler.permissionLevel = clazz.getAnnotation(HxCCommand.class).defaultPermission();
                            subCommandsHandler.enable = clazz.getAnnotation(HxCCommand.class).isEnabled();
                            commands.put(subCommand.getCommandName(), subCommandsHandler);
                        }

                        if (subCommandsHandler.enable) subCommands.put(subCommand.getCommandName().toLowerCase(), subCommand);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        commandConfig.initConfiguration();
    }

    public static void initializeCommands(FMLServerStartingEvent event) {
        if (!CommandConfig.enableCommands) return;
        event.registerServerCommand(CommandHxC.INSTANCE);
    }

    public static class CommandHxC extends AbstractCommandMain {
        public static final AbstractCommandMain INSTANCE = new CommandHxC();

        @Override
        public String getCommandName() {
            return "HxC";
        }

        @Override
        public String getCommandUsage(ICommandSender sender) {
            return String.format("/%s help for help", getCommandName());
        }

        @Override
        public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
            if (args.length == 0) throw new WrongUsageException("Type '" + getCommandUsage(sender) + "' for help.");
            String commandName = args[0].toLowerCase();
            if (!subCommands.containsKey(commandName)) throw new WrongUsageException("Type '" + getCommandUsage(sender) + "' for help.");
            ISubCommand command = subCommands.get(commandName);

            if (!PermissionHandler.canUseSubCommand(sender, command)) throw new CommandException("commands.generic.permission");

            LinkedList<String> subArgs = new LinkedList<>(Arrays.asList(args));
            subArgs.removeFirst();

            command.execute(sender, subArgs);

        }

        @Override
        public List<String> getCommandAliases() {
            return Arrays.asList("HxCCore", "HxC", "hxccore", "hxC", "hxc", "Hxc", "HXC");
        }

        @Override
        public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
            return true;
        }

        @Override
        public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
            if (args.length == 1) {
                return getListOfStringsMatchingLastWord(args, subCommands.keySet());
            } else if (subCommands.containsKey(args[0])) {
                LinkedList<String> lArgs = new LinkedList<>(Arrays.asList(args));
                lArgs.removeFirst();
                return subCommands.get(args[0]).addTabCompletionOptions(sender, lArgs, pos);
            }
            return getCommandAliases();
        }
    }

    @Config
    public static class CommandConfig {
        public static boolean enableCommands = true;

        @Config.flags(RETAIN_ORIGINAL_VALUES)
        public static LinkedHashMap<String, SubCommandsHandler> commands = new LinkedHashMap<>();

        public static LinkedHashMap<String, Integer> vanillaPermissionOverride = new LinkedHashMap<>();

        public static LinkedHashMap<Integer, SubPermissions> commandPermissions = new LinkedHashMap<Integer, SubPermissions>(){{
            put(1, new SubPermissions("Default", 3, 0));
            put(2, new SubPermissions("&eHelper", 5, 512));
            put(3, new SubPermissions("&9Moderator", 10, 4096));
            put(4, new SubPermissions("&6Admin", 16, 32768));
            put(5, new SubPermissions("&4&lOwner", -1, -1));
        }};
    }

    public static class SubPermissions {
        public String name;
        public int homeAmount;
        public int maxBlocksProtected;

        public SubPermissions(String name, int homeAmount, int maxBlocksProtected) {
            this.name = name;
            this.homeAmount = homeAmount;
            this.maxBlocksProtected = maxBlocksProtected;
        }

        public SubPermissions() {
        }

        @Override
        public String toString() {
            return "SubPermissions{" +
                    "name='" + name + '\'' +
                    ", homeAmount=" + homeAmount +
                    ", maxBlocksProtected=" + maxBlocksProtected +
                    '}';
        }
    }

    public static class SubCommandsHandler {
        public int permissionLevel;
        public boolean enable;

        @Override
        public String toString() {
            return "SubCommandsHandler{" +
                    "permissionLevel=" + permissionLevel +
                    ", enable=" + enable +
                    '}';
        }
    }
}
