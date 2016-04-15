package HxCKDMS.HxCCore.api.Configuration.Handlers;

import HxCKDMS.HxCCore.api.Configuration.Config;
import HxCKDMS.HxCCore.api.Configuration.AbstractTypeHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.*;

import static HxCKDMS.HxCCore.api.Configuration.Flags.retainOriginalValues;

@SuppressWarnings({"unchecked","unused"})
public class AdvancedHandlers {

    private static Object getValue(Class<?> type, String value) {
        if (type == String.class) return value;
        else if (type == Integer.class) return Integer.parseInt(value);
        else if (type == Double.class) return Double.parseDouble(value);
        else if (type == Character.class) return value.toCharArray()[0];
        else if (type == Float.class) return Float.parseFloat(value);
        else if (type == Long.class) return Long.parseLong(value);
        else if (type == Short.class) return Short.parseShort(value);
        else if (type == Byte.class) return Byte.parseByte(value);
        else if (type == Boolean.class) return Boolean.parseBoolean(value);
        else throw new NullPointerException("fuck you!");
    }

    //LIST STUFF
    //The above comment is very descriptive @Karel
    private static void mainListWriter(Field field, LinkedHashMap<String, LinkedHashMap<String, Object>> config, HashMap<String, String> DataWatcher) throws IllegalAccessException {
        List<Object> tempList = (List<Object>) field.get(null);

        String categoryName = field.isAnnotationPresent(Config.category.class) ? field.getAnnotation(Config.category.class).value() : "General";
        StringBuilder listTextBuilder = new StringBuilder();

        listTextBuilder.append('[');
        tempList.forEach(item -> listTextBuilder.append('\n').append("\t\t").append(item));
        listTextBuilder.append('\n').append('\t').append(']');

        LinkedHashMap<String, Object> categoryValues = config.getOrDefault(categoryName, new LinkedHashMap<>());
        categoryValues.putIfAbsent(field.getName(), listTextBuilder.toString());
        config.put(categoryName, categoryValues);

        ParameterizedType parameterizedType = (ParameterizedType)field.getGenericType();
        DataWatcher.put("ListType", ((Class<?>)parameterizedType.getActualTypeArguments()[0]).getCanonicalName());
    }

    private static <T> void mainListReader(String variable, HashMap<String, String> DataWatcher, BufferedReader reader, Class<?> configClass, List<T> tempList) throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException, IOException {
        Field field = configClass.getField(variable);
        Class<T> listType = (Class<T>) Class.forName(DataWatcher.get("ListType"));
        if (field.isAnnotationPresent(Config.flags.class) && (field.getAnnotation(Config.flags.class).value() & retainOriginalValues) == 1) tempList = (List<T>) field.get(null);

        String line;
        while ((line = reader.readLine()) != null && !line.trim().equals("]")) tempList.add((T) getValue(listType, line.trim()));

        field.set(configClass, tempList);
    }

    public static class ListHandler extends AbstractTypeHandler {

        @Override
        public void write(Field field, LinkedHashMap<String, LinkedHashMap<String, Object>> config, HashMap<String, String> DataWatcher) throws IllegalAccessException {
            mainListWriter(field, config, DataWatcher);
            DataWatcher.put("Type", List.class.getCanonicalName());
        }

        @Override
        public void read(String variable, HashMap<String, String> DataWatcher, String currentLine, BufferedReader reader, Class<?> configClass) throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException, IOException {
            mainListReader(variable, DataWatcher, reader, configClass, new LinkedList<>());
        }

        @Override
        public Class<?>[] getTypes() {
            return new Class<?>[] {List.class};
        }
    }

    public static class LinkedListHandler extends AbstractTypeHandler {

        @Override
        public void write(Field field, LinkedHashMap<String, LinkedHashMap<String, Object>> config, HashMap<String, String> DataWatcher) throws IllegalAccessException {
            mainListWriter(field, config, DataWatcher);
            DataWatcher.put("Type", LinkedList.class.getCanonicalName());
        }

        @Override
        public void read(String variable, HashMap<String, String> DataWatcher, String currentLine, BufferedReader reader, Class<?> configClass) throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException, IOException {
            mainListReader(variable, DataWatcher, reader, configClass, new LinkedList<>());
        }

        @Override
        public Class<?>[] getTypes() {
            return new Class<?>[]{LinkedList.class};
        }
    }

    public static class ArrayListHandler extends AbstractTypeHandler {

        @Override
        public void write(Field field, LinkedHashMap<String, LinkedHashMap<String, Object>> config, HashMap<String, String> DataWatcher) throws IllegalAccessException {
            mainListWriter(field, config, DataWatcher);
            DataWatcher.put("Type", ArrayList.class.getCanonicalName());
        }

        @Override
        public void read(String variable, HashMap<String, String> DataWatcher, String currentLine, BufferedReader reader, Class<?> configClass) throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException, IOException {
            mainListReader(variable, DataWatcher, reader, configClass, new ArrayList<>());
        }


