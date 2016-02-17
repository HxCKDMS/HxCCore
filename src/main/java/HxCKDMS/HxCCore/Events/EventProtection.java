package HxCKDMS.HxCCore.Events;

import HxCKDMS.HxCCore.Handlers.NBTFileIO;
import HxCKDMS.HxCCore.HxCCore;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.event.world.BlockEvent;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class EventProtection {
    public static LinkedHashMap<String, int[]> protectedZones = new LinkedHashMap<>();
    public static List<EntityPlayer> override = new ArrayList<>();
    @SubscribeEvent
    public void mineBlock(BlockEvent.BreakEvent event) {
        if (!protectedZones.isEmpty() && !override.contains(event.getPlayer())) {
            NBTTagCompound protectedLandList = NBTFileIO.getNbtTagCompound(HxCCore.CustomWorldData, "protectedLand");
            protectedZones.forEach((x, z) -> {
                if (z[0] == event.getPlayer().dimension) {
                    if (event.pos.getX() > z[1] && event.pos.getX() < z[4] && event.pos.getY() > z[2] && event.pos.getY() < z[5] && event.pos.getZ() > z[3] && event.pos.getZ() < z[6]) {
                        for (int i = 0; i < protectedLandList.getTagList(x, 8).tagCount(); i++) {
                            if (protectedLandList.getTagList(x, 8).getStringTagAt(i).equals(event.getPlayer().getName())) {
                                return;
                            }
                        }
                        event.setCanceled(true);
                    }
                }
            });
        }
    }

    @SubscribeEvent
    public void placeBlock(BlockEvent.PlaceEvent event) {
        if (!protectedZones.isEmpty() && !override.contains(event.player)) {
            NBTTagCompound protectedLandList = NBTFileIO.getNbtTagCompound(HxCCore.CustomWorldData, "protectedLand");
            protectedZones.forEach((x, z) -> {
                if (z[0] == event.player.dimension) {
                    if (event.pos.getX() > z[1] && event.pos.getX() < z[4] && event.pos.getY() > z[2] && event.pos.getY() < z[5] && event.pos.getZ() > z[3] && event.pos.getZ() < z[6]) {
                        for (int i = 0; i < protectedLandList.getTagList(x, 8).tagCount(); i++) {
                            if (protectedLandList.getTagList(x, 8).getStringTagAt(i).equalsIgnoreCase(event.player.getName())) {
                                return;
                            }
                        }
                        event.setCanceled(true);
                    }
                }
            });
        }
    }

    public static void load() {
        NBTTagCompound protectedLands = NBTFileIO.getNbtTagCompound(HxCCore.CustomWorldData, "protectedLands");
        NBTTagList lands = protectedLands.getTagList("lands", 8);
        for (int i = 0; i < lands.tagCount(); i++) {
            String tg = lands.getStringTagAt(i);
            String[] tgs = tg.split("="), tg2 = tgs[1].split(", ");
            int[] tg3 = new int[tg2.length];
            for (int j = 0; j < tg2.length; j++)
                tg3[j] = Integer.parseInt(tg2[j]);
            protectedZones.putIfAbsent(tg.split("=")[0], tg3);
        }
    }
}
