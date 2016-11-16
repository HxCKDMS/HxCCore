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
    private static final NBTTagCompound unbreakable = new NBTTagCompound();
    static {
        unbreakable.setBoolean("Unbreakable", true);
    }
    public static HashMap<String, Kit> kits = new HashMap<String, Kit>() {{
        put("Starter", new Kit(1, new LinkedHashMap<String, Kit.DummyItem>() {{
            put("minecraft:stone_sword", new Kit.DummyItem(1, 0, "Sturdy Crude Blade", -1, new HashMap<>(), new LinkedHashMap<>(), new ArrayList<Kit.DummyAttribute>(){{new Kit.DummyAttribute("generic.luck", "Stroke of Luck", "mainhand", 0, 16);new Kit.DummyAttribute("generic.attackDamage", "Keen Edge", "mainhand", 0, 2);new Kit.DummyAttribute("generic.attackSpeed", "Light", "mainhand", 0, 1);}}, new LinkedList<String>(){{add("A beginners weapon.");}}, unbreakable));
            put("minecraft:stone_pickaxe", new Kit.DummyItem(1, 0, "Sturdy Crude Pickaxe", -1, new HashMap<>(), new LinkedHashMap<>(), new ArrayList<Kit.DummyAttribute>(){{new Kit.DummyAttribute("generic.luck", "Stroke of Luck", "mainhand", 0, 16);new Kit.DummyAttribute("generic.attackDamage", "Keen Edge", "mainhand", 0, 1);new Kit.DummyAttribute("generic.attackSpeed", "Light", "mainhand", 0, 4);}}, new LinkedList<String>(){{add("A beginners pickaxe.");}}, unbreakable));
            put("minecraft:stone_axe", new Kit.DummyItem(1, 0, "Sturdy Crude Axe", -1, new HashMap<>(), new LinkedHashMap<>(), new ArrayList<Kit.DummyAttribute>(){{new Kit.DummyAttribute("generic.luck", "Stroke of Luck", "mainhand", 0, 16);new Kit.DummyAttribute("generic.attackDamage", "Keen Edge", "mainhand", 0, 1);new Kit.DummyAttribute("generic.attackSpeed", "Light", "mainhand", 0, 4);}}, new LinkedList<String>(){{add("A beginners axe.");}}, unbreakable));
            put("minecraft:stone_shovel", new Kit.DummyItem(1, 0, "Sturdy Crude Spade", -1, new HashMap<>(), new LinkedHashMap<>(), new ArrayList<Kit.DummyAttribute>(){{new Kit.DummyAttribute("generic.luck", "Stroke of Luck", "mainhand", 0, 16);new Kit.DummyAttribute("generic.attackDamage", "Keen Edge", "mainhand", 0, 1);new Kit.DummyAttribute("generic.attackSpeed", "Light", "mainhand", 0, 4);}}, new LinkedList<String>(){{add("A beginners spade.");}}, unbreakable));
            put("minecraft:cooked_porkchop", new Kit.DummyItem(16, 0, "Meat", -1, new HashMap<>(), new LinkedHashMap<>(), new ArrayList<>(), new LinkedList<String>(){{add("What kind of meat?");}}, new NBTTagCompound()));
            put("minecraft:leather_helmet", new Kit.DummyItem(1, 0, "Basic Helmet", 0x00000000, new HashMap<>(), new LinkedHashMap<>(), new ArrayList<>(), new LinkedList<String>(){{add("Protects your thinking meat.");}}, unbreakable));
            put("minecraft:leather_chestplate", new Kit.DummyItem(1, 0, "Basic Chestplate", 0x00000000, new HashMap<>(), new LinkedHashMap<>(), new ArrayList<>(), new LinkedList<String>(){{add("Protects your pump.");}}, unbreakable));
            put("minecraft:leather_leggings", new Kit.DummyItem(1, 0, "Basic Pants", 0x00000000, new HashMap<>(), new LinkedHashMap<>(), new ArrayList<>(), new LinkedList<String>(){{add("Protects your knees.");}}, unbreakable));
            put("minecraft:leather_boots", new Kit.DummyItem(1, 0, "Basic Boots", 0x00000000, new HashMap<>(), new LinkedHashMap<>(), new ArrayList<>(), new LinkedList<String>(){{add("Protects your feet.");}}, unbreakable));
            put("minecraft:potion", new Kit.DummyItem(1, 0, "Second Chance", -1, new HashMap<>(), new LinkedHashMap<String, Kit.DummyPotionEffect>(){{put("minecraft:regeneration", new Kit.DummyPotionEffect((byte)4, 100, false, false));put("minecraft:instant_health", new Kit.DummyPotionEffect((byte)2, 0, false, false));put("minecraft:resistance", new Kit.DummyPotionEffect((byte)2, 1000, false, false));put("minecraft:fire_resistance", new Kit.DummyPotionEffect((byte)1, 10000, false, false));}}, new ArrayList<>(), new LinkedList<String>(){{add("Gives you a second chance at life.");}}, new NBTTagCompound()));
        }}));
    }};
}
