package hxckdms.hxccore.configs;

import hxckdms.hxcconfig.Config;
import hxckdms.hxccore.utilities.Kit;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

@Config
public class KitConfiguration {
    public static HashMap<String, Kit> kits = new HashMap<String, Kit>() {{
        put("Test", new Kit(2, new HashMap<String, Kit.DummyItem>() {{
            put("minecraft:stone", new Kit.DummyItem(32, 0, "apple", -1, new HashMap<String, Integer>() {{put("sharpness", 4);}}, new LinkedHashMap<>(), new ArrayList<>(), new LinkedList<>(), new NBTTagCompound()));
        }}));
    }};
}
