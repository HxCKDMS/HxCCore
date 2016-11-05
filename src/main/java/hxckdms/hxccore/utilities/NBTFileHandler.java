package hxckdms.hxccore.utilities;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import hxckdms.hxccore.configs.Configuration;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.event.world.WorldEvent;

import java.io.File;
import java.io.IOException;
import java.util.*;

@SuppressWarnings({"unused", "WeakerAccess"})
public class NBTFileHandler {
    private static Hashtable<String, NBTFileHandler> fileHandlers = new Hashtable<>();
    private Hashtable<String, Data> table = new Hashtable<>();
    private File nbtFile;
    private long saveTime = 0, loadTime = 0;

    public NBTFileHandler(String handlerName, File nbtFile) {
        this.nbtFile = nbtFile;

        readFromFile(true);
        fileHandlers.putIfAbsent(handlerName, this);
    }

    public static void unRegister(String handlerName) {
        fileHandlers.remove(handlerName);
    }

    public static Set<String> getHandlers() {
        return new HashSet<>(fileHandlers.keySet());
    }

    public void setString(String tagName, String value) {
        setValue(tagName, String.class, value);
    }

    public String getString(String tagName) {
        return hasTag(tagName) ? getValue(String.class, tagName) : null;
    }

    public void setBoolean(String tagName, boolean value) {
        setValue(tagName, Byte.class, value ? (byte) 1 : (byte) 0);
    }

    public boolean getBoolean(String tagName) {
        return hasTag(tagName) && getValue(Byte.class, tagName) == (byte) 1;
    }

    public void setInteger(String tagName, int value) {
        setValue(tagName, Integer.class, value);
    }

    public int getInteger(String tagName) {
        return hasTag(tagName) ? getValue(Integer.class, tagName) : 0;
    }


    public void setDouble(String tagName, double value) {
        setValue(tagName, Double.class, value);
    }

    public double getDouble(String tagName) {
        return hasTag(tagName) ? getValue(Double.class, tagName) : 0D;
    }

    public void setFloat(String tagName, float value) {
        setValue(tagName, Float.class, value);
    }

    public float getFloat(String tagName) {
        return hasTag(tagName) ? getValue(Float.class, tagName) : 0F;
    }

    public void setShort(String tagName, short value) {
        setValue(tagName, Short.class, value);
    }

    public short getShort(String tagName) {
        return hasTag(tagName) ? getValue(Short.class, tagName) : 0;
    }

    public void setLong(String tagName, long value) {
        setValue(tagName, Long.class, value);
    }

    public long getLong(String tagName) {
        return hasTag(tagName) ? getValue(Long.class, tagName) : 0;
    }

    public void setByte(String tagName, byte value) {
        setValue(tagName, Byte.class, value);
    }

    public byte getByte(String tagName) {
        return hasTag(tagName) ? getValue(Byte.class, tagName) : 0;
    }

    public void setIntArray(String tagName, int[] value) {
        setValue(tagName, int[].class, value);
    }

    public int[] getIntArray(String tagName) {
        return hasTag(tagName) ? getValue(int[].class, tagName) : null;
    }

    public void setByteArray(String tagName, byte[] value) {
        setValue(tagName, byte[].class, value);
    }

    public byte[] getByteArray(String tagName) {
        return hasTag(tagName) ? getValue(byte[].class, tagName) : null;
    }

    public void setTagCompound(String tagName, NBTTagCompound value) {
        setValue(tagName, NBTTagCompound.class, value);
    }

    public NBTTagCompound getTagCompound(String tagName) {
        return getTagCompound(tagName, null);
    }

    //TODO: add defaultValue to every getter.
    public NBTTagCompound getTagCompound(String tagName, NBTTagCompound defaultValue) {
        return hasTag(tagName) ? getValue(NBTTagCompound.class, tagName) : defaultValue;
    }

    public void setTagList(String tagName, NBTTagList value) {
        setValue(tagName, NBTTagList.class, value);
    }

    public NBTTagList getTagList(String tagName) {
        return hasTag(tagName) ? getValue(NBTTagList.class, tagName) : null;
    }


    private synchronized <T> void setValue(String tagName, Class<T> type, T value) {
        table.put(tagName, new Data<>(value, type));
    }

    @SuppressWarnings("unchecked")
    private synchronized <T> T getValue(Class<T> type, String tagName) {
        if(table.get(tagName).type == type )
            return ((Data<T>) table.get(tagName)).value ;
        else throw new NullPointerException();
    }

