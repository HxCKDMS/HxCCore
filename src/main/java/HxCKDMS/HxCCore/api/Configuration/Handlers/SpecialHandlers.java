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

public class SpecialHandlers {
    private static List<Class> classes = new ArrayList<>();

    public static void registerSpecialClass(Class clazz) {
        classes.add(clazz);
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public static class SpecialClassHandler implements ITypeHandler, IMultiLineHandler, ICollectionsHandler {

        @Override
        public void write(Field field, LinkedHashMap<String, LinkedHashMap<String, Object>> config) throws IllegalAccessException {
            List<Field> fields = Arrays.asList(field.get(null).getClass().getDeclaredFields());
            String categoryName = field.isAnnotationPresent(Config.category.class) ? field.getAnnotation(Config.category.class).value() : "General";
            StringBuilder classTextBuilder = new StringBuilder();

            classTextBuilder.append('[');
            for (Field aField : fields) {
                Object value = aField.get(field.get(null));
                String fName = aField.getName();

                Type type = aField.getGenericType();
                boolean isParameterized = (type instanceof ParameterizedType);
                Class<?> cType = isParameterized ? (Class<?>) ((ParameterizedType) type).getRawType() : (Class<?>) type;
                ICollectionsHandler cHandler = HxCConfig.getCollectionsHandler(cType);

                classTextBuilder.append("\n\t\t").append(fName).append('=').append(cHandler.writeInCollection(aField, value, null, isParameterized ? (ParameterizedType) type : null).stream().map(str -> "\t\t" + str).reduce((a, b) -> a + '\n' + b).get().trim());
            }
            classTextBuilder.append("\n\t]");
            LinkedHashMap<String, Object> categoryValues = config.getOrDefault(categoryName, new LinkedHashMap<>());
            categoryValues.putIfAbsent(field.getName(), classTextBuilder.toString());
            config.put(categoryName, categoryValues);
        }

        @Override
        public void read(String variable, String currentLine, BufferedReader reader, Class<?> configClass) throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException, IOException {
            Field field = HxCConfig.getField(configClass, variable);
            if (field.isAnnotationPresent(Config.flags.class) && (field.getAnnotation(Config.flags.class).value() & OVERWRITE) == OVERWRITE && field.get(null) != null) return;

            String line;
            String fName = "";
            while ((line = reader.readLine()) != null && !line.trim().startsWith("]")) try {
                if (fName.isEmpty()) fName = line.split("=")[0].trim();

                if (line.contains("=") && !fName.isEmpty()) {

                    Field aField = HxCConfig.getField(field.get(null).getClass(), fName);
                    ICollectionsHandler cHandler = HxCConfig.getCollectionsHandler(aField.getType());

                    Map<String, Object> info = new HashMap<>();
                    info.put("Type", aField.getGenericType());

                    aField.set(field.get(null), cHandler.readFromCollection(null, line.split("=")[1].trim(), reader, info));

                    fName = "";
                }
            } catch (Exception ignored) {
                ignored.printStackTrace();
            }
        }

        @Override
        public List<String> writeInCollection(Field field, Object value, HashMap<String, Object> not, ParameterizedType parameterizedType) {
            List<Field> fields = Arrays.asList(value.getClass().getDeclaredFields());
            LinkedList<String> lines = new LinkedList<>();

            lines.add("[");

            for (Field aField : fields) try {
                Object fValue = aField.get(value);
                String fName = aField.getName();

                Type type = aField.getGenericType();
                boolean isParameterized = (type instanceof ParameterizedType);
                Class<?> cType = isParameterized ? (Class<?>) ((ParameterizedType) type).getRawType() : (Class<?>) type;
                ICollectionsHandler cHandler = HxCConfig.getCollectionsHandler(cType);

                LinkedList<String> itValue = new LinkedList<>(cHandler.writeInCollection(field, fValue, null, isParameterized ? (ParameterizedType) type : null).stream().map(str -> "\t" +str).collect(Collectors.toList()));
                String valueFirst = itValue.getFirst();
                itValue.removeFirst();

                lines.add('\t' + fName + "=" + valueFirst.trim());
                lines.addAll(itValue);

            } catch (Exception ignored) {
                ignored.printStackTrace();
            }
            lines.add("]");

            return lines;
        }

        @Override
        public Object readFromCollection(HashMap<String, Object> not, String currentLine, BufferedReader reader, Map<String, Object> info) throws IOException {
            Class<?> type = (Class<?>) info.get("Type");
            Object instance;
            try {
                instance = type.newInstance();
            } catch (Exception ignored) {
                ignored.printStackTrace();
                return null;
            }

            String line;
            String fName = "";

            while ((line = reader.readLine()) != null && !line.trim().equals("]")) try {
                if (fName.isEmpty()) fName = line.split("=")[0].trim();

                if (line.contains("=") && !fName.isEmpty()) {
                    Field field = HxCConfig.getField(type, fName);

                    Map<String, Object> innerInfo = new HashMap<>();
                    innerInfo.put("Type", field.getGenericType());

                    ICollectionsHandler cHandler = HxCConfig.getCollectionsHandler(field.getType());

                    field.set(instance, cHandler.readFromCollection(null, line.split("=")[1].trim(), reader, innerInfo));

                    fName = "";
                }
            } catch (Exception ignored) {
                ignored.printStackTrace();
            }

            return instance;
        }

        @Override
        public Class<?>[] getTypes() {
            Class<?>[] tmp = new Class<?>[classes.size()];
            return classes.toArray(tmp);
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
