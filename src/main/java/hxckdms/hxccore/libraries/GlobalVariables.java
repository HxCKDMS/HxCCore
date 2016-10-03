package hxckdms.hxccore.libraries;

import net.minecraft.server.MinecraftServer;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;

public class GlobalVariables {
    public static File modConfigDir, modWorldDir, customWorldData, permissionData;
    public static volatile HashMap<UUID, String> devTags = new HashMap<>();
    public static MinecraftServer server;
}