    public synchronized boolean hasTag(String tagName) {
        return table.containsKey(tagName);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private synchronized void saveToFile(boolean force) {
        if (!force && Calendar.getInstance().getTimeInMillis() - saveTime < 10000L) return;
        saveTime = Calendar.getInstance().getTimeInMillis();

        try {
            if (!nbtFile.exists()) nbtFile.createNewFile();
        } catch (Exception ignored) {}

        NBTTagCompound tagCompound = new NBTTagCompound();

        for (Map.Entry<String, Data> entry : table.entrySet()) {
            if (entry.getValue().type == String.class) tagCompound.setString(entry.getKey(), (String) entry.getValue().value);
            else if (entry.getValue().type == Integer.class) tagCompound.setInteger(entry.getKey(), (Integer) entry.getValue().value);
            else if (entry.getValue().type == Double.class) tagCompound.setDouble(entry.getKey(), (Double) entry.getValue().value);
            else if (entry.getValue().type == Float.class) tagCompound.setFloat(entry.getKey(), (Float) entry.getValue().value);
            else if (entry.getValue().type == Short.class) tagCompound.setShort(entry.getKey(), (Short) entry.getValue().value);
            else if (entry.getValue().type == Long.class) tagCompound.setLong(entry.getKey(), (Long) entry.getValue().value);
            else if (entry.getValue().type == Byte.class) tagCompound.setByte(entry.getKey(), (Byte) entry.getValue().value);
            else if (entry.getValue().type == int[].class) tagCompound.setIntArray(entry.getKey(), (int[]) entry.getValue().value);
            else if (entry.getValue().type == byte[].class) tagCompound.setByteArray(entry.getKey(), (byte[]) entry.getValue().value);
            else if (entry.getValue().type == NBTTagCompound.class) tagCompound.setTag(entry.getKey(), (NBTBase) entry.getValue().value);
            else if (entry.getValue().type == NBTTagList.class) tagCompound.setTag(entry.getKey(), (NBTBase) entry.getValue().value);
        }

        try {
            CompressedStreamTools.safeWrite(tagCompound, nbtFile);
        } catch (IOException e) {
            if (Configuration.debugMode) e.printStackTrace();
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private synchronized void readFromFile(boolean force) {
        if (!force && Calendar.getInstance().getTimeInMillis() - loadTime < 10000L) return;
        loadTime = Calendar.getInstance().getTimeInMillis();

        NBTTagCompound tagCompound;
        try {
            if (!nbtFile.exists()) nbtFile.createNewFile();
            tagCompound = CompressedStreamTools.read(nbtFile);
        } catch (IOException e) {
            if (Configuration.debugMode) e.printStackTrace();
            return;
        }

        table.clear();
        for (String key : (Set<String>) tagCompound.func_150296_c()) {
            NBTBase base = tagCompound.getTag(key);

            switch (NBTBase.NBTTypes[base.getId()]) {
                case "STRING":
                    table.put(key, new Data<>(tagCompound.getString(key), String.class));
                    break;
                case "INT":
                    table.put(key, new Data<>(tagCompound.getInteger(key), Integer.class));
                    break;
                case "DOUBLE":
                    table.put(key, new Data<>(tagCompound.getDouble(key), Double.class));
                    break;
                case "FLOAT":
                    table.put(key, new Data<>(tagCompound.getFloat(key), Float.class));
                    break;
                case "SHORT":
                    table.put(key, new Data<>(tagCompound.getShort(key), Short.class));
                    break;
                case "LONG":
                    table.put(key, new Data<>(tagCompound.getLong(key), Long.class));
                    break;
                case "BYTE":
                    table.put(key, new Data<>(tagCompound.getByte(key), Byte.class));
                    break;
                case "INT[]":
                    table.put(key, new Data<>(tagCompound.getIntArray(key), int[].class));
                    break;
                case "BYTE[]":
                    table.put(key, new Data<>(tagCompound.getByteArray(key), byte[].class));
                    break;
                case "COMPOUND":
                    table.put(key, new Data<>((NBTTagCompound) tagCompound.getTag(key), NBTTagCompound.class));
                    break;
                case "LIST":
                    table.put(key, new Data<>((NBTTagList) tagCompound.getTag(key), NBTTagList.class));
                    break;

            }
        }
    }

    private static class Data<T> {
        private T value;
        private Class<T> type;

        private Data(T value, Class<T> type) {
            this.value = value;
            this.type = type;
        }
    }

    public static void loadCertainNBTFile(final String name) {
        fileHandlers.get(name).readFromFile(true);
    }

    public static void loadCustomNBTFiles(final boolean force) {
        fileHandlers.forEach((name, handler) -> handler.readFromFile(force));
    }

    public static void saveCertainNBTFile(final String name) {
        fileHandlers.get(name).saveToFile(true);
    }

    public static void saveCustomNBTFiles(final boolean force) {
        fileHandlers.forEach((name, handler) -> handler.saveToFile(force));
    }

    public static class NBTSaveEvents implements EventListener {

        @SubscribeEvent
        public void worldSaveEvent(WorldEvent.Save event) {
            saveCustomNBTFiles(false);
        }
    }
}