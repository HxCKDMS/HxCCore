package HxCKDMS.HxCCore.api.Configuration;

import net.minecraft.nbt.NBTTagCompound;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;

public abstract class AbstractTypeHandler {
    public abstract void write(Field field, LinkedHashMap<String, LinkedHashMap<String, Object>> config, NBTTagCompound DataWatcher) throws IllegalAccessException;

    public abstract void read(String variable, NBTTagCompound DataWatcher, String currentLine, BufferedReader reader, Class<?> configClass) throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException, IOException;

    public abstract Class<?>[] getTypes();
}
