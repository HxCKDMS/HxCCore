package HxCKDMS.HxCCore.Configs;

import HxCKDMS.HxCCore.HxCCore;
import HxCKDMS.HxCCore.api.Configuration.Config;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.potion.PotionEffect;

import java.util.*;

public class Kits {
    @Config.Map
    public static LinkedHashMap<String, String> Kits = new LinkedHashMap<>();

    @Config.Map
    public static LinkedHashMap<String, Integer> KitPerms = new LinkedHashMap<>();

    public static boolean canGetKit(int permLevel, String kit) {
        return KitPerms.get(kit) <= permLevel;
    }

    public static List<ItemStack> getItems(String Kit) {
        String[] vals = Kits.get(Kit).substring(1, Kits.get(Kit).length() - 1).split("; ");
        List<ItemStack> items = new ArrayList<>();
        int color = -1;
        for (String tmp : vals) {
            NBTTagCompound tags = new NBTTagCompound();
            List<PotionEffect> pots = new ArrayList<>();
            List<NBTTagCompound> attribs = new ArrayList<>();
            int num;
            String specialData = "";
            if (!tmp.contains("(")) {
                num = Integer.parseInt(tmp.substring(tmp.indexOf("=")).replace("=", "").trim());
                tmp = tmp.replace("=", "").replace(String.valueOf(num), "").trim();
            } else {
                num = Integer.parseInt(tmp.substring(tmp.indexOf("="), tmp.indexOf("(") - 1).replace("=", "").trim());
                specialData = tmp.substring(tmp.indexOf("(")+1, tmp.lastIndexOf(")"));
                tmp = tmp.replace(specialData, "").replace("()", "").replace("=", "").replace(String.valueOf(num), "").trim();
            }
            tmp = tmp.substring(1, tmp.length() - 1);
            String[] tmp2 = tmp.split(":");
            ItemStack tmp3 = null;
            if (tmp2.length == 2) {
                tmp3 = new ItemStack(GameRegistry.findItem(tmp2[0], tmp2[1]), num);
            } else if (tmp2.length == 3) {
                tmp3 = new ItemStack(GameRegistry.findItem(tmp2[0], tmp2[1]), num, Integer.parseInt(tmp2[2]));
            }
            if (!specialData.isEmpty() && tmp3 != null) {
                String[] data;
                List<String> lore = null;
                if (specialData.contains("~")) data = specialData.split("~");
                else data = new String[]{specialData};

                for (String v : data) {
                    String[] args = v.split("=");
                    switch (args[0]) {
                        case ("name"):
                            tmp3.setStackDisplayName(args[1]);
                            break;
                        case ("unbreakable"):
                            tags.setBoolean("Unbreakable", true);
                            break;//lol...
                        case ("color"):
                            color = Integer.parseInt(args[1]);
                            break;
                        case ("lore"):
                            lore = Arrays.asList(args);
                            break;
                        case ("effects"):
                            List<String> effects = Arrays.asList(args[1].split(":"));
                            for (int i = 0; i < effects.size(); i+=3)
                                pots.add(new PotionEffect(Integer.parseInt(effects.get(i)), Integer.parseInt(effects.get(i+1)), Integer.parseInt(effects.get(i+2))));
                            break;
                        case ("enchantments"):
                            List<String> enchs = Arrays.asList(args[1].split(":"));
                            int id = -1; boolean b = false;
                            for (String str : enchs) {
                                if (b)  tmp3.addEnchantment(Enchantment.enchantmentsList[id], Integer.parseInt(str));
                                else id = Integer.parseInt(str);
                                b = !b;
                            }
                            break;
                        case ("attributes"):
                            List<String> attrs = Arrays.asList(args[1].split(":"));
                            for (int i = 0; i < attrs.size(); i+=5) {
                                NBTTagCompound attr = new NBTTagCompound();
                                attr.setString("AttributeName", attrs.get(i));
                                attr.setString("Name", attrs.get(i + 1));
                                attr.setString("Slot", attrs.get(i + 2));
                                attr.setInteger("Operation", Integer.valueOf(attrs.get(i + 3)));
                                attr.setDouble("Amount", Double.valueOf(attrs.get(i + 4)));
                                UUID uuid = UUID.randomUUID();
                                attr.setLong("UUIDMost", uuid.getMostSignificantBits());
                                attr.setLong("UUIDLeast", uuid.getLeastSignificantBits());
                                attribs.add(attr);
                            }
                            break;
                    }
                }
                NBTTagList enchs;
                NBTTagCompound display;
                if (tmp3.hasTagCompound()) {
                    if (tmp3.getTagCompound().hasKey("ench")) {
                        enchs = tmp3.getTagCompound().getTagList("ench", 10);
                    } else {
                        enchs = new NBTTagList();
                    }
                    if (tmp3.getTagCompound().hasKey("display")) {
                        display = tmp3.getTagCompound().getCompoundTag("display");
                    } else {
                        display = new NBTTagCompound();
                    }
                } else {
                    tmp3.setTagCompound(new NBTTagCompound());
                    display = new NBTTagCompound();
                    enchs = new NBTTagList();
                }
                if (lore != null) {
                    NBTTagList lore2 = display.getTagList("Lore", 8);
                    lore.forEach(z -> {
                        if (!z.isEmpty() && !z.equalsIgnoreCase("lore"))
                            lore2.appendTag(new NBTTagString(z));
                    });
                    display.setTag("Lore", lore2);
                }
                if (!pots.isEmpty()) {
                    NBTTagList potions = tmp3.getTagCompound().getTagList("CustomPotionEffects", 10);
                    pots.forEach(z -> {
                        NBTTagCompound tg = new NBTTagCompound();
                        tg.setByte("id", (byte) z.getPotionID());
                        tg.setByte("amplifier", (byte) z.getAmplifier());
                        tg.setInteger("duration", z.getDuration());
                        tg.setByte("ambient", (byte) 0);
                        tg.setByte("showParticles", (byte) 0);//yes IK 1.8 only but doesn't hurt to have it
                        potions.appendTag(tg);
                    });
                    tags.setTag("CustomPotionEffects", potions);
                }
                if (!attribs.isEmpty()) {
                    NBTTagList attributes = tmp3.getTagCompound().getTagList("AttributeModifiers", 10);
                    attribs.forEach(attributes::appendTag);
                    tags.setTag("AttributeModifiers", attributes);
                }
                if (color > -1) {
                    display.setInteger("color", color);
                    color = -1;
                }
                tags.setTag("ench", enchs);
                tags.setTag("display", display);
                tmp3.setTagCompound(tags);
            }
            items.add(tmp3);
        }
        return items;
    }