        @Override
        public Class<?>[] getTypes() {
            return new Class<?>[]{ArrayList.class};
        }
    }



    //MAP STUFF

    private static void mainMapWriter(Field field, LinkedHashMap<String, LinkedHashMap<String, Object>> config, HashMap<String, String> DataWatcher) throws IllegalAccessException {
        Map<Object, Object> tempMap = (Map<Object, Object>) field.get(null);

        String categoryName = field.isAnnotationPresent(Config.category.class) ? field.getAnnotation(Config.category.class).value() : "General";
        StringBuilder mapTextBuilder = new StringBuilder();

        mapTextBuilder.append('[');
        tempMap.forEach((key, value) -> mapTextBuilder.append('\n').append("\t\t").append(key).append('=').append(value));
        mapTextBuilder.append('\n').append('\t').append(']');

        LinkedHashMap<String, Object> categoryValues = config.getOrDefault(categoryName, new LinkedHashMap<>());
        categoryValues.putIfAbsent(field.getName(), mapTextBuilder.toString());
        config.put(categoryName, categoryValues);

        ParameterizedType parameterizedType = (ParameterizedType)field.getGenericType();
        DataWatcher.put("MapKeyType", ((Class<?>)parameterizedType.getActualTypeArguments()[0]).getCanonicalName());
        DataWatcher.put("MapValueType", ((Class<?>)parameterizedType.getActualTypeArguments()[1]).getCanonicalName());
    }

    private static <K,V> void mainMapReader(String variable, HashMap<String, String> DataWatcher, BufferedReader reader, Class<?> configClass, Map<K,V> tempMap) throws NoSuchFieldException, ClassNotFoundException, IOException, IllegalAccessException {
        Field field = configClass.getField(variable);
        Class<K> mapKeyType = (Class<K>) Class.forName(DataWatcher.get("MapKeyType"));
        Class<V> mapValueType = (Class<V>) Class.forName(DataWatcher.get("MapValueType"));
        if (field.isAnnotationPresent(Config.flags.class) && (field.getAnnotation(Config.flags.class).value() & retainOriginalValues) == 1) tempMap = (Map<K, V>) field.get(null);

        String line;
        while ((line = reader.readLine()) != null && !line.trim().equals("]")) tempMap.put((K)getValue(mapKeyType, line.split("=")[0].trim()), (V)getValue(mapValueType, line.split("=")[1]));

        System.out.println(field.getName() + "-" + ((ParameterizedType)field.getGenericType()).getActualTypeArguments()[1] + "-" + tempMap);
        field.set(configClass, tempMap);
    }

    public static class MapHandler extends AbstractTypeHandler {

        @Override
        public void write(Field field, LinkedHashMap<String, LinkedHashMap<String, Object>> config, HashMap<String, String> DataWatcher) throws IllegalAccessException {
            mainMapWriter(field, config, DataWatcher);
            DataWatcher.put("Type", Map.class.getCanonicalName());
        }

        @Override
        public void read(String variable, HashMap<String, String> DataWatcher, String currentLine, BufferedReader reader, Class<?> configClass) throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException, IOException {
            mainMapReader(variable, DataWatcher, reader, configClass, new HashMap<>());
        }

        @Override
        public Class<?>[] getTypes() {
            return new Class<?>[]{Map.class};
        }
    }

    public static class HashMapHandler extends AbstractTypeHandler {

        @Override
        public void write(Field field, LinkedHashMap<String, LinkedHashMap<String, Object>> config, HashMap<String, String> DataWatcher) throws IllegalAccessException {
            mainMapWriter(field, config, DataWatcher);
            DataWatcher.put("Type", HashMap.class.getCanonicalName());
        }

        @Override
        public void read(String variable, HashMap<String, String> DataWatcher, String currentLine, BufferedReader reader, Class<?> configClass) throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException, IOException {
            mainMapReader(variable, DataWatcher, reader, configClass, new HashMap<>());
        }

        @Override
        public Class<?>[] getTypes() {
            return new Class<?>[]{HashMap.class};
        }
    }

    public static class LinkedHashMapHandler extends AbstractTypeHandler {

        @Override
        public void write(Field field, LinkedHashMap<String, LinkedHashMap<String, Object>> config, HashMap<String, String> DataWatcher) throws IllegalAccessException {
            mainMapWriter(field, config, DataWatcher);
            DataWatcher.put("Type", LinkedHashMap.class.getCanonicalName());
        }

        @Override
        public void read(String variable, HashMap<String, String> DataWatcher, String currentLine, BufferedReader reader, Class<?> configClass) throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException, IOException {
            mainMapReader(variable, DataWatcher, reader, configClass, new LinkedHashMap<>());
        }

        @Override
        public Class<?>[] getTypes() {
            return new Class<?>[]{LinkedHashMap.class};
        }
    }
}
