package HxCKDMS.HxCCore.Configs;

import HxCKDMS.HxCCore.HxCCore;
import HxCKDMS.HxCCore.api.Configuration.Category;
import HxCKDMS.HxCCore.api.Configuration.Config;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

@SuppressWarnings("unused")
public class Configurations {
    @Config.Boolean(description = "Debug Mode Enable? Can cause lag and console spam!")
    public static boolean DebugMode;

    @Config.Boolean(description = "Enable all HxCCommands. (Disable if you don't want any new commands)", category = "Features")
    public static boolean enableCommands = true;
    @Config.Integer(description = "Don't Exceed 100 without Tinkers or a mod that changes Health Bar.", category = "Features")
    public static int MaxHealth = 20;
    @Config.Integer(description = "The higher the number the more Max Damage!", category = "Features")
    public static int MaxDamage = 11;
    @Config.Integer(description = "Sets the amount of ticks it takes for a tpa request to time out.", category = "Features")
    public static Integer TpaTimeout = 600;
    @Config.Integer(description = "How many levels are required per increment of buffs.", category = "Features")
    public static Integer XPBuffPerLevels = 5;

    @Config.List
    public static List<Character> bannedColorCharacters = Arrays.asList('k', 'm', '4');

    @Config.Boolean(description = "Change this to false to disable automatic crash reporter when HxCKDMS Core is Possibly involved.", category = "Features")
    public static boolean autoCrashReporterEnabled = true;

    @Config.Map(category = "Permissions", description = "You can rename these... and the second part is colour. the third is number of homes (-1 = infinite) AND you can add more..")
    public static LinkedHashMap<String, String> Permissions = new LinkedHashMap<>();
    @Config.Map(description = "HxC is labels given to special people. Group is the Server rank Name is nickname", forceReset = true)
    public static LinkedHashMap<String, String> formats = new LinkedHashMap<>();

    @Config.Boolean
    public static boolean EnableGroupTagInChat = true, EnableHxCTagInChat = true, EnableColourInChat = true;

    @Config.String(description = "This is the file name of the last crash reported so the same crash-report doesn't get reported multiple times.", category = "DNT", forceReset = true)
    public static String lastCheckedCrash = "";

    static {
        formats.put("ChatFormat", "HEADER MESSAGE");
        formats.put("PlayerNametagFormat", "HXC GROUP NAME");
        formats.put("GroupFormat", "&r[%1$s&r]");
        formats.put("HxCFormat", "&r[%1$s&r]");
        formats.put("BroadcastFormat", "&f[&6SERVER&f] &f<SENDER&f> &4MESSAGE");
    }

    public static void updateCFGS() {
        if (Permissions.isEmpty()) {
            Permissions.put("Default", "f 3");
            Permissions.put("Helper", "e 5");
            Permissions.put("Moderator", "9 10");
            Permissions.put("Admin", "6 16");
            Permissions.put("Owner", "4 -1");
        }
        if (CommandsConfig.BannedCommands.isEmpty()) {
            CommandsConfig.BannedCommands.put("example command", 0);
        }

        if (Kits.Kits.isEmpty() && Kits.KitPerms.isEmpty()) {
            //http://puu.sh/kLnuM.jpg
            //These are mainly for testing and showing all the features that can be done...
//        Kits.put("Starter", "{<minecraft:stone_sword> = 1 (name=BOB Cutter~unbreakable~enchantments=21:5:20:1~lore=Bob cutter hurts bob...=Therefore bob is sad.~attributes=generic.attackDamage:Damage:mainhand:0:50.0); <minecraft:stone_pickaxe> = 1; <minecraft:stone_axe> = 1; <minecraft:stone_shovel> = 1; <minecraft:cooked_porkchop> = 8}");
            Kits.Kits.put("Starter", "{<minecraft:stone_sword> = 1 (name=Starter Sword~unbreakable~enchantments=21:1:20:1~lore=A Divine Gift from DrZed.~attributes=generic.attackDamage:Damage:mainhand:0:5.0); <minecraft:stone_pickaxe> = 1 (name=Starter Pick~unbreakable~enchantments=32:3:35:1~lore=A Divine Gift from DrZed.); <minecraft:stone_axe> = 1 (name=Starter Axe~unbreakable~enchantments=32:5~lore=A Divine Gift from DrZed.); <minecraft:stone_shovel> = 1 (name=Starter Spade~unbreakable~enchantments=32:3~lore=A Divine Gift from DrZed.); <minecraft:cooked_porkchop> = 8 (name=Almighty Bacon~unbreakable~lore=The Almighty Bacon!); <minecraft:leather_helmet> = 1 (name=Starter Helmet~unbreakable~color=0~lore=A Divine Gift from DrZed)~attributes=generic.maxHealth:HealthBoost:head:0:2.0); <minecraft:leather_chestplate> = 1 (name=Starter Chestplate~unbreakable~color=0~lore=A Divine Gift from DrZed.~attributes=generic.maxHealth:HealthBoost:torso:0:3.0); <minecraft:leather_leggings> = 1 (name=Starter Leggings~unbreakable~color=0~lore=A Divine Gift from DrZed.~attributes=generic.maxHealth:HealthBoost:legs:0:3.0); <minecraft:leather_boots> = 1 (name=Starter Shoes~color=0~unbreakable~lore=A Divine Gift from DrZed.~attributes=generic.maxHealth:HealthBoost:feet:0:2.0)}");
            Kits.Kits.put("Drugs", "{<minecraft:potion> = 1 (name=Speed~lore=A Divine Gift from DrZed.~effects=1:10000:15:2:16000:4:3:10000:15:4:16000:3:9:120:1)}");

            Kits.KitPerms.put("Starter", 1);
            Kits.KitPerms.put("Drugs", 4);
        }
    }

    public static void preInitConfigs() {
        HxCCore.hxCConfig.registerCategory(new Category("General"));
        HxCCore.hxCConfig.registerCategory(new Category("Features"));
        HxCCore.hxCConfig.registerCategory(new Category("Permissions", "Do not add a permission level requirement for a command if the permission level doesn't exist!"));
        HxCCore.hxCConfig.registerCategory(new Category("DNT", "DO NOT TOUCH!!!!!!!!!"));

        HxCCore.hxCConfig.handleConfig(Configurations.class, HxCCore.HxCConfigFile);

        HxCCore.commandCFG.registerCategory(new Category("General"));
        HxCCore.commandCFG.handleConfig(CommandsConfig.class, HxCCore.commandCFGFile);

        HxCCore.kits.registerCategory(new Category("General"));
        HxCCore.kits.handleConfig(Kits.class, HxCCore.kitsFile);

        updateCFGS();
        HxCCore.hxCConfig.handleConfig(Configurations.class, HxCCore.HxCConfigFile);
        HxCCore.commandCFG.handleConfig(CommandsConfig.class, HxCCore.commandCFGFile);
        HxCCore.kits.handleConfig(Kits.class, HxCCore.kitsFile);
    }
}
