package hxckdms.hxccore.configs;

import hxckdms.hxcconfig.Config;

import java.util.ArrayList;

@Config
public class Configuration {
    @Config.comment("Debug Mode Enable? Can cause lag and console spam!")
    public static boolean debugMode;

    @Config.category("Chat")
    public static String chatMessageLayout = "(GAMEMODE)DEV_TAG[PERMISSION_TAG]<PLAYER_NICK> CHAT_MESSAGE";

    @Config.category("Chat")
    public static ArrayList<Character> bannedColorCharacters = new ArrayList<>();

    @Config.category("Chat")
    public static String broadcastLayout = "[&6SERVER&f] &f<SENDER&f> &4MESSAGE";

    @Config.category("Features")
    @Config.comment("Don't Exceed 100 without Tinkers or a mod that changes Health Bar.")
    public static int maxBonusHealth = 60;

    @Config.category("Features")
    @Config.comment("Max number of any individual entity type")
    public static int maxEntitiesOfOneType = 50;

    @Config.category("Features")
    @Config.comment("Delay between checking for excess lag")
    public static int updateDelay = 10;

    @Config.category("Features")
    @Config.comment("The higher the number the more Max Damage!")
    public static int maxBonusDamage = 1;

    @Config.category("Features")
    @Config.comment("How many levels are required per increment of buffs.")
    public static int XPBuffPerLevels = 5;

    @Config.category("Features")
    @Config.comment("How much health to gain per increment of buffs.")
    public static int healthPerBuff = 1;

    @Config.category("Features")
    @Config.comment("How much damage to gain per increment of buffs.")
    public static int damagePerBuff = 1;

    @Config.category("Features")
    @Config.comment("How much mining speed per increment of buffs.")
    public static float miningSpeedPerBuff = 0.015f;

    @Config.category("Features")
    @Config.comment("How much lifesteal per increment of Buffs")
    public static float lifestealPerBuff = 0.015f;

    @Config.category("Features")
    @Config.comment("Change this to false to disable automatic crash reporter when HxCKDMS Core is Possibly involved.")
    public static boolean autoCrashReporterEnabled = true;

    @Config.category("Features")
    @Config.comment("Makes help report back in color.")
    public static boolean doColorizedHelp = true;

    @Config.category("Features")
    @Config.comment("Clear|Cull|Off Clear kills all if over #, Cull kills random until below limit, Off doesn't clear")
    public static String clearExcessEntities = "cull";


    public static int coloredChatMinimumPermissionLevel = 0;

    @Config.category("Features")
    @Config.comment("Max uses of /hxc RTP")
    public static int maxRTPUses = 5;

    @Config.category("Features")
    @Config.comment("Generates a random number for x/y during /hxc RTP (automatically negative if going negative direction)")
    public static int maxRTPRandom = 500;

    @Config.category("Features")
    @Config.comment("Added to the random number for x/y (automatically negative if going negative direction)")
    public static int minRTPRandom = 1000;
    public static char defaultChatColour = 'f';
    public static char defaultNameColour = 'f';
    public static char defaultOpNameColour = '4';
    public static char serverChatColour = '4';
    public static boolean useTextStorageofHomes = false;
    public static boolean storeTextHomesUsingName = false;
    @Config.category("Features")
    @Config.comment("Untested, cancels all player interact events within protected zones.")
    public static boolean cancelAllEventsInProtection = false;

    public static boolean allowMobsPowerTool, herobrineMessages, enableCapes = true, showPingOutsideF3Menu;
}
