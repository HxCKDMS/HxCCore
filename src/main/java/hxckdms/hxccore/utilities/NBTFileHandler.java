package hxckdms.hxccore.utilities;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;

public class NBTFileHandler {
    private static HashSet<NBTFileHandler> fileHandlers = new HashSet<>();
    private Hashtable<String, Data> table = new Hashtable<>();
    private File nbtFile;

    public NBTFileHandler(File nbtFile) {
        this.nbtFile = nbtFile;

        readFromFile();
        fileHandlers.add(this);
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

    private synchronized <T> void setValue(String tagName, Class<T> type, T value) {
        table.put(tagName, new Data<>(value, type));
    }

    private synchronized <T> T getValue(Class<T> type, String tagName) {
        if(table.get(tagName).type == type )
            return ((Data<T>) table.get(tagName)).value ;
        else throw new NullPointerException();
    }

    private synchronized boolean hasTag(String tagName) {
        return table.containsKey(tagName);
    }

    private synchronized void saveToFile() {
        try {
            if (!nbtFile.exists()) nbtFile.createNewFile();
        } catch (Exception ignored) {}

        NBTTagCompound tagCompound = new NBTTagCompound();

        System.out.println(table);

        for (Map.Entry<String, Data> entry : table.entrySet()) {
            if (entry.getValue().type == String.class) tagCompound.setString(entry.getKey(), (String) entry.getValue().value);
            //else if (entry.getValue().type == Boolean.class) tagCompound.setBoolean(entry.getKey(), (Boolean) entry.getValue().value);
            else if (entry.getValue().type == Integer.class) tagCompound.setInteger(entry.getKey(), (Integer) entry.getValue().value);
            else if (entry.getValue().type == Double.class) tagCompound.setDouble(entry.getKey(), (Double) entry.getValue().value);
            else if (entry.getValue().type == Float.class) tagCompound.setFloat(entry.getKey(), (Float) entry.getValue().value);
            else if (entry.getValue().type == Short.class) tagCompound.setShort(entry.getKey(), (Short) entry.getValue().value);
            else if (entry.getValue().type == Long.class) tagCompound.setLong(entry.getKey(), (Long) entry.getValue().value);
            else if (entry.getValue().type == Byte.class) tagCompound.setByte(entry.getKey(), (Byte) entry.getValue().value);
            else if (entry.getValue().type == int[].class) tagCompound.setIntArray(entry.getKey(), (int[]) entry.getValue().value);
            else if (entry.getValue().type == byte[].class) tagCompound.setByteArray(entry.getKey(), (byte[]) entry.getValue().value);
            else if (entry.getValue().type == NBTTagCompound.class) tagCompound.setTag(entry.getKey(), (NBTBase) entry.getValue().value);
        }

        try {
            CompressedStreamTools.safeWrite(tagCompound, nbtFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private synchronized void readFromFile() {
        NBTTagCompound tagCompound;
        try {
            tagCompound = CompressedStreamTools.read(nbtFile);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        table.clear();
        for (String key : tagCompound.getKeySet()) {
            NBTBase base = tagCompound.getTag(key);

            switch (NBTBase.NBT_TYPES[base.getId()]) {
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

            }
        }
    }

    private class Data<T> {
        private T value;
        private Class<T> type;

        private Data(T value, Class<T> type) {
            this.value = value;
            this.type = type;
        }
    }

    public static void loadCustomNBTFiles() {
        //fileHandlers.parallelStream().forEach(NBTFileHandler::readFromFile);
    }

    public static void saveCustomNBTFiles() {
        fileHandlers.parallelStream().forEach(NBTFileHandler::saveToFile);
    }
}