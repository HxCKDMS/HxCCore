package HxCKDMS.HxCCore;

import HxCKDMS.HxCCore.Configs.CommandsConfig;
import HxCKDMS.HxCCore.Configs.Configurations;
import HxCKDMS.HxCCore.Contributors.CodersCheck;
import HxCKDMS.HxCCore.Crash.CrashHandler;
import HxCKDMS.HxCCore.Crash.CrashReportThread;
import HxCKDMS.HxCCore.Events.*;
import HxCKDMS.HxCCore.Handlers.CommandsHandler;
import HxCKDMS.HxCCore.Handlers.HxCReflectionHandler;
import HxCKDMS.HxCCore.Registry.CommandRegistry;
import HxCKDMS.HxCCore.api.Command.HxCCommand;
import HxCKDMS.HxCCore.api.Configuration.HxCConfig;
import HxCKDMS.HxCCore.api.Utils.LogHelper;
import HxCKDMS.HxCCore.lib.References;
import HxCKDMS.HxCCore.network.MessageColor;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.network.NetworkCheckHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.Potion;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.common.MinecraftForge;

import java.io.*;
import java.net.URL;
import java.util.*;

import static HxCKDMS.HxCCore.lib.References.*;

@SuppressWarnings({"unused", "ResultOfMethodCallIgnored"})
@Mod(modid = MOD_ID, name = MOD_NAME, version = VERSION, dependencies = DEPENDENCIES, acceptableRemoteVersions = "*")
public class HxCCore {
    @Instance(MOD_ID)
    public static HxCCore instance;

    private static LinkedHashMap<String, String> vers = new LinkedHashMap<>();
    public static final List<String> knownMods = Arrays.asList("HxCCore", "HxCSkills", "HxCEnchants", "HxCWorldGen", "HxCLinkPads", "HxCBlocks",
            "HxCFactions", "HxCTiC", "HxCArcanea", "HxCBows", "HxCDiseases", "HxCArmory");
    public static MinecraftServer server;
    public static HashMap<EntityPlayerMP, EntityPlayerMP> tpaRequestList = new HashMap<>();
    public static HashMap<EntityPlayerMP, Integer> TpaTimeoutList = new HashMap<>();
    public static SimpleNetworkWrapper network;

    public static File HxCCoreDir, HxCConfigDir, HxCConfigFile, commandCFGFile, kitsFile, HxCLogDir;
    public static HxCConfig hxCConfig = new HxCConfig(), commandCFG = new HxCConfig(),
    kits = new HxCConfig();

    public static volatile LinkedHashMap<UUID, String> HxCLabels = new LinkedHashMap<>();

    private static PrintWriter commandLog;

    public static final Thread crashReportThread = new Thread(new CrashReportThread()),
            CodersCheckThread = new Thread(new CodersCheck());


    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        FMLCommonHandler.instance().registerCrashCallable(new CrashHandler());
        crashReportThread.setName("HxCKDMS Crash check thread");
        Runtime.getRuntime().addShutdownHook(crashReportThread);

        HxCConfigDir = new File(event.getModConfigurationDirectory(), "HxCKDMS");
        if (!HxCConfigDir.exists()) HxCConfigDir.mkdirs();

        HxCConfigFile = new File(HxCConfigDir, "HxCCore.cfg");
        commandCFGFile = new File(HxCConfigDir, "HxCCommands.cfg");
        kitsFile = new File(HxCConfigDir, "HxC-Kits.cfg");

        Configurations.preInitConfigs();

        for (int i = 0; i < Configurations.Permissions.size(); i++) {
            PERM_NAMES[i] = (String) Configurations.Permissions.keySet().toArray()[i];
            PERM_COLOURS[i] = Configurations.Permissions.get(PERM_NAMES[i]).charAt(0);
            HOMES[i] = Integer.parseInt(Configurations.Permissions.get(PERM_NAMES[i]).substring(1).trim());
        }

        network = NetworkRegistry.INSTANCE.newSimpleChannel(PACKET_CHANNEL_NAME);
        network.registerMessage(MessageColor.Handler.class, MessageColor.class, 0, Side.CLIENT);

        CodersCheckThread.setName("HxCKDMS Contributors check thread");
        CodersCheckThread.start();
        extendEnchantsArray();

        if (Configurations.enableCommands)
            CommandRegistry.registerCommands(new CommandsHandler(),
                    event.getAsmData().getAll(HxCCommand.class.getCanonicalName()));

        if (!Loader.isModLoaded("BiomesOPlenty")) extendPotionsArray();
        //NEED TO IMPLEMENT Reika's Packet changes...

        LogHelper.info("If you see any debug messages, feel free to bug one of the authors about it ^_^", MOD_NAME);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        FMLCommonHandler.instance().bus().register(new EventNickSync());
        FMLCommonHandler.instance().bus().register(new EventTpRequest());
        FMLCommonHandler.instance().bus().register(new EventJoinWorld());
        FMLCommonHandler.instance().bus().register(new EventPlayerNetworkCheck());

