package hxckdms.hxccore.registry;

import hxckdms.hxcconfig.Config;
import hxckdms.hxcconfig.HxCConfig;
import hxckdms.hxcconfig.handlers.SpecialHandlers;
import hxckdms.hxccore.api.command.AbstractMultiCommand;
import hxckdms.hxccore.api.command.HxCCommand;
import hxckdms.hxccore.api.command.SubCommandConfigHandler;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

import static hxckdms.hxcconfig.Flags.RETAIN_ORIGINAL_VALUES;
import static hxckdms.hxccore.libraries.Constants.MOD_ID;
import static hxckdms.hxccore.libraries.GlobalVariables.modConfigDir;

@SuppressWarnings({"unchecked", "WeakerAccess", "unused"})
public class CommandRegistry {
    private static HashSet<AbstractMultiCommand> multiCommands = new HashSet<>();

    public static void registerCommands(FMLPreInitializationEvent event) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        SpecialHandlers.registerSpecialClass(SubCommandConfigHandler.class);
        SpecialHandlers.registerSpecialClass(SubPermissions.class);

        HxCConfig commandConfig = new HxCConfig(CommandConfig.class, "HxCCommands", modConfigDir, "cfg", MOD_ID);
        commandConfig.initConfiguration();

        Set<ASMDataTable.ASMData> asmDataTable = event.getAsmData().getAll(HxCCommand.class.getCanonicalName());

        if (asmDataTable != null && !asmDataTable.isEmpty()) {
            for (ASMDataTable.ASMData data : asmDataTable) {
                try {
                    if (Class.forName(data.getClassName()).getSuperclass() != AbstractMultiCommand.class) continue;
                    Class<? extends AbstractMultiCommand> clazz = (Class<? extends AbstractMultiCommand>) Class.forName(data.getClassName());

                    AbstractMultiCommand multiCommand = clazz.newInstance();
                    multiCommands.add(multiCommand);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        multiCommands.forEach(command -> command.registerSubCommands(event));

        commandConfig.initConfiguration();
    }

    public static void initializeCommands(FMLServerStartingEvent event) {
        if (!CommandConfig.enableCommands) return;
        multiCommands.forEach(event::registerServerCommand);
    }

    @Config
    public static class CommandConfig {
        public static boolean enableCommands = true;

        @Config.flags(RETAIN_ORIGINAL_VALUES)
        public static LinkedHashMap<String, SubCommandConfigHandler> commands = new LinkedHashMap<>();

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
}
