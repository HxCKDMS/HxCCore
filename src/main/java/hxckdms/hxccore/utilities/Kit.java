package hxckdms.hxccore.utilities;

import hxckdms.hxcconfig.HxCConfig;
import hxckdms.hxcconfig.handlers.SpecialHandlers;
import hxckdms.hxccore.configs.KitConfiguration;
import hxckdms.hxccore.configs.NBTHandler;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

import java.sql.Time;
import java.util.*;

import static hxckdms.hxccore.libraries.Constants.MOD_NAME;
import static hxckdms.hxccore.libraries.GlobalVariables.kitConfig;
import static hxckdms.hxccore.libraries.GlobalVariables.modConfigDir;

@SuppressWarnings("WeakerAccess")
public class Kit {
    public int permissionLevel;
    public HashMap<String, DummyItem> items;
    public int delayBetweenUses;
    public int maxUses;

    public Kit() {
    }

    public Kit(int permissionLevel, HashMap<String, DummyItem> items) {
        this.permissionLevel = permissionLevel;
        this.items = items;
    }

    public Kit(int permissionLevel, HashMap<String, DummyItem> items, int TimeDelay, int MaxUses) {
        this.permissionLevel = permissionLevel;
        this.items = items;
        this.delayBetweenUses = TimeDelay;
        this.maxUses = MaxUses;
    }

    public static void initConfigs() {
        SpecialHandlers.registerSpecialClass(Kit.class);
        SpecialHandlers.registerSpecialClass(DummyItem.class);
        SpecialHandlers.registerSpecialClass(DummyPotionEffect.class);
        SpecialHandlers.registerSpecialClass(DummyAttribute.class);
        kitConfig = new HxCConfig(KitConfiguration.class, "Kits", modConfigDir, "cfg", MOD_NAME);
        kitConfig.registerHandler(new NBTHandler());
        kitConfig.initConfiguration();
    }

    public List<ItemStack> getKitItems() {
        ArrayList<ItemStack> itemStacks = new ArrayList<>();

        for (Map.Entry<String, DummyItem> entry : items.entrySet()) {
            ItemStack stack = new ItemStack((Item) Item.itemRegistry.getObject(entry.getKey()), entry.getValue().amount, entry.getValue().metadata);
            NBTTagCompound tagCompound = (NBTTagCompound) entry.getValue().nbtData.copy();

            boolean hasAdded = false;
            NBTTagList enchantTagList = tagCompound.getTagList("ench", 10);
            for (Map.Entry<Integer, Integer> enchantEntry : entry.getValue().enchantments.entrySet()) {
                NBTTagCompound EnchantTagCompound = new NBTTagCompound();
                EnchantTagCompound.setShort("id",  enchantEntry.getKey().shortValue());
                EnchantTagCompound.setShort("lvl", enchantEntry.getValue().shortValue());
                enchantTagList.appendTag(EnchantTagCompound);
                hasAdded = true;
            }


            if (hasAdded) tagCompound.setTag("ench", enchantTagList);
            hasAdded = false;

            NBTTagList customPotionList = tagCompound.getTagList("CustomPotionEffects", 10);

            for (Map.Entry<Integer, DummyPotionEffect> customPotionEffectEntry : entry.getValue().customPotionEffects.entrySet()) {
                NBTTagCompound customPotionTag = new NBTTagCompound();
                customPotionTag.setByte("Id", customPotionEffectEntry.getKey().byteValue());
                customPotionTag.setByte("Amplifier", customPotionEffectEntry.getValue().amplifier);
                customPotionTag.setInteger("Duration", customPotionEffectEntry.getValue().duration);
                customPotionTag.setBoolean("Ambient", customPotionEffectEntry.getValue().ambient);
                customPotionTag.setBoolean("ShowParticles", customPotionEffectEntry.getValue().showParticles);
                customPotionList.appendTag(customPotionTag);
                hasAdded = true;
            }
            if (hasAdded) tagCompound.setTag("CustomPotionEffects", customPotionList);
            hasAdded = false;


            NBTTagList attributes = tagCompound.getTagList("AttributeModifiers", 10);
            for (DummyAttribute dummyAttribute : entry.getValue().attributes) {
                NBTTagCompound attribute = new NBTTagCompound();

                attribute.setString("AttributeName", dummyAttribute.attributeName);
                attribute.setString("Name", dummyAttribute.name);
                attribute.setString("Slot", dummyAttribute.slot);
                attribute.setInteger("Operation", dummyAttribute.operation);
                attribute.setDouble("Amount", dummyAttribute.amount);
                UUID uuid = UUID.randomUUID();
                attribute.setLong("UUIDMost", uuid.getMostSignificantBits());
                attribute.setLong("UUIDLeast", uuid.getLeastSignificantBits());
                attributes.appendTag(attribute);
                hasAdded = true;
            }
            if (hasAdded) tagCompound.setTag("AttributeModifiers", attributes);

            NBTTagCompound displayCompound = tagCompound.getCompoundTag("display");
            NBTTagList toolTips = new NBTTagList();
            if (entry.getValue().color != -1) displayCompound.setInteger("color", entry.getValue().color);
            if (!entry.getValue().itemName.isEmpty()) displayCompound.setString("Name", entry.getValue().itemName);
            entry.getValue().lore.stream().map(NBTTagString::new).forEachOrdered(toolTips::appendTag);
            displayCompound.setTag("Lore", toolTips);
            tagCompound.setTag("display", displayCompound);
            stack.setTagCompound(tagCompound);
            itemStacks.add(stack);
        }
        return itemStacks;
    }

    public static class DummyItem {
        public int amount;
        public int metadata;
        public String itemName;
        public int color;
        public HashMap<Integer, Integer> enchantments;
        public HashMap<Integer, DummyPotionEffect> customPotionEffects;
        public ArrayList<DummyAttribute> attributes;
        public LinkedList<String> lore;
        public NBTTagCompound nbtData;

        public DummyItem() {
        }

        public DummyItem(int amount, int metadata, String itemName, int color, HashMap<Integer, Integer> enchantments, HashMap<Integer, DummyPotionEffect> customPotionEffects, ArrayList<DummyAttribute> attributes, LinkedList<String> lore, NBTTagCompound nbtData) {
            this.amount = amount;
            this.metadata = metadata;
            this.itemName = itemName;
            this.color = color;
            this.enchantments = enchantments;
            this.customPotionEffects = customPotionEffects;
            this.attributes = attributes;
            this.lore = lore;
            this.nbtData = nbtData;
        }
    }

    public static class DummyAttribute {
        public String attributeName;
        public String name;
        public String slot;
        public int operation;
        public double amount;

        public DummyAttribute() {
        }

        public DummyAttribute(String attributeName, String name, String slot, int operation, double amount) {
            this.attributeName = attributeName;
            this.name = name;
            this.slot = slot;
            this.operation = operation;
            this.amount = amount;
        }
    }

    public static class DummyPotionEffect {
        public byte amplifier;
        public int duration;
        public boolean ambient;
        public boolean showParticles;

        public DummyPotionEffect() {
        }

        public DummyPotionEffect(byte amplifier, int duration, boolean ambient, boolean showParticles) {
            this.amplifier = amplifier;
            this.duration = duration;
            this.ambient = ambient;
            this.showParticles = showParticles;
        }
    }
}
