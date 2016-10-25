package hxckdms.hxccore.configs;

import hxckdms.hxcconfig.Config;

import java.util.ArrayList;

@Config
public class Configuration {
    @Config.category("chat")
    public static String chatMessageLayout = "(GAMEMODE)DEV_TAG[PERMISSION_TAG]<PLAYER_NICK> CHAT_MESSAGE";
    @Config.category("chat")
    public static ArrayList<Character> bannedColorCharacters = new ArrayList<>();
    @Config.category("chat")
    public static String broadcastLayout = "[&6SERVER&f] &f<SENDER&f> &4MESSAGE";
}
