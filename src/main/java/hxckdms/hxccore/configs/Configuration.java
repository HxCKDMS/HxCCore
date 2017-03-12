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
    public static int maxBonusHealth = 10;

    @Config.category("Features")
    @Config.comment("The higher the number the more Max Damage!")
    public static int maxBonusDamage = 1;

    @Config.category("Features")
    @Config.comment("How many levels are required per increment of buffs.")
    public static int XPBuffPerLevels = 5;

    @Config.category("Features")
    @Config.comment("Change this to false to disable automatic crash reporter when HxCKDMS Core is Possibly involved.")
    public static boolean autoCrashReporterEnabled = true;

    public static int coloredChatMinimumPermissionLevel = 0;
    public static char defaultChatColour = 'f';
    public static char defaultNameColour = 'f';
    public static char defaultOpNameColour = '4';

    public static boolean allowMobsPowerTool, herobrineMessages, enableCapes = true, showPingOutsideF3Menu;
}
