package HxCKDMS.HxCCore.lib;

public class References {
    public static final String MOD_ID = "HxCCore";
    public static final String MOD_NAME = "HxCKDMS Core";
    public static final String VERSION = "1.6.0";
    public static final String DEPENDENCIES = "required-after:Forge@[11.14.2.1450,)";
    public static final String PACKET_CHANNEL_NAME = MOD_ID.toLowerCase();

    public static final String ERROR_REPORT_ADDRESS = "game.kappador.com";
    public static final int ERROR_REPORT_PORT = 7643;

    public static String[] PERM_NAMES = new String[]{"Default", "Friend", "Helper", "Moderator", "Admin", "Owner"};
    public static char[] PERM_COLOURS = new char[]{'f', 'a', 'e', '1', '6', '4'};

    public static final String[] COMMANDS = new String[]{"help", "heal", "kill", "god", "fly", "feed", "burn", "extinguish", "smite", "setHome",
            "home", "setWarp", "warp", "repair", "repairAll", "modlist", "nick", "playerinfo", "serverinfo", "setperms", "spawn", "tpp"};

    public static final String CC = "\u00a7";
}
