package HxCKDMS.HxCCore;

import HxCKDMS.HxCCore.Configs.CommandsConfig;
import HxCKDMS.HxCCore.Configs.Configurations;
import HxCKDMS.HxCCore.Contributors.CodersCheck;
import HxCKDMS.HxCCore.Crash.CrashHandler;
import HxCKDMS.HxCCore.Crash.CrashReportThread;
import HxCKDMS.HxCCore.Events.*;
import HxCKDMS.HxCCore.Proxy.IProxy;
import HxCKDMS.HxCCore.Registry.CommandRegistry;
import HxCKDMS.HxCCore.api.Command.HxCCommand;
import HxCKDMS.HxCCore.api.Configuration.HxCConfig;
import HxCKDMS.HxCCore.api.Handlers.CommandsHandler;
import HxCKDMS.HxCCore.api.Handlers.HxCReflectionHandler;
import HxCKDMS.HxCCore.api.Utils.LogHelper;
import HxCKDMS.HxCCore.api.Utils.ServerTranslationUtil;
import HxCKDMS.HxCCore.lib.References;
import HxCKDMS.HxCCore.network.MessageColor;
import HxCKDMS.HxCCore.network.PacketClientSync;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.network.NetworkCheckHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.potion.Potion;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.GameRules;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.common.MinecraftForge;

import java.io.*;
import java.net.URL;
import java.util.*;

import static HxCKDMS.HxCCore.lib.References.*;

@SuppressWarnings({"unused", "ResultOfMethodCallIgnored", "WeakerAccess"})
@Mod(modid = MOD_ID, name = MOD_NAME, version = VERSION, dependencies = DEPENDENCIES, acceptableRemoteVersions = "*")
public class HxCCore {
    @Instance(MOD_ID)
    public static HxCCore instance;
    private long initTime = 0;
    public static ServerTranslationUtil util = new ServerTranslationUtil();

    private static LinkedHashMap<String, String> vers = new LinkedHashMap<>();
    public static final List<String> knownMods = Arrays.asList("HxCCore", "HxCSkills", "HxCEnchants", "HxCWorldGen", "HxCLinkPads", "HxCBlocks",
            "HxCFactions", "HxCTiC", "HxCArcanea", "HxCBows", "HxCDiseases", "HxCArmory");
    public static MinecraftServer server;
    public static HashMap<EntityPlayerMP, EntityPlayerMP> tpaRequestList = new HashMap<>();
    public static HashMap<EntityPlayerMP, Integer> TpaTimeoutList = new HashMap<>();
    public static SimpleNetworkWrapper network;
    public static HxCConfig config, commandConfig, kitConfig;

    @SidedProxy(serverSide = SERVER_PROXY_CLASS, clientSide = CLIENT_PROXY_CLASS)
    public static IProxy proxy;

    public static File HxCCoreDir, HxCConfigDir, HxCConfigFile, commandCFGFile, kitsFile,
            HxCLogDir, CustomWorldData, PermissionsData, KitsData;
    public static volatile File mcRootDir = new File("");

    public static volatile LinkedHashMap<UUID, String> HxCLabels = new LinkedHashMap<>();

    private static PrintWriter commandLog;

    private static final Thread crashReportThread = new Thread(new CrashReportThread()),
            CodersCheckThread = new Thread(new CodersCheck());

    public HashMap<String, String> HxCRules = new HashMap<>();

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        long t = System.nanoTime();
        FMLCommonHandler.instance().registerCrashCallable(new CrashHandler());
        crashReportThread.setName("HxCKDMS Crash check thread");
        Runtime.getRuntime().addShutdownHook(crashReportThread);
        if (!event.getSourceFile().getName().equals("HxCCore-" + MinecraftForge.MC_VERSION + "-" + References.VERSION + "-universal.jar") && !((boolean)Launch.blackboard.get("fml.deobfuscatedEnvironment")))
            throw new RuntimeException("You have renamed HxCCore or downloaded it from an illegal place, Be sure you got it from http://mods.curse.com/mc-mods/minecraft/227594-hxc-core");
        HxCConfigDir = new File(event.getModConfigurationDirectory(), "HxCKDMS");
        if (!HxCConfigDir.exists()) HxCConfigDir.mkdirs();
        HxCConfigFile = new File(HxCConfigDir, "HxCCore.cfg");
        commandCFGFile = new File(HxCConfigDir, "HxCCommands.cfg");
//        kitsFile = new File(HxCConfigDir, "HxC-Kits.cfg");

