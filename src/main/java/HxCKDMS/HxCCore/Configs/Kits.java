package HxCKDMS.HxCCore.Configs;

import HxCKDMS.HxCCore.api.Configuration.Config;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

public class Kits {
    @Config.Map
    public static LinkedHashMap<String, String> Kits = new LinkedHashMap<>();
    @Config.Map
    public static LinkedHashMap<String, Integer> KitPerms = new LinkedHashMap<>();

    static {
        Kits.put("Starter", "{<minecraft:stone_sword:0> = 1; <minecraft:stone_pickaxe:0> = 1; <minecraft:stone_axe:0> = 1; <minecraft:stone_shovel> = 1; <minecraft:coocked_porkchop:0> = 8}");

        KitPerms.put("Starter", 1);
    }

    public static boolean canGetKit(int permLevel, String kit) {
        return KitPerms.get(kit) <= permLevel;
    }

    public static List<ItemStack> getItems(String Kit) {
        String[] vals = Kits.get(Kit).substring(1, Kits.get(Kit).length()-1).split("; ");
        List<ItemStack> items = new ArrayList<>();
        for (String tmp : vals) {
            int num;
            String specialData = "";
            if (!tmp.contains("(")) {
                num = Integer.parseInt(tmp.substring(tmp.indexOf("=")).replace("=", "").trim());
                tmp = tmp.replace("=", "").replace(String.valueOf(num), "").trim();
            } else {
                num = Integer.parseInt(tmp.substring(tmp.indexOf("="), tmp.indexOf("(")-1).replace("=", "").trim());
                tmp = tmp.replace("=", "").replace(String.valueOf(num), "").trim();
                specialData = tmp.substring(tmp.indexOf("("), tmp.lastIndexOf(")"));
                tmp = tmp.replace(specialData, "").replace("()", "").trim();
            }
            tmp = tmp.substring(1, tmp.length() - 1);
            String[] tmp2 = tmp.split(":");
            ItemStack tmp3 = new ItemStack(GameRegistry.findItem(tmp2[0], tmp2[1]), num);
            tmp3.setMetadata(Integer.parseInt(tmp2[2]));
            if (!specialData.isEmpty()) {
                String[] data = specialData.split("|");
                for (String v : data) {
                    String[] args = v.split("=");
                    switch (args[0]) {
                        case("name") :
                            tmp3.setStackDisplayName(args[1]);
                            break;
                        case("enchantments") :
                            List<String> enchs = Arrays.asList(args[1].split(":"));
                            int id = 0;
                            for (String str : enchs) {
                                if (id != 0) {
                                    tmp3.addEnchantment(Enchantment.enchantmentsList[id], Integer.parseInt(str));
                                    id = 0;
                                } else {
                                    id = Integer.parseInt(str);
                                }
                                enchs.remove(str);
                            }
                            break;
                    }
                }
            }
            items.add(tmp3);
        }
        return items;
    }
}
