package HxCKDMS.HxCCore.api.Configuration.New;

import net.minecraft.nbt.NBTTagList;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;

public abstract class AbstractTypeHandler {
    public abstract boolean write(Field field, LinkedHashMap<String, LinkedHashMap<String, String>> config, NBTTagList DataWatcher) throws IllegalAccessException;

    public abstract boolean read(Field field, LinkedHashMap<String, LinkedHashMap<String, String>> config, NBTTagList DataWatcher) throws IllegalAccessException;

    public abstract Class<?>[] getTypes();
}
