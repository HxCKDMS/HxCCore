package hxckdms.hxccore.utilities;

import hxckdms.hxccore.libraries.GlobalVariables;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.UUID;

@SuppressWarnings({"unused", "WeakerAccess"})
public class HxCPlayerInfoHandler {
    private static Hashtable<UUID, NBTFileHandler> playerDataTable = new Hashtable<>();

    public static void setString(EntityPlayer player, String tagName, String value) {
        UUID uuid = player.getUniqueID();
        playerDataTable.get(uuid).setString(tagName, value);
    }

    public static String getString(EntityPlayer player, String tagName) {
        return getString(player, tagName, null);
    }

    public static String getString(EntityPlayer player, String tagName, String defaultValue) {
        UUID uuid = player.getUniqueID();
        return playerDataTable.containsKey(uuid) ? playerDataTable.get(uuid).getString(tagName) : defaultValue;
    }

    public static void setBoolean(EntityPlayer player, String tagName, boolean value) {
        UUID uuid = player.getUniqueID();
        playerDataTable.get(uuid).setBoolean(tagName, value);
    }

    public static boolean getBoolean(EntityPlayer player, String tagName) {
        UUID uuid = player.getUniqueID();
        return playerDataTable.containsKey(uuid) && playerDataTable.get(uuid).getBoolean(tagName);
    }

    public static void setInteger(EntityPlayer player, String tagName, int value) {
        UUID uuid = player.getUniqueID();
        playerDataTable.get(uuid).setInteger(tagName, value);
    }

    public static int getInteger(EntityPlayer player, String tagName) {
        UUID uuid = player.getUniqueID();
        return playerDataTable.containsKey(uuid) ? playerDataTable.get(uuid).getInteger(tagName) : 0;
    }

    public static void setDouble(EntityPlayer player, String tagName, double value) {
        UUID uuid = player.getUniqueID();
        playerDataTable.get(uuid).setDouble(tagName, value);
    }

    public static double getDouble(EntityPlayer player, String tagName) {
        UUID uuid = player.getUniqueID();
        return playerDataTable.containsKey(uuid) ? playerDataTable.get(uuid).getDouble(tagName) : 0D;
    }

    public static void setFloat(EntityPlayer player, String tagName, float value) {
        UUID uuid = player.getUniqueID();
        playerDataTable.get(uuid).setFloat(tagName, value);
    }

    public static float getFloat(EntityPlayer player, String tagName) {
        UUID uuid = player.getUniqueID();
        return playerDataTable.containsKey(uuid) ? playerDataTable.get(uuid).getFloat(tagName) : 0F;
    }

    public static void setShort(EntityPlayer player, String tagName, short value) {
        UUID uuid = player.getUniqueID();
        playerDataTable.get(uuid).setShort(tagName, value);
    }

    public static short getShort(EntityPlayer player, String tagName) {
        UUID uuid = player.getUniqueID();
        return playerDataTable.containsKey(uuid) ? playerDataTable.get(uuid).getShort(tagName) : 0;
    }

    public static void setLong(EntityPlayer player, String tagName, long value) {
        UUID uuid = player.getUniqueID();
        playerDataTable.get(uuid).setLong(tagName, value);
    }

    public static long getLong(EntityPlayer player, String tagName) {
        UUID uuid = player.getUniqueID();
        return playerDataTable.containsKey(uuid) ? playerDataTable.get(uuid).getLong(tagName) : 0L;
    }

    public static void setByte(EntityPlayer player, String tagName, byte value) {
        UUID uuid = player.getUniqueID();
        playerDataTable.get(uuid).setByte(tagName, value);
    }

    public static byte getByte(EntityPlayer player, String tagName) {
        UUID uuid = player.getUniqueID();
        return playerDataTable.containsKey(uuid) ? playerDataTable.get(uuid).getByte(tagName) : 0;
    }

    public static void setIntArray(EntityPlayer player, String tagName, int[] value) {
        UUID uuid = player.getUniqueID();
        playerDataTable.get(uuid).setIntArray(tagName, value);
    }

    public static int[] getIntArray(EntityPlayer player, String tagName) {
        UUID uuid = player.getUniqueID();
        return playerDataTable.containsKey(uuid) ? playerDataTable.get(uuid).getIntArray(tagName) : null;
    }

    public static void setByteArray(EntityPlayer player, String tagName, byte[] value) {
        UUID uuid = player.getUniqueID();
        playerDataTable.get(uuid).setByteArray(tagName, value);
    }

    public static byte[] getByteArray(EntityPlayer player, String tagName) {
        UUID uuid = player.getUniqueID();
        return playerDataTable.containsKey(uuid) ? playerDataTable.get(uuid).getByteArray(tagName) : null;
    }

    public static void setTagCompound(EntityPlayer player, String tagName, NBTTagCompound value) {
        UUID uuid = player.getUniqueID();
        playerDataTable.get(uuid).setTagCompound(tagName, value);
    }

    public static NBTTagCompound getTagCompound(EntityPlayer player, String tagName) {
        return getTagCompound(player, tagName, null);
    }

    //TODO: add defaultValue to every getter.
    public static NBTTagCompound getTagCompound(EntityPlayer player, String tagName, NBTTagCompound defaultValue) {
        UUID uuid = player.getUniqueID();
        return hasTag(player, tagName) ? playerDataTable.get(uuid).getTagCompound(tagName) : defaultValue;
    }

    public static void setTagList(EntityPlayer player, String tagName, NBTTagList value) {
        UUID uuid = player.getUniqueID();
        playerDataTable.get(uuid).setTagList(tagName, value);
    }

    public static NBTTagList getTagList(EntityPlayer player, String tagName) {
        UUID uuid = player.getUniqueID();
        return playerDataTable.containsKey(uuid) ? playerDataTable.get(uuid).getTagList(tagName) : null;
    }

    public static boolean hasTag(EntityPlayer player, String tagName) {
        UUID uuid = player.getUniqueID();
        return playerDataTable.containsKey(uuid) && playerDataTable.get(uuid).hasTag(tagName);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static class CustomPlayerDataEvents {
        @SubscribeEvent
        public void playerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
            UUID uuid = event.player.getUniqueID();
            File modPlayerData = new File(GlobalVariables.modWorldDir, "HxC-" + uuid.toString() + ".dat");

            try {
                if (!modPlayerData.exists()) modPlayerData.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            playerDataTable.put(uuid, new NBTFileHandler(uuid.toString(), modPlayerData));
            NBTFileHandler.loadCertainNBTFile(uuid.toString());
        }

        @SubscribeEvent
        public void playerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
            UUID uuid = event.player.getUniqueID();
            NBTFileHandler.saveCustomNBTFiles(true);
            NBTFileHandler.unRegister(uuid.toString());
            playerDataTable.remove(uuid);
        }
    }
}
