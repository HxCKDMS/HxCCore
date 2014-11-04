package HxCKDMS.HxCCore.Handlers;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;

import java.io.File;
import java.io.IOException;

public class NBTFileIO {
    public static NBTTagCompound getNbtTagCompound(File dataFile, String tag){
        NBTTagCompound tagCompound = new NBTTagCompound();
        try{
            tagCompound = CompressedStreamTools.read(dataFile);
        }catch(IOException e){
            e.printStackTrace();
            return tagCompound;
        }
        return tagCompound.getCompoundTag(tag);
    }

    public static void setNbtTagCompound(File dataFile, String tag, NBTTagCompound tagCompound){
        NBTTagCompound data;
        try{
            data = CompressedStreamTools.read(dataFile);
            data.setTag(tag, tagCompound);
            CompressedStreamTools.write(data, dataFile);
        }catch(Exception e){
            try {
                data = new NBTTagCompound();
                data.setTag(tag, tagCompound);
                CompressedStreamTools.write(data, dataFile);
            }catch(IOException exception){
                exception.printStackTrace();
            }
        }
    }

    public static boolean getBoolean(File dataFile, String tag){
        NBTTagCompound data;
        try{
            data = CompressedStreamTools.read(dataFile);
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return data.getBoolean(tag);
    }

    public static void setBoolean(File dataFile, String tag, Boolean bool){
        NBTTagCompound data;
        try{
            data = CompressedStreamTools.read(dataFile);
            data.setBoolean(tag, bool);
            CompressedStreamTools.write(data, dataFile);
        }catch(Exception e){
            try{
                data = new NBTTagCompound();
                data.setBoolean(tag, bool);
                CompressedStreamTools.write(data, dataFile);
            }catch(Exception exception){
                exception.printStackTrace();
            }
        }
    }

    public static String getString(File datafile, String tag){
        NBTTagCompound data;
        try{
            data = CompressedStreamTools.read(datafile);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
        return data.getString(tag);
    }

    public static void setString(File dataFile, String tag, String string){
        NBTTagCompound data;
        try{
            data = CompressedStreamTools.read(dataFile);
            data.setString(tag, string);
            CompressedStreamTools.write(data, dataFile);
        }catch(Exception e){
            try{
                data = new NBTTagCompound();
                data.setString(tag, string);
                CompressedStreamTools.write(data, dataFile);
            }catch(Exception exception){
                exception.printStackTrace();
            }
        }
    }
}
