package HxCKDMS.HxCCore.lib;

import java.util.Arrays;
import java.util.List;

public class References {
    public static final String MOD_ID = "HxCCore";
    public static final String MOD_NAME = "HxCKDMS Core";
    public static final String VERSION = "1.8.0";
    public static final String DEPENDENCIES = "required-after:Forge@[10.13.4.1448,)";
    public static final String PACKET_CHANNEL_NAME = MOD_ID.toLowerCase();

    public static final String ERROR_REPORT_ADDRESS = "69.121.101.201";
    public static final int ERROR_REPORT_PORT = 7643;

    public static String[] PERM_NAMES = new String[]{"Default", "Friend", "Helper", "Moderator", "Admin", "Owner"};
    public static char[] PERM_COLOURS = new char[]{'f', 'a', 'e', '1', '6', '4'};
    public static final List<String> COLOR_CHARS_STRING = Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f", "k", "m", "n", "l", "o");
    public static final List<Character> COLOR_CHARS = Arrays.asList('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'k', 'm', 'n', 'l', 'o');
    public static final String[] COMMANDS = new String[]{"help", "heal", "kill", "god", "fly", "feed", "burn", "extinguish", "smite", "setHome",
            "home", "setWarp", "warp", "repair", "repairAll", "modlist", "nick", "playerinfo", "serverinfo", "setperms", "spawn", "tpp"};

    public static final String CC = "\u00a7";
}