        MinecraftForge.EVENT_BUS.register(new EventGod());
        MinecraftForge.EVENT_BUS.register(new EventXPtoBuffs());
        MinecraftForge.EVENT_BUS.register(new EventChat());
        MinecraftForge.EVENT_BUS.register(new EventPowerTool());
        MinecraftForge.EVENT_BUS.register(new EventPlayerDeath());
        if (Configurations.enableCommands && CommandsConfig.EnabledCommands.containsKey("Path") && CommandsConfig.EnabledCommands.get("Path"))
            MinecraftForge.EVENT_BUS.register(new EventBuildPath());
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        versionCheck();
        knownMods.forEach(mod -> {
            if (Loader.isModLoaded(mod))
                LogHelper.info("Thank you for using " + mod, MOD_NAME);
        });
    }

    @EventHandler
    public void serverStart(FMLServerStartingEvent event) {
        server = event.getServer();

        Loader.instance().getModList().forEach(m -> {
            if (knownMods.contains(m.getModId())) {
                String s = getNewVer(m.getModId(), m.getVersion());
                if (!s.isEmpty())
                    server.logWarning("A New version of " + m.getModId() + " has been found please update ASAP New Version Found = " + s);
            }
        });

        if (Configurations.enableCommands) CommandsHandler.initCommands(event);

        File WorldDir = new File(event.getServer().getEntityWorld().getSaveHandler().getWorldDirectory(), "HxCData");
        if (!WorldDir.exists())
            WorldDir.mkdirs();
        HxCCoreDir = WorldDir;
        File LogDir = new File(ForgeModContainer.getConfig().getConfigFile().getPath().replace("config\\forge.cfg", "logs\\"), "HxCLogs");
        if (!LogDir.exists())
            LogDir.mkdirs();
        HxCLogDir = LogDir;

        File CustomWorldFile = new File(HxCCoreDir, "HxCWorld.dat");
        File PermissionsData = new File(HxCCoreDir, "HxC-Permissions.dat");
        File OLDLOG = new File(HxCLogDir, "HxC-CommandLog.log");

        try {
            if (!CustomWorldFile.exists())
                CustomWorldFile.createNewFile();
            if (!PermissionsData.exists())
                PermissionsData.createNewFile();
            if (OLDLOG.exists()) {
                OLDLOG.renameTo(new File(HxCLogDir, "HxC-Command.log"));
            }
            commandLog = new PrintWriter(new File(HxCLogDir, "HxC-Command.log"), "UTF-8");
        } catch (IOException ignored) {}
    }
    private boolean loggedCommand;
    public void logCommand(String str) {
        commandLog.println(str);
        loggedCommand = true;
    }

    @EventHandler
    public void serverStop(FMLServerStoppingEvent event) {
        try {
            commandLog.close();
            if (!loggedCommand)
                (new File(HxCLogDir, "HxC-Command.log")).delete();
            else
                (new File(HxCLogDir, "HxC-Command.log")).renameTo(new File(HxCLogDir, "HxC-CommandLog-" + Calendar.getInstance().getTime().toString().replace(":", "." + ".log")));
        } catch (Exception ignored) {}
    }

    private static void extendEnchantsArray() {
        /**
         * Made by DrZed inspired by
         * Biomes O'Plenty
         **/
        int enchantsOffset;
        LogHelper.info("Extending Enchants Array", MOD_NAME);
        enchantsOffset = Enchantment.enchantmentsList.length;
        Enchantment[] enchantmentsList = new Enchantment[enchantsOffset + 256];
        System.arraycopy(Enchantment.enchantmentsList, 0, enchantmentsList, 0, enchantsOffset);
        HxCReflectionHandler.setPrivateFinalValue(Enchantment.class, null, enchantmentsList, "enchantmentsList", "field_77331_b");
        LogHelper.info("Enchants Array now: " + Enchantment.enchantmentsList.length, MOD_NAME);
    }

    private static void extendPotionsArray() {
        /**
         * Taken From Biomes O'Plenty
         * modified to fit my interest
         * The License for BOP is CC
         * I Thank BOP Team for this
         **/
        //TODO: Reika's Packet hack
        int potionOffset;
        LogHelper.info("Extending Potions Array.", MOD_NAME);
        potionOffset = Potion.potionTypes.length;
        Potion[] potionTypes = new Potion[potionOffset + 256];
        System.arraycopy(Potion.potionTypes, 0, potionTypes, 0, potionOffset);
        HxCReflectionHandler.setPrivateFinalValue(Potion.class, null, potionTypes, "potionTypes", "field_76425_a");
    }

    @NetworkCheckHandler
    public boolean checkNetwork(Map<String, String> mods, Side side) {
        return !mods.containsKey("HxCCore") || mods.get("HxCCore").equals(VERSION);
    }

    public static String getNewVer(String mod, String Version) {
        if (!vers.get(mod+":1.7.10").equalsIgnoreCase(Version))
            return vers.get(mod+":1.7.10");
        return "";
    }

    public static void versionCheck() {
        vers = new LinkedHashMap<>();
        try {
            URL url = new URL("https://raw.githubusercontent.com/HxCKDMS/HxCLib/master/HxCVersions.txt");
            InputStream inputStream = url.openStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String inputLine;
            while ((inputLine = bufferedReader.readLine()) != null) {
                if (!inputLine.startsWith("//")) {
                    String[] str = inputLine.split("-");
                    vers.put(str[0], str[1]);
                }
            }
        } catch (Exception e) {
            LogHelper.error("Can not resolve HxCVersions.txt", References.MOD_NAME);
            if (Configurations.DebugMode) {
                e.printStackTrace();
            }
        }
    }

}
