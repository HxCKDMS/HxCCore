package HxCKDMS.HxCCore.Configs;

import HxCKDMS.HxCCore.api.Configuration.Config;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

import java.util.*;

public class Kits {
    @Config.Map
    public static LinkedHashMap<String, String> Kits = new LinkedHashMap<>();

    @Config.Map
    public static LinkedHashMap<String, Integer> KitPerms = new LinkedHashMap<>();

    static {
        Kits.put("Starter", "{<minecraft:stone_sword> = 1 (name=BOB Cutter~unbreakable~enchantments=21:5:20:1~lore=Bob cutter hurts bob...=Therefore bob is sad.); <minecraft:stone_pickaxe> = 1; <minecraft:stone_axe> = 1; <minecraft:stone_shovel> = 1; <minecraft:coocked_porkchop> = 8}");

        KitPerms.put("Starter", 1);
    }

    public static boolean canGetKit(int permLevel, String kit) {
        return KitPerms.get(kit) <= permLevel;
    }

    public static List<ItemStack> getItems(String Kit) {
        String[] vals = Kits.get(Kit).substring(1, Kits.get(Kit).length() - 1).split("; ");
        List<ItemStack> items = new ArrayList<>();
        for (String tmp : vals) {
            NBTTagCompound tags = new NBTTagCompound(), tmpo = new NBTTagCompound();
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
                        case ("lore"):
                            lore = Arrays.asList(args);
                            break;
                        case ("enchantments"):
                            List<String> enchs = Arrays.asList(args[1].split(":"));
                            int id = 0;
                            for (String str : enchs) {
                                if (id != 0) {
                                    tmp3.addEnchantment(Enchantment.enchantmentsList[id], Integer.parseInt(str));
                                    id = 0;
                                } else id = Integer.parseInt(str);
                            }
                            break;
                    }
                }
                NBTTagList enchs = tmp3.getTagCompound().getTagList("ench", 10);
                NBTTagCompound display = tmp3.getTagCompound().getCompoundTag("display");
                if (lore != null) {
                    NBTTagList lore2 = display.getTagList("Lore", 8);
                    lore.forEach(z -> {
                        if (!z.isEmpty() && !z.equalsIgnoreCase("lore"))
                            lore2.appendTag(new NBTTagString(z));
                    });
                    display.setTag("Lore", lore2);
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
