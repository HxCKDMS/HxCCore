package HxCKDMS.HxCCore.api.Handlers;

import HxCKDMS.HxCCore.Configs.Configurations;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;

import java.io.File;
import java.io.IOException;

@SuppressWarnings("unused")
public class NBTFileIO {
    public static NBTTagCompound getData(File dataFile) {
        try {
            return CompressedStreamTools.read(dataFile);
        } catch (IOException e) {
            if (Configurations.DebugMode)
                e.printStackTrace();
            return new NBTTagCompound();
        }
    }

    public static NBTTagCompound getNbtTagCompound(File dataFile, String tag) {
        NBTTagCompound tagCompound = getData(dataFile);
        if (tagCompound == null || !tagCompound.hasKey(tag)) return new NBTTagCompound();
        return tagCompound.getCompoundTag(tag);
    }

    public static void setNbtTagCompound(File dataFile, String tag, NBTTagCompound tagCompound) {
        NBTTagCompound data;
        try {
            data = CompressedStreamTools.read(dataFile);
            data.setTag(tag, tagCompound);
            CompressedStreamTools.write(data, dataFile);
        } catch (Exception e) {
            try {
                data = new NBTTagCompound();
                data.setTag(tag, tagCompound);
                CompressedStreamTools.write(data, dataFile);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }

    public static boolean getBoolean(File dataFile, String tag) {
        NBTTagCompound data = getData(dataFile);
        return !(data == null || !data.hasKey(tag)) && data.getBoolean(tag);
    }

    public static void setBoolean(File dataFile, String tag, Boolean bool) {
        NBTTagCompound data;
        try {
            data = CompressedStreamTools.read(dataFile);
            data.setBoolean(tag, bool);
            CompressedStreamTools.write(data, dataFile);
        } catch (Exception e) {
            try {
                data = new NBTTagCompound();
                data.setBoolean(tag, bool);
                CompressedStreamTools.write(data, dataFile);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    public static String getString(File dataFile, String tag) {
        NBTTagCompound data = getData(dataFile);
        if (data == null || !data.hasKey(tag)) return "";
        return data.getString(tag);
    }

    public static void setString(File dataFile, String tag, String string) {
        NBTTagCompound data;
        try {
            data = CompressedStreamTools.read(dataFile);
            data.setString(tag, string);
            CompressedStreamTools.write(data, dataFile);
        } catch (Exception e) {
            try {
                data = new NBTTagCompound();
                data.setString(tag, string);
                CompressedStreamTools.write(data, dataFile);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    public static int getInteger(File dataFile, String tag) {
        NBTTagCompound data = getData(dataFile);
        if (data == null || !data.hasKey(tag)) return 0;
        return data.getInteger(tag);
    }

    public static void setInteger(File dataFile, String tag, int i) {
        NBTTagCompound data;
        try {
            data = CompressedStreamTools.read(dataFile);
            data.setInteger(tag, i);
            CompressedStreamTools.write(data, dataFile);
        } catch (Exception e) {
            try {
                data = new NBTTagCompound();
                data.setInteger(tag, i);
                CompressedStreamTools.write(data, dataFile);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    public static double getDouble(File dataFile, String tag) {
        NBTTagCompound data = getData(dataFile);
        if (data == null || !data.hasKey(tag)) return 0;
        return data.getDouble(tag);
    }

    public static void setDouble(File dataFile, String tag, double i) {
        NBTTagCompound data;
        try {
            data = CompressedStreamTools.read(dataFile);
            data.setDouble(tag, i);
            CompressedStreamTools.write(data, dataFile);
        } catch (Exception e) {
            try {
                data = new NBTTagCompound();
                data.setDouble(tag, i);
                CompressedStreamTools.write(data, dataFile);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    public static float getFloat(File dataFile, String tag) {
        NBTTagCompound data = getData(dataFile);
        if (data == null || !data.hasKey(tag)) return 0;
        return data.getFloat(tag);
    }

    public static void setFloat(File dataFile, String tag, float i) {
        NBTTagCompound data;
        try {
            data = CompressedStreamTools.read(dataFile);
            data.setFloat(tag, i);
            CompressedStreamTools.write(data, dataFile);
        } catch (Exception e) {
            try {
                data = new NBTTagCompound();
                data.setFloat(tag, i);
                CompressedStreamTools.write(data, dataFile);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    public static short getShort(File dataFile, String tag) {
        NBTTagCompound data = getData(dataFile);
        if (data == null || !data.hasKey(tag)) return 0;
        return data.getShort(tag);
    }

    public static void setShort(File dataFile, String tag, short i) {
        NBTTagCompound data;
        try {
            data = CompressedStreamTools.read(dataFile);
            data.setShort(tag, i);
            CompressedStreamTools.write(data, dataFile);
        } catch (Exception e) {
            try {
                data = new NBTTagCompound();
                data.setShort(tag, i);
                CompressedStreamTools.write(data, dataFile);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    public static long getLong(File dataFile, String tag) {
        NBTTagCompound data = getData(dataFile);
        if (data == null || !data.hasKey(tag)) return 0;
        return data.getLong(tag);
    }

    public static void setLong(File dataFile, String tag, long i) {
        NBTTagCompound data;
        try {
            data = CompressedStreamTools.read(dataFile);
            data.setLong(tag, i);
            CompressedStreamTools.write(data, dataFile);
        } catch (Exception e) {
            try {
                data = new NBTTagCompound();
                data.setLong(tag, i);
                CompressedStreamTools.write(data, dataFile);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    public static byte getByte(File dataFile, String tag) {
        NBTTagCompound data = getData(dataFile);
        if (data == null || !data.hasKey(tag)) return 0;
        return data.getByte(tag);
    }

    public static void setByte(File dataFile, String tag, byte i) {
        NBTTagCompound data;
        try {
            data = CompressedStreamTools.read(dataFile);
            data.setByte(tag, i);
            CompressedStreamTools.write(data, dataFile);
        } catch (Exception e) {
            try {
                data = new NBTTagCompound();
                data.setByte(tag, i);
                CompressedStreamTools.write(data, dataFile);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    public static int[] getIntArray(File dataFile, String tag) {
        NBTTagCompound data = getData(dataFile);
        if (data == null || !data.hasKey(tag)) return new int[1];
        return data.getIntArray(tag);
    }

    public static void setIntArray(File dataFile, String tag, int[] i) {
        NBTTagCompound data;
        try {
            data = CompressedStreamTools.read(dataFile);
            data.setIntArray(tag, i);
            CompressedStreamTools.write(data, dataFile);
        } catch (Exception e) {
            try {
                data = new NBTTagCompound();
                data.setIntArray(tag, i);
                CompressedStreamTools.write(data, dataFile);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    public static byte[] getByteArray(File dataFile, String tag) {
        NBTTagCompound data = getData(dataFile);
        if (data == null || !data.hasKey(tag)) return new byte[1];
        return data.getByteArray(tag);
    }

    public static void setByteArray(File dataFile, String tag, byte[] i) {
        NBTTagCompound data;
        try {
            data = CompressedStreamTools.read(dataFile);
            data.setByteArray(tag, i);
            CompressedStreamTools.write(data, dataFile);
        } catch (Exception e) {
            try {
                data = new NBTTagCompound();
                data.setByteArray(tag, i);
                CompressedStreamTools.write(data, dataFile);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    public static boolean hasKey(File dataFile, String tag) {
        NBTTagCompound data = getData(dataFile);
        return data != null && data.hasKey(tag);
    }
}
