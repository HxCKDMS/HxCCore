package HxCKDMS.HxCCore.lib;

import HxCKDMS.HxCCore.Configs.Configurations;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class References {
    public static final String MOD_ID = "HxCCore";
    public static final String MOD_NAME = "HxCKDMS Core";
    public static final String VERSION = "1.9.7";
    public static final String DEPENDENCIES = "required-after:Forge@[10.13.4.1448,)";
    public static final String PACKET_CHANNEL_NAME = MOD_ID.toLowerCase();

    public static final String ERROR_REPORT_ADDRESS = "67.187.15.252";
    public static final int ERROR_REPORT_PORT = 7643;

    public static final UUID HPBuffUUID = UUID.fromString("edff168f-32d7-438b-8d29-189e9405e032");
    public static final UUID DMBuffUUID = UUID.fromString("17cb8d52-6376-11e4-b116-123b93f75cba");
    public static final UUID DMdeBuffUUID = UUID.fromString("37da5d51-6626-1555-b166-126663f75c3a");
    public static final UUID SpeedUUID = UUID.fromString("fe15f828-62d7-11e4-b116-123b93f75cba");

    public static String[] PERM_NAMES = new String[Configurations.Permissions.size()];
    public static char[] PERM_COLOURS = new char[Configurations.Permissions.size()];
    public static int[] HOMES = new int[Configurations.Permissions.size()];

    public static final List<String> COLOR_CHARS_STRING = Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f", "k", "m", "n", "l", "o");
    public static final List<Character> COLOR_CHARS = Arrays.asList('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'k', 'm', 'n', 'l', 'o');

    public static final String CC = "\u00a7";
}
