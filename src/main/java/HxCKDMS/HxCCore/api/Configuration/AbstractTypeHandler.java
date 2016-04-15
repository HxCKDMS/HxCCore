package HxCKDMS.HxCCore.api.Configuration;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedHashMap;

public abstract class AbstractTypeHandler {
    public abstract void write(Field field, LinkedHashMap<String, LinkedHashMap<String, Object>> config, HashMap<String, String> DataWatcher) throws IllegalAccessException;

    public abstract void read(String variable, HashMap<String, String> DataWatcher, String currentLine, BufferedReader reader, Class<?> configClass) throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException, IOException;

    public abstract Class<?>[] getTypes();
}
