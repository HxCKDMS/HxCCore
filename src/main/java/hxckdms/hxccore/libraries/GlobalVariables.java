package hxckdms.hxccore.libraries;

import hxckdms.hxcconfig.HxCConfig;
import hxckdms.hxccore.utilities.NBTFileHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

public class GlobalVariables {
    public static File modConfigDir, modWorldDir, customWorldDataFile, permissionDataFile;
    public static NBTFileHandler customWorldData, permissionData;
    public static volatile HashMap<UUID, String> devTags = new HashMap<>();
    public static SimpleNetworkWrapper network;
    public static Map<String, String> langFile;
    public static final HashSet<UUID> doesPlayerHaveMod = new HashSet<>();
    public static HxCConfig mainConfig, commandConfig, kitConfig, alternateHomesConfig;
    public static HashMap<String, byte[]> playerCapes = new HashMap<>();
}
