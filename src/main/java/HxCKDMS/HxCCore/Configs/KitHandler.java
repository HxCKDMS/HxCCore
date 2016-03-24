package HxCKDMS.HxCCore.Configs;

import HxCKDMS.HxCCore.Handlers.NBTFileIO;
import HxCKDMS.HxCCore.HxCCore;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.potion.PotionEffect;

import java.util.*;

public class KitHandler {
    public static HashMap<String, String> defaultKits = new HashMap<>();
    static {
        defaultKits.putIfAbsent("BobCutter", "4<minecraft:stone_sword> = 1 (name=BOB Cutter~unbreakable~enchantments=21:5:20:1~lore=Bob cutter hurts bob...=Therefore bob is sad.~attributes=generic.attackDamage:Damage:mainhand:0:50.0); <minecraft:stone_pickaxe> = 1; <minecraft:stone_axe> = 1; <minecraft:stone_shovel> = 1; <minecraft:cooked_porkchop> = 8");
        defaultKits.putIfAbsent("Starter", "1<minecraft:stone_sword> = 1 (name=Starter Sword~unbreakable~enchantments=21:1:20:1~lore=A Divine Gift from DrZed.~attributes=generic.attackDamage:Damage:mainhand:0:5.0); <minecraft:stone_pickaxe> = 1 (name=Starter Pick~unbreakable~enchantments=32:3:35:1~lore=A Divine Gift from DrZed.); <minecraft:stone_axe> = 1 (name=Starter Axe~unbreakable~enchantments=32:5~lore=A Divine Gift from DrZed.); <minecraft:stone_shovel> = 1 (name=Starter Spade~unbreakable~enchantments=32:3~lore=A Divine Gift from DrZed.); <minecraft:cooked_porkchop> = 8 (name=Almighty Bacon~unbreakable~lore=The Almighty Bacon!); <minecraft:leather_helmet> = 1 (name=Starter Helmet~unbreakable~color=0~lore=A Divine Gift from DrZed)~attributes=generic.maxHealth:HealthBoost:head:0:2.0); <minecraft:leather_chestplate> = 1 (name=Starter Chestplate~unbreakable~color=0~lore=A Divine Gift from DrZed.~attributes=generic.maxHealth:HealthBoost:torso:0:3.0); <minecraft:leather_leggings> = 1 (name=Starter Leggings~unbreakable~color=0~lore=A Divine Gift from DrZed.~attributes=generic.maxHealth:HealthBoost:legs:0:3.0); <minecraft:leather_boots> = 1 (name=Starter Shoes~color=0~unbreakable~lore=A Divine Gift from DrZed.~attributes=generic.maxHealth:HealthBoost:feet:0:2.0)");
        defaultKits.putIfAbsent("Drugs", "4<minecraft:potion> = 1 (name=Speed~lore=A Divine Gift from DrZed.~effects=1:10000:15:2:16000:4:3:10000:15:4:16000:3:9:120:1)");
    }

    public static boolean canGetKit(String Kit, int permLevel) {
        NBTTagCompound Kits = NBTFileIO.getNbtTagCompound(HxCCore.KitsData, "Kits");
        return Kits.getInteger(Kit) <= permLevel;
    }
//Too tired to continue, future me, don't yell at me, I'm sure this is something stupid, but I can't think well. Enjoy cleaning up my mess.
    public static List<ItemStack> getKit(String Kit) {
        NBTTagCompound Kits = NBTFileIO.getNbtTagCompound(HxCCore.KitsData, "Kits");
        NBTTagList sets = Kits.getTagList("Items", 10);
        for (int i = 0; i < sets.tagCount() - 1; i++) {
            if (sets.getCompoundTagAt(i).getString("name").equalsIgnoreCase(Kit)) {
                sets.getCompoundTagAt(i).getCompoundTag("KitItems");
            }
        }
        return null;
    }

    public static List<ItemStack> createKitByString(String Kit) {
        int lvl = Integer.getInteger(String.valueOf(Kit.charAt(0)));
        String[] vals = Kit.substring(1).split("; ");
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
}
