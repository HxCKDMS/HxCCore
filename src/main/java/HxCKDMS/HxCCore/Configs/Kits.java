package HxCKDMS.HxCCore.Configs;

import HxCKDMS.HxCCore.api.Configuration.Config;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class Kits {
    @Config.Map
    public static LinkedHashMap<String, String> Kits = new LinkedHashMap<>();
    @Config.Map
    public static LinkedHashMap<String, Integer> KitPerms = new LinkedHashMap<>();

    static {
        Kits.put("KitPotato", "{<minecraft:potato:0> = 16, <minecraft:potato:1> = 4, <minecraft:iron_sword:30> = 1}");
        Kits.put("KitSwords", "{<minecraft:diamond_sword:0> = 1, <minecraft:gold_sword:0> = 1, <minecraft:iron_sword:0> = 1}");
        Kits.put("KitBasic", "{<minecraft:stone_pickaxe:0> = 1, <minecraft:stone_axe:0> = 1, <minecraft:stone_shovel:0> = 1}");

        KitPerms.put("KitPotato", 1);
        KitPerms.put("KitSwords", 2);
        KitPerms.put("KitBasic", 0);
    }

    public static boolean canGetKit(int permLevel, String kit) {
        return KitPerms.get(kit) <= permLevel;
    }

    public static ItemStack[] getItems(String Kit) {
        String[] vals = Kits.get(Kit).substring(1, Kits.get(Kit).length()-2).split(", ");
        List<ItemStack> items = new ArrayList<>();
        for (String tmp : vals) {
            int num = Integer.parseInt(tmp.substring(tmp.lastIndexOf("=")).replace("=", "").trim());
            tmp = tmp.replace("=", "").replace(String.valueOf(num), "").trim();
            tmp = tmp.substring(1, tmp.length() - 1);
            String[] tmp2 = tmp.split(":");
            ItemStack tmp3 = new ItemStack(GameRegistry.findItem(tmp2[0], tmp2[1]), num);
            tmp3.setMetadata(Integer.parseInt(tmp2[2]));
            items.add(tmp3);
        }
        return (ItemStack[])items.toArray();
    }
}