        config = new HxCConfig(Configurations.class, "HxCCore", HxCConfigDir, "cfg", MOD_NAME);
        //kitConfig = new HxCConfig(Kits.class, "HxC-Kits", HxCConfigDir, "cfg");
        commandConfig = new HxCConfig(CommandsConfig.class, "HxCCommands", HxCConfigDir, "cfg", MOD_NAME);

        Configurations.preInitConfigs();
        updateConfigs();

        network = NetworkRegistry.INSTANCE.newSimpleChannel(PACKET_CHANNEL_NAME);
        network.registerMessage(MessageColor.Handler.class, MessageColor.class, 0, Side.CLIENT);
        if (Configurations.DebugMode)
            network.registerMessage(PacketClientSync.Handler.class, PacketClientSync.class, 1, Side.CLIENT);

        CodersCheckThread.setName("HxCKDMS Contributors check thread");
        CodersCheckThread.start();
        extendEnchantsArray();

        if (Configurations.enableCommands)
            CommandRegistry.registerCommands(new CommandsHandler(), event.getAsmData().getAll(HxCCommand.class.getCanonicalName()));

        if (!Loader.isModLoaded("BiomesOPlenty")) extendPotionsArray();

        HxCRules.putIfAbsent("XPBuffs", "false");
        HxCRules.putIfAbsent("LogCommands", "false");
        HxCRules.putIfAbsent("ReportCommands", "false");
        HxCRules.putIfAbsent("AFKDebuffs", "true");
        HxCRules.putIfAbsent("XPCooldownInterrupt", "true");

        proxy.preInit(event);

        LogHelper.info("If you see any debug messages, feel free to bug one of the authors about it ^_^", MOD_NAME);
        initTime += System.nanoTime() - t;
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        long t = System.nanoTime();
        //yay 1.8.x+ all is Forge EventBus :D
        FMLCommonHandler.instance().bus().register(new EventNickSync());
        FMLCommonHandler.instance().bus().register(new EventTpRequest());
        FMLCommonHandler.instance().bus().register(new EventJoinWorld());
        FMLCommonHandler.instance().bus().register(new EventPlayerNetworkCheck());