    public static void createKit(List<ItemStack> stacks, String name, int permLevel) {
        String str = setItems(stacks);
        str = str.substring(0, str.length()-1).replace("(~", "(").replaceAll(":0>", ">").replaceAll(";<", "; <");
        Kits.put(name, "{" + str + "}");
        KitPerms.put(name, permLevel);
        HxCCore.kits.handleConfig(Kits.class, HxCCore.kitsFile);
    }

    public static void removeKit(String name) {
        Kits.remove(name);
        KitPerms.remove(name);
        HxCCore.kits.handleConfig(Kits.class, HxCCore.kitsFile);
    }

    public static void chngKitPerms(String name, int lvl) {
        KitPerms.replace(name, lvl);
        HxCCore.kits.handleConfig(Kits.class, HxCCore.kitsFile);
    }

    public static String setItems(List<ItemStack> items) {
        String str = "";
        for (ItemStack z : items) {
            if (z != null)
                str = str + setItem(z) + ";";
        }
        return str;
    }

    public static String setItem(ItemStack item) {
        String str = (String)Item.itemRegistry.getNameForObject(item.getItem());
        str = "<" + str + ":" + item.getMetadata() + "> = " + item.stackSize;
        if (item.hasTagCompound()) {
            NBTTagCompound tags = item.getTagCompound();
            String ench = "", display = "", pots = "", unbreakable = "", attribs = "";
            for (int i = 0; i < item.getEnchantmentTagList().tagCount(); i++) {
                int a = item.getEnchantmentTagList().getCompoundTagAt(i).getInteger("id");
                int b = item.getEnchantmentTagList().getCompoundTagAt(i).getInteger("lvl");
                if (ench.isEmpty())
                    ench = "~enchantments=" + a + ":" + b;
                else ench = ench + ":"+ a + ":" + b;
            }
            if (tags.hasKey("unbreakable")) {
                unbreakable = "~unbreakable";
            }
            if (tags.hasKey("CustomPotionEffects")) {
                NBTTagList ps = tags.getTagList("CustomPotionEffects", 10);
                pots = pots + "~effects=";
                for (int i = 0; i < ps.tagCount(); i++) {
                    pots = pots + ps.getCompoundTagAt(i).getByte("id");
                    pots = ":" + pots + ps.getCompoundTagAt(i).getInteger("duration") + ":";
                    pots = pots + ps.getCompoundTagAt(i).getByte("amplifier");
                    if (i != ps.tagCount()-1)
                        pots = pots + ":";
                }
            }
            if (tags.hasKey("AttributeModifiers")) {
                NBTTagList ps = tags.getTagList("AttributeModifiers", 10);
                attribs = attribs + "~attributes=";
                for (int i = 0; i < ps.tagCount(); i++) {
                    attribs = attribs + ps.getCompoundTagAt(i).getString("attributename");
                    attribs = ":" + attribs + ps.getCompoundTagAt(i).getString("name");
                    attribs = ":" + attribs + ps.getCompoundTagAt(i).getString("slot");
                    attribs = ":" + attribs + ps.getCompoundTagAt(i).getInteger("operation");
                    attribs = ":" + attribs + ps.getCompoundTagAt(i).getDouble("amount");
                    if (i != ps.tagCount()-1)
                        attribs = attribs + ":";
                }
            }
            if (tags.hasKey("display")) {
                NBTTagCompound dis = tags.getCompoundTag("display");
                if (dis.hasKey("name")) {
                    display = display + "~name=" + dis.getString("name");
                } if (dis.hasKey("color")) {
                    display = display + "~color=" + dis.getInteger("color");
                } if (dis.hasKey("lore")) {
                    NBTTagList lore = dis.getTagList("lore", 8);
                    display = display + "~lore";
                    for (int i = 0; i < lore.tagCount(); i++)
                        display = "=" + display + lore.getStringTagAt(i);
                }
            }
            str = str + " (" + ench + display + unbreakable + attribs + pots + ")";
        }
        return str;
    }
}