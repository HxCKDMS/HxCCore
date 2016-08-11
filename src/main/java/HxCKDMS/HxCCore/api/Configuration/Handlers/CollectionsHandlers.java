package HxCKDMS.HxCCore.api.Configuration.Handlers;


import HxCKDMS.HxCCore.api.Configuration.Config;
import HxCKDMS.HxCCore.api.Configuration.HxCConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

import static HxCKDMS.HxCCore.api.Configuration.Flags.OVERWRITE;
import static HxCKDMS.HxCCore.api.Configuration.Flags.RETAIN_ORIGINAL_VALUES;

@SuppressWarnings("unchecked")
public class CollectionsHandlers {

    //LIST STUFF
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    private static void mainListWriter(Field field, LinkedHashMap<String, LinkedHashMap<String, Object>> config) throws IllegalAccessException {
        Type[] types = ((ParameterizedType)field.getGenericType()).getActualTypeArguments();

        boolean isParameterized = (types[0] instanceof ParameterizedType);

        Class<?> type = isParameterized ? (Class<?>) ((ParameterizedType) types[0]).getRawType() : (Class<?>) types[0];
        ICollectionsHandler cHandler = HxCConfig.getCollectionsHandler(type);

        List<Object> tempList = (List<Object>) field.get(null);

        String categoryName = field.isAnnotationPresent(Config.category.class) ? field.getAnnotation(Config.category.class).value() : "General";
        StringBuilder listTextBuilder = new StringBuilder();

        listTextBuilder.append('[');
        for (Object value : tempList) {
            listTextBuilder.append('\n').append(cHandler.writeInCollection(field, value, null, isParameterized ? (ParameterizedType) types[0] : null).stream().map(str -> "\t\t" + str).reduce((a, b) -> a + "\n" + b).get());
        }
        listTextBuilder.append('\n').append('\t').append(']');

        LinkedHashMap<String, Object> categoryValues = config.getOrDefault(categoryName, new LinkedHashMap<>());
        categoryValues.putIfAbsent(field.getName(), listTextBuilder.toString());
        config.put(categoryName, categoryValues);
    }

    private static List<String> mainListCollectionWriter(Field field, List<Object> value, ParameterizedType parameterizedType) {
        Type[] types = parameterizedType.getActualTypeArguments();
        boolean isParameterized = (types[0] instanceof ParameterizedType);
        Class<?> type = isParameterized ? (Class<?>) ((ParameterizedType) types[0]).getRawType() : (Class<?>) types[0];

        ICollectionsHandler cHandler = HxCConfig.getCollectionsHandler(type);

        LinkedList<String> lines = new LinkedList<>();

        lines.add("[");

        for (Object obj : value) {
            lines.addAll(cHandler.writeInCollection(field, obj, null, isParameterized ? (ParameterizedType) types[0] : null).stream().map(str -> "\t" + str).collect(Collectors.toList()));
        }
        lines.add("]");

        return lines;
    }

    private static <T> void mainListReader(String variable, BufferedReader reader, Class<?> configClass, List<T> tempList) throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException, IOException {
        HashMap<String, Object> info = new HashMap<>();

        Field field = HxCConfig.getField(configClass, variable);
        Type[] types = ((ParameterizedType) field.getGenericType()).getActualTypeArguments();
        boolean isParameterized = (types[0] instanceof ParameterizedType);

        Class<T> listType = isParameterized ? (Class<T>) ((ParameterizedType) types[0]).getRawType() : (Class<T>) types[0];
        ICollectionsHandler cHandler = HxCConfig.getCollectionsHandler(listType);

        info.put("Type", types[0]);

        if (field.isAnnotationPresent(Config.flags.class) && (field.getAnnotation(Config.flags.class).value() & RETAIN_ORIGINAL_VALUES) == RETAIN_ORIGINAL_VALUES) tempList = (List<T>) field.get(null);

        String line;
        while ((line = reader.readLine()) != null && !line.trim().equals("]")) try { tempList.add((T) cHandler.readFromCollection(null, line.trim(), reader, info)); } catch (Exception ignored) {}

        if (field.isAnnotationPresent(Config.flags.class) && (field.getAnnotation(Config.flags.class).value() & OVERWRITE) == OVERWRITE) {
            if (field.get(null) == null || ((List) field.get(null)).isEmpty()) field.set(configClass, tempList);
        } else field.set(configClass, tempList);
    }