        MinecraftForge.EVENT_BUS.register(new EventGod());
        MinecraftForge.EVENT_BUS.register(new EventProtection());
        MinecraftForge.EVENT_BUS.register(new EventFly());
        MinecraftForge.EVENT_BUS.register(new EventChat());
        MinecraftForge.EVENT_BUS.register(new EventPowerTool());
        MinecraftForge.EVENT_BUS.register(new EventPlayerDeath());
        MinecraftForge.EVENT_BUS.register(new EventXPCooldownCanceller());
        MinecraftForge.EVENT_BUS.register(new EventXPBuffs());
        proxy.init(event);
        if (Configurations.DebugMode)
            MinecraftForge.EVENT_BUS.register(new EventPlayerHxCDataSync());
        if (Configurations.enableCommands && CommandsConfig.EnabledCommands.containsKey("Path") && CommandsConfig.EnabledCommands.get("Path"))
            MinecraftForge.EVENT_BUS.register(new EventBuildPath());
        initTime += System.nanoTime() - t;
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        long t = System.nanoTime();
        if(Configurations.versionCheck)
            versionCheck();
        knownMods.forEach(mod -> {
            if (Loader.isModLoaded(mod))
                LogHelper.info("Thank you for using " + mod, MOD_NAME);
        });
        proxy.postInit(event);
        initTime += System.nanoTime() - t; //718758345
        LogHelper.info("HxCCore initialized all parts in a total of " + initTime + " nano seconds. Or " + initTime/100000000 + " seconds", MOD_NAME);
    }

    private GameRules rules;

    @EventHandler
    public void serverStart(FMLServerStartingEvent event) {
        server = event.getServer();

        rules = server.worldServerForDimension(0).getGameRules();
        HxCRules.forEach(this::registerGamerule);
        if(Configurations.versionCheck)
            Loader.instance().getModList().stream().filter(m -> knownMods.contains(m.getModId())).forEach(m -> {
                String s = getNewVer(m.getModId(), m.getVersion());
                if (!s.isEmpty())
                    server.logWarning("A new version of " + m.getModId() + " has been released. Please update! New Version:" + s);
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

        CustomWorldData = new File(HxCCoreDir, "HxCWorld.dat");
        PermissionsData = new File(HxCCoreDir, "HxC-Permissions.dat");
        KitsData = new File(HxCCoreDir, "HxC-Kits.dat");
        File OLDLOG = new File(HxCLogDir, "HxC-Command.log");

        try {
            if (!CustomWorldData.exists())
                CustomWorldData.createNewFile();
            if (!PermissionsData.exists())
                PermissionsData.createNewFile();
            if (!KitsData.exists())
                KitsData.createNewFile();
            if (!OLDLOG.exists())
                OLDLOG.createNewFile();
            commandLog = new PrintWriter(new File(HxCLogDir, "HxC-Command.log"), "UTF-8");
//            EventProtection.load();
        } catch (IOException ignored) {}
    }

    public void registerGamerule(String rule, String value) {
        if (!rules.hasRule(rule))
            rules.addGameRule(rule, value);
        else
            HxCRules.replace(rule, rules.getGameRuleStringValue(rule));
    }

    public static void updateGamerules() {
        instance.HxCRules.forEach((rule,value) -> instance.HxCRules.replace(rule, instance.rules.getGameRuleStringValue(rule)));

//        if (instance.HxCRules.get("XPCooldownInterrupt").equals("true"))
    }

    private boolean loggedCommand;

    public void logCommand(String str) {
        try {
            if (commandLog != null) {
                commandLog.println(str);
            } else {
                File OLDLOG = new File(HxCLogDir, "HxC-Command.log");
                if (!OLDLOG.exists())
                    OLDLOG.createNewFile();
                commandLog = new PrintWriter(new File(HxCLogDir, "HxC-Command.log"), "UTF-8");
                LogHelper.error("HxCCommand Log doesn't exist.", MOD_NAME);
            }
            loggedCommand = true;
        } catch (Exception ignored) {}
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

    @NetworkCheckHandler
    public boolean checkNetwork(Map<String, String> mods, Side side) {
        return !mods.containsKey("HxCCore") || mods.get("HxCCore").equals(VERSION);
    }

    public static String getNewVer(String mod, String Version) {
        try {
            int a = Integer.parseInt(Version.replaceAll("\\.", "")),
                    b = Integer.parseInt(vers.get(mod + ":1.7.10").replaceAll("\\.", ""));
            if (b > a)
                return vers.get(mod + ":1.7.10");
        } catch(Exception ignored) {}
        return "";
    }

    public static void updateConfigs() {
        PERM_NAMES = new String[Configurations.Permissions.size()];
        PERM_COLOURS = new char[Configurations.Permissions.size()];
        HOMES = new int[Configurations.Permissions.size()];
        for (int i = 0; i < Configurations.Permissions.size(); i++) {
            PERM_NAMES[i] = (String) Configurations.Permissions.keySet().toArray()[i];
            String[] temp = Configurations.Permissions.get(PERM_NAMES[i]).replaceAll(" ", "").split(",");
            if (temp.length == 3) {
                PERM_COLOURS[i] = temp[0].charAt(0);
                HOMES[i] = Integer.parseInt(temp[1]);
                PROTECT_SIZE[i] = Long.parseLong(temp[2]);
            } else {
                HOMES[i] = Integer.parseInt(temp[0]);
                PROTECT_SIZE[i] = Long.parseLong(temp[1]);
            }
        }
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
            if (Configurations.DebugMode) e.printStackTrace();
        }
    }

    private static void extendEnchantsArray() {
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
}
