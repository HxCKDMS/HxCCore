package HxCKDMS.HxCCore.api.Configuration.Handlers;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ICollectionsHandler {
    List<String> writeInCollection(Field field, Object value, HashMap<String, Object> subDataWatcher, ParameterizedType parameterizedType);
    Object readFromCollection(HashMap<String, Object> subDataWatcher, String currentLine, BufferedReader reader, Map<String, Object> info) throws IOException;

    Class<?>[] getTypes();
}
