package HxCKDMS.HxCCore.api.Configuration.Handlers;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;

public interface ITypeHandler {
    void write(Field field, LinkedHashMap<String, LinkedHashMap<String, Object>> config) throws IllegalAccessException;
    void read(String variable, String currentLine, BufferedReader reader, Class<?> configClass) throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException, IOException;

    Class<?>[] getTypes();
}
