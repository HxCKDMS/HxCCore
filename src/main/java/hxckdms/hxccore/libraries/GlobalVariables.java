package hxckdms.hxccore.libraries;

import hxckdms.hxccore.utilities.NBTFileHandler;
import net.minecraft.server.MinecraftServer;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;

public class GlobalVariables {
    public static File modConfigDir, modWorldDir, customWorldDataFile, permissionDataFile;
    public static NBTFileHandler customWorldData, permissionData;
    public static volatile HashMap<UUID, String> devTags = new HashMap<>();
    public static MinecraftServer server;
}
