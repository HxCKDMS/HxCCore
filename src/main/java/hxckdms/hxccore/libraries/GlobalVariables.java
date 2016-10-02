package hxckdms.hxccore.libraries;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;

public class GlobalVariables {
    public static File modConfigDir;
    public static volatile HashMap<UUID, String> devTags = new HashMap<>();
}
