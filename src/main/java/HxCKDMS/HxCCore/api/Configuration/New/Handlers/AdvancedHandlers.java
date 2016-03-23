package HxCKDMS.HxCCore.api.Configuration.New.Handlers;

import HxCKDMS.HxCCore.api.Configuration.New.AbstractTypeHandler;
import HxCKDMS.HxCCore.api.Configuration.New.Config;
import net.minecraft.nbt.NBTTagCompound;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.*;

public class AdvancedHandlers {

    //LIST STUFF
    private static void mainListWriter(Field field, LinkedHashMap<String, LinkedHashMap<String, Object>> config, NBTTagCompound DataWatcher) throws IllegalAccessException {
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
        DataWatcher.setString("ListType", ((Class<?>)parameterizedType.getActualTypeArguments()[0]).getCanonicalName());
    }

    private static <T> void mainListReader(String variable, NBTTagCompound DataWatcher, BufferedReader reader, Class<?> configClass, List<T> tempList) throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException, IOException {
        Field field = configClass.getField(variable);
        Class<T> listType = (Class<T>) Class.forName(DataWatcher.getString("ListType"));

        String line;
        while ((line = reader.readLine()) != null && !line.trim().equals("]")) tempList.add((T)line.trim());

        field.set(configClass, tempList);
    }

    public static class ListHandler extends AbstractTypeHandler {

        @Override
        public void write(Field field, LinkedHashMap<String, LinkedHashMap<String, Object>> config, NBTTagCompound DataWatcher) throws IllegalAccessException {
            mainListWriter(field, config, DataWatcher);
            DataWatcher.setString("Type", List.class.getCanonicalName());
        }

        @Override
        public void read(String variable, NBTTagCompound DataWatcher, String currentLine, BufferedReader reader, Class<?> configClass) throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException, IOException {
            mainListReader(variable, DataWatcher, reader, configClass, new LinkedList<>());
        }

        @Override
        public Class<?>[] getTypes() {
            return new Class<?>[] {List.class};
        }
    }

    public static class LinkedListHandler extends AbstractTypeHandler {

        @Override
        public void write(Field field, LinkedHashMap<String, LinkedHashMap<String, Object>> config, NBTTagCompound DataWatcher) throws IllegalAccessException {
            mainListWriter(field, config, DataWatcher);
            DataWatcher.setString("Type", LinkedList.class.getCanonicalName());
        }

        @Override
        public void read(String variable, NBTTagCompound DataWatcher, String currentLine, BufferedReader reader, Class<?> configClass) throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException, IOException {
            mainListReader(variable, DataWatcher, reader, configClass, new LinkedList<>());
        }

        @Override
        public Class<?>[] getTypes() {
            return new Class<?>[]{LinkedList.class};
        }
    }

    public static class ArrayListHandler extends AbstractTypeHandler {

        @Override
        public void write(Field field, LinkedHashMap<String, LinkedHashMap<String, Object>> config, NBTTagCompound DataWatcher) throws IllegalAccessException {
            mainListWriter(field, config, DataWatcher);
            DataWatcher.setString("Type", ArrayList.class.getCanonicalName());
        }

        @Override
        public void read(String variable, NBTTagCompound DataWatcher, String currentLine, BufferedReader reader, Class<?> configClass) throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException, IOException {
            mainListReader(variable, DataWatcher, reader, configClass, new ArrayList<>());
        }


        @Override
        public Class<?>[] getTypes() {
            return new Class<?>[]{ArrayList.class};
        }
    }



    //MAP STUFF

    private static void mainMapWriter(Field field, LinkedHashMap<String, LinkedHashMap<String, Object>> config, NBTTagCompound DataWatcher) throws IllegalAccessException {
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
        DataWatcher.setString("MapKeyType", ((Class<?>)parameterizedType.getActualTypeArguments()[0]).getCanonicalName());
        DataWatcher.setString("MapValueType", ((Class<?>)parameterizedType.getActualTypeArguments()[1]).getCanonicalName());
    }

    private static <K,V> void mainMapReader(String variable, NBTTagCompound DataWatcher, BufferedReader reader, Class<?> configClass, Map<K,V> tempMap) throws NoSuchFieldException, ClassNotFoundException, IOException, IllegalAccessException {
        Field field = configClass.getField(variable);
        Class<K> mapKeyType = (Class<K>) Class.forName(DataWatcher.getString("MapKeyType"));
        Class<V> mapValueType = (Class<V>) Class.forName(DataWatcher.getString("MapValueType"));

        String line;
        while ((line = reader.readLine()) != null && !line.trim().equals("]")) tempMap.put((K)line.split("=")[0].trim(), (V)line.split("=")[1]);

        field.set(configClass, tempMap);
    }

    public static class MapHandler extends AbstractTypeHandler {

        @Override
        public void write(Field field, LinkedHashMap<String, LinkedHashMap<String, Object>> config, NBTTagCompound DataWatcher) throws IllegalAccessException {
            mainMapWriter(field, config, DataWatcher);
            DataWatcher.setString("Type", Map.class.getCanonicalName());
        }

        @Override
        public void read(String variable, NBTTagCompound DataWatcher, String currentLine, BufferedReader reader, Class<?> configClass) throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException, IOException {
            mainMapReader(variable, DataWatcher, reader, configClass, new HashMap<>());
        }

        @Override
        public Class<?>[] getTypes() {
            return new Class<?>[]{Map.class};
        }
    }

    public static class HashMapHandler extends AbstractTypeHandler {

        @Override
        public void write(Field field, LinkedHashMap<String, LinkedHashMap<String, Object>> config, NBTTagCompound DataWatcher) throws IllegalAccessException {
            mainMapWriter(field, config, DataWatcher);
            DataWatcher.setString("Type", HashMap.class.getCanonicalName());
        }

        @Override
        public void read(String variable, NBTTagCompound DataWatcher, String currentLine, BufferedReader reader, Class<?> configClass) throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException, IOException {
            mainMapReader(variable, DataWatcher, reader, configClass, new HashMap<>());
        }

        @Override
        public Class<?>[] getTypes() {
            return new Class<?>[]{HashMap.class};
        }
    }

    public static class LinkedHashMapHandler extends AbstractTypeHandler {

        @Override
        public void write(Field field, LinkedHashMap<String, LinkedHashMap<String, Object>> config, NBTTagCompound DataWatcher) throws IllegalAccessException {
            mainMapWriter(field, config, DataWatcher);
            DataWatcher.setString("Type", LinkedHashMap.class.getCanonicalName());
        }

        @Override
        public void read(String variable, NBTTagCompound DataWatcher, String currentLine, BufferedReader reader, Class<?> configClass) throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException, IOException {
            mainMapReader(variable, DataWatcher, reader, configClass, new LinkedHashMap<>());
        }

        @Override
        public Class<?>[] getTypes() {
            return new Class<?>[]{LinkedHashMap.class};
        }
    }
}