    private static <T> List mainListCollectionReader(BufferedReader reader, Map<String, Object> info, List<T> tempList) throws IOException {

        Type[] types = ((ParameterizedType) info.get("Type")).getActualTypeArguments();
        boolean isParameterized = (types[0] instanceof ParameterizedType);
        Class<T> listType = isParameterized ? (Class<T>) ((ParameterizedType) types[0]).getRawType() : (Class<T>) types[0];

        ICollectionsHandler cHandler = HxCConfig.getCollectionsHandler(listType);

        Map<String, Object> innerInfo = new HashMap<>();
        innerInfo.clear();
        innerInfo.put("Type", types[0]);

        String line;
        while ((line = reader.readLine()) != null && !line.trim().startsWith("]")) try {
            if (cHandler instanceof IMultiLineHandler && ((IMultiLineHandler) cHandler).beginChar() == line.trim().charAt(0)) {
                tempList.add((T) cHandler.readFromCollection(null, line.trim(), reader, innerInfo));
                continue;
            }

            tempList.add((T) cHandler.readFromCollection(null, line.trim(), reader, innerInfo));

            reader.mark(1000000);

        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
        return tempList;
    }

    public static class ListHandler implements ITypeHandler, IMultiLineHandler, ICollectionsHandler {

        @Override
        public void write(Field field, LinkedHashMap<String, LinkedHashMap<String, Object>> config) throws IllegalAccessException {
            mainListWriter(field, config);
        }

        @Override
        public void read(String variable, String currentLine, BufferedReader reader, Class<?> configClass) throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException, IOException {
            mainListReader(variable, reader, configClass, new LinkedList<>());
        }

        @Override
        public List<String> writeInCollection(Field field, Object value, HashMap<String, Object> subDataWatcher, ParameterizedType parameterizedType) {
            return mainListCollectionWriter(field, (List) value, parameterizedType);
        }

        @Override
        public List readFromCollection(HashMap<String, Object> subDataWatcher, String currentLine, BufferedReader reader, Map<String, Object> info) throws IOException {
            return mainListCollectionReader(reader, info, new ArrayList<>());
        }

        @Override
        public Class<?>[] getTypes() {
            return new Class<?>[] {List.class};
        }

        @Override
        public char beginChar() {
            return '[';
        }

        @Override
        public char endChar() {
            return ']';
        }
    }

    public static class LinkedListHandler implements ITypeHandler, IMultiLineHandler, ICollectionsHandler {

        @Override
        public void write(Field field, LinkedHashMap<String, LinkedHashMap<String, Object>> config) throws IllegalAccessException {
            mainListWriter(field, config);
        }

        @Override
        public void read(String variable, String currentLine, BufferedReader reader, Class<?> configClass) throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException, IOException {
            mainListReader(variable, reader, configClass, new LinkedList<>());
        }

        @Override
        public List<String> writeInCollection(Field field, Object value, HashMap<String, Object> subDataWatcher, ParameterizedType parameterizedType) {
            return mainListCollectionWriter(field, (List) value, parameterizedType);
        }

        @Override
        public LinkedList readFromCollection(HashMap<String, Object> subDataWatcher, String currentLine, BufferedReader reader, Map<String, Object> info) throws IOException {
            return (LinkedList) mainListCollectionReader(reader, info, new LinkedList<>());
        }

        @Override
        public Class<?>[] getTypes() {
            return new Class<?>[]{LinkedList.class};
        }

        @Override
        public char beginChar() {
            return '[';
        }

        @Override
        public char endChar() {
            return ']';
        }
    }

    public static class ArrayListHandler implements ITypeHandler, IMultiLineHandler, ICollectionsHandler {

        @Override
        public void write(Field field, LinkedHashMap<String, LinkedHashMap<String, Object>> config) throws IllegalAccessException {
            mainListWriter(field, config);
        }

        @Override
        public void read(String variable, String currentLine, BufferedReader reader, Class<?> configClass) throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException, IOException {
            mainListReader(variable, reader, configClass, new ArrayList<>());
        }


        @Override
        public List<String> writeInCollection(Field field, Object value, HashMap<String, Object> subDataWatcher, ParameterizedType parameterizedType) {
            return mainListCollectionWriter(field, (List) value, parameterizedType);
        }

        @Override
        public ArrayList readFromCollection(HashMap<String, Object> subDataWatcher, String currentLine, BufferedReader reader, Map<String, Object> info) throws IOException {
            return (ArrayList) mainListCollectionReader(reader, info, new ArrayList<>());
        }

        @Override
        public Class<?>[] getTypes() {
            return new Class<?>[]{ArrayList.class};
        }

        @Override
        public char beginChar() {
            return '[';
        }

        @Override
        public char endChar() {
            return ']';
        }
    }

    //MAP STUFF

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    private static void mainMapWriter(Field field, LinkedHashMap<String, LinkedHashMap<String, Object>> config) throws IllegalAccessException {
        Map<Object, Object> tempMap = (Map<Object, Object>) field.get(null);

        Type[] types = ((ParameterizedType)field.getGenericType()).getActualTypeArguments();

        boolean isKeyParameterized = types[0] instanceof ParameterizedType;
        boolean isValueParameterized = types[1] instanceof ParameterizedType;

        Class<?> keyType = isKeyParameterized ? (Class<?>) ((ParameterizedType) types[0]).getRawType() : (Class<?>) types[0];
        Class<?> valueType = isValueParameterized ? (Class<?>) ((ParameterizedType) types[1]).getRawType() : (Class<?>) types[1];

        ICollectionsHandler cKeyHandler = HxCConfig.getCollectionsHandler(keyType);
        ICollectionsHandler cValueHandler = HxCConfig.getCollectionsHandler(valueType);

        String categoryName = field.isAnnotationPresent(Config.category.class) ? field.getAnnotation(Config.category.class).value() : "General";
        StringBuilder mapTextBuilder = new StringBuilder();

        mapTextBuilder.append('[');
        for (Map.Entry<Object, Object> entry: tempMap.entrySet()) {
            mapTextBuilder.append('\n').append("\t\t").append(cKeyHandler.writeInCollection(field, entry.getKey(), null, isKeyParameterized ? (ParameterizedType) types[0] : null).stream().reduce((a, b) -> a + "\n\t\t" + b).get()).append('=').append(cValueHandler.writeInCollection(field, entry.getValue(), null, isValueParameterized ? (ParameterizedType) types[1] : null).stream().reduce((a, b) -> a + "\n\t\t" + b).get());
        }
        mapTextBuilder.append('\n').append('\t').append(']');

        LinkedHashMap<String, Object> categoryValues = config.getOrDefault(categoryName, new LinkedHashMap<>());
        categoryValues.putIfAbsent(field.getName(), mapTextBuilder.toString());
        config.put(categoryName, categoryValues);
    }

    private static List<String> mainMapCollectionWriter(Field field, Map<Object, Object> value, ParameterizedType parameterizedType) {
        Type[] types = parameterizedType.getActualTypeArguments();
        boolean isKeyParameterized = (types[0] instanceof ParameterizedType);
        boolean isValueParameterized = (types[1] instanceof ParameterizedType);
        Class<?> keyType = isKeyParameterized ? (Class<?>) ((ParameterizedType) types[0]).getRawType() : (Class<?>) types[0];
        Class<?> valueType = isValueParameterized ? (Class<?>) ((ParameterizedType) types[1]).getRawType() : (Class<?>) types[1];

        ICollectionsHandler cKeyHandler = HxCConfig.getCollectionsHandler(keyType);
        ICollectionsHandler cValueHandler = HxCConfig.getCollectionsHandler(valueType);

        LinkedList<String> lines = new LinkedList<>();

        lines.add("[");
        for (Map.Entry<Object, Object> entry : value.entrySet()) {
            LinkedList<String> itKey = new LinkedList<>(cKeyHandler.writeInCollection(field, entry.getKey(), null, isKeyParameterized ? (ParameterizedType) types[0] : null).stream().map(str -> "\t" + str).collect(Collectors.toList()));
            LinkedList<String> itValue = new LinkedList<>(cValueHandler.writeInCollection(field, entry.getValue(), null, isValueParameterized ? (ParameterizedType) types[1] : null).stream().map(str -> "\t" + str).collect(Collectors.toList()));
            String keyLast = itKey.getLast();
            String valueFirst = itValue.getFirst();
            itKey.removeLast();
            itValue.removeFirst();

            lines.addAll(itKey);
            lines.add(keyLast + "=" + valueFirst.trim());
            lines.addAll(itValue);
        }
        lines.add("]");

        return lines;
    }

    private static <K,V> void mainMapReader(String variable, BufferedReader reader, Class<?> configClass, Map<K,V> tempMap) throws NoSuchFieldException, ClassNotFoundException, IOException, IllegalAccessException {
        Field field = configClass.getDeclaredField(variable);

        HashMap<String, Object> keyInfo = new HashMap<>();
        HashMap<String, Object> valueInfo = new HashMap<>();

        Type[] types = ((ParameterizedType)field.getGenericType()).getActualTypeArguments();

        boolean isKeyParameterized = types[0] instanceof ParameterizedType;
        boolean isValueParameterized = types[1] instanceof ParameterizedType;


        Class<K> mapKeyType = isKeyParameterized ? (Class<K>) ((ParameterizedType) types[0]).getRawType() : (Class<K>) types[0];
        Class<V> mapValueType = isValueParameterized ? (Class<V>) ((ParameterizedType) types[1]).getRawType() : (Class<V>) types[1];


        ICollectionsHandler cKeyHandler = HxCConfig.getCollectionsHandler(mapKeyType);
        ICollectionsHandler cValueHandler = HxCConfig.getCollectionsHandler(mapValueType);

        keyInfo.put("Type", types[0]);
        valueInfo.put("Type", types[1]);

        if (field.isAnnotationPresent(Config.flags.class) && (field.getAnnotation(Config.flags.class).value() & RETAIN_ORIGINAL_VALUES) == RETAIN_ORIGINAL_VALUES) tempMap = (Map<K, V>) field.get(null);

        String line;
        K key = null;
        while ((line = reader.readLine()) != null && !line.trim().equals("]")) try {
            if (key == null) {
                key = (K) cKeyHandler.readFromCollection(null, line.split("=")[0].trim(), reader, keyInfo);

                try {
                    reader.reset();
                    line = reader.readLine();
                    if (line == null) break;
                } catch (IOException ignored) {}
            }

            if (line.contains("=")) {
                tempMap.put(key, (V) cValueHandler.readFromCollection(null, line.split("=")[1].trim(), reader, valueInfo));
                key = null;
            }
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }

        if (field.isAnnotationPresent(Config.flags.class) && (field.getAnnotation(Config.flags.class).value() & OVERWRITE) == OVERWRITE) {
            if (field.get(null) == null || ((Map) field.get(null)).isEmpty()) field.set(configClass, tempMap);
        } else field.set(configClass, tempMap);
    }

    private static <K, V> Map mainMapCollectionReader(BufferedReader reader, Map<String, Object> info, Map<K, V> tempMap) throws IOException {
        Type[] types = ((ParameterizedType) info.get("Type")).getActualTypeArguments();

        boolean isKeyParameterized = types[0] instanceof ParameterizedType;
        boolean isValueParameterized = types[1] instanceof ParameterizedType;

        Class<K> mapKeyType = isKeyParameterized ? (Class<K>) ((ParameterizedType) types[0]).getRawType() : (Class<K>) types[0];
        Class<V> mapValueType = isValueParameterized ? (Class<V>) ((ParameterizedType) types[1]).getRawType() : (Class<V>) types[1];

        ICollectionsHandler cKeyHandler = HxCConfig.getCollectionsHandler(mapKeyType);
        ICollectionsHandler cValueHandler = HxCConfig.getCollectionsHandler(mapValueType);

        Map<String, Object> keyInnerInfo = new HashMap<>();
        keyInnerInfo.put("Type", types[0]);
        Map<String, Object> valueInnerInfo = new HashMap<>();
        valueInnerInfo.put("Type", types[1]);

        String line;
        K key = null;
        while ((line = reader.readLine()) != null && !line.trim().startsWith("]")) try {
            if (key == null) key = (K) cKeyHandler.readFromCollection(null, line.split("=")[0].trim(), reader, keyInnerInfo);

            if (line.contains("=")) {
                tempMap.put(key, (V) cValueHandler.readFromCollection(null, line.split("=")[1].trim(), reader, valueInnerInfo));
                key = null;
            }

            reader.mark(1000000);
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }

        return tempMap;
    }

    public static class MapHandler implements ITypeHandler, IMultiLineHandler, ICollectionsHandler {

        @Override
        public void write(Field field, LinkedHashMap<String, LinkedHashMap<String, Object>> config) throws IllegalAccessException {
            mainMapWriter(field, config);
        }

        @Override
        public void read(String variable, String currentLine, BufferedReader reader, Class<?> configClass) throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException, IOException {
            mainMapReader(variable, reader, configClass, new HashMap<>());
        }

        @Override
        public List<String> writeInCollection(Field field, Object value, HashMap<String, Object> subDataWatcher, ParameterizedType parameterizedType) {
            return mainMapCollectionWriter(field, (Map) value, parameterizedType);
        }

        @Override
        public Object readFromCollection(HashMap<String, Object> subDataWatcher, String currentLine, BufferedReader reader, Map<String, Object> info) throws IOException {
            return mainMapCollectionReader(reader, info, new HashMap<>());
        }

        @Override
        public Class<?>[] getTypes() {
            return new Class<?>[]{Map.class};
        }

        @Override
        public char beginChar() {
            return '[';
        }

        @Override
        public char endChar() {
            return ']';
        }
    }

    public static class HashMapHandler implements ITypeHandler, IMultiLineHandler, ICollectionsHandler {

        @Override
        public void write(Field field, LinkedHashMap<String, LinkedHashMap<String, Object>> config) throws IllegalAccessException {
            mainMapWriter(field, config);
        }

        @Override
        public void read(String variable, String currentLine, BufferedReader reader, Class<?> configClass) throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException, IOException {
            mainMapReader(variable, reader, configClass, new HashMap<>());
        }

        @Override
        public List<String> writeInCollection(Field field, Object value, HashMap<String, Object> subDataWatcher, ParameterizedType parameterizedType) {
            return mainMapCollectionWriter(field, (Map) value, parameterizedType);
        }

        @Override
        public Object readFromCollection(HashMap<String, Object> subDataWatcher, String currentLine, BufferedReader reader, Map<String, Object> info) throws IOException {
            return mainMapCollectionReader(reader, info, new HashMap<>());
        }

        @Override
        public Class<?>[] getTypes() {
            return new Class<?>[]{HashMap.class};
        }

        @Override
        public char beginChar() {
            return '[';
        }

        @Override
        public char endChar() {
            return ']';
        }
    }

    public static class LinkedHashMapHandler implements ITypeHandler, IMultiLineHandler, ICollectionsHandler {

        @Override
        public void write(Field field, LinkedHashMap<String, LinkedHashMap<String, Object>> config) throws IllegalAccessException {
            mainMapWriter(field, config);
        }

        @Override
        public void read(String variable, String currentLine, BufferedReader reader, Class<?> configClass) throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException, IOException {
            mainMapReader(variable, reader, configClass, new LinkedHashMap<>());
        }

        @Override
        public List<String> writeInCollection(Field field, Object value, HashMap<String, Object> subDataWatcher, ParameterizedType parameterizedType) {
            return mainMapCollectionWriter(field, (Map) value, parameterizedType);
        }

        @Override
        public Object readFromCollection(HashMap<String, Object> subDataWatcher, String currentLine, BufferedReader reader, Map<String, Object> info) throws IOException {
            return mainMapCollectionReader(reader, info, new LinkedHashMap<>());
        }

        @Override
        public Class<?>[] getTypes() {
            return new Class<?>[]{LinkedHashMap.class};
        }

        @Override
        public char beginChar() {
            return '[';
        }

        @Override
        public char endChar() {
            return ']';
        }
    }
}