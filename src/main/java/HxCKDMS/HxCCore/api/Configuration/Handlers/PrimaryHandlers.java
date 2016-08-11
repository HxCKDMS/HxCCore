package HxCKDMS.HxCCore.api.Configuration.Handlers;


import HxCKDMS.HxCCore.api.Configuration.Config;
import HxCKDMS.HxCCore.api.Configuration.HxCConfig;

import java.io.BufferedReader;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.*;

import static HxCKDMS.HxCCore.api.Configuration.Flags.OVERWRITE;


public class PrimaryHandlers {
    private static void write(Field field, LinkedHashMap<String, LinkedHashMap<String, Object>> config) throws IllegalAccessException {
        String categoryName = field.isAnnotationPresent(Config.category.class) ? field.getAnnotation(Config.category.class).value() : "General";

        LinkedHashMap<String, Object> categoryValues = config.getOrDefault(categoryName, new LinkedHashMap<>());
        categoryValues.putIfAbsent(field.getName(), field.get(null));
        config.put(categoryName, categoryValues);
    }

    public static class StringHandler implements ITypeHandler, ICollectionsHandler {

        @Override
        public void write(Field field, LinkedHashMap<String, LinkedHashMap<String, Object>> config) throws IllegalAccessException {
            PrimaryHandlers.write(field, config);
        }

        @Override
        public void read(String variable, String currentLine, BufferedReader reader, Class<?> configClass) throws NoSuchFieldException, ClassNotFoundException, IllegalAccessException {
            String value = currentLine.trim().replace(variable, "").replace("=", "");
            Field field = HxCConfig.getField(configClass, variable);

            if (field.isAnnotationPresent(Config.flags.class) && (field.getAnnotation(Config.flags.class).value() & OVERWRITE) == OVERWRITE) {
                if ("".equals(field.get(null)) || field.get(null) == null) {
                    if (!value.isEmpty()) field.set(configClass, value);
                }
            } else if (!value.isEmpty()) field.set(configClass, value);
        }

        @Override
        public List<String> writeInCollection(Field field, Object value, HashMap<String, Object> subDataWatcher, ParameterizedType parameterizedType) {
            return Collections.singletonList(String.valueOf(value));
        }

        @Override
        public String readFromCollection(HashMap<String, Object> subDataWatcher, String currentLine, BufferedReader reader, Map<String, Object> info) {
            return currentLine;
        }

        @Override
        public Class<?>[] getTypes() {
            return new Class<?>[] {String.class};
        }
    }

    public static class IntegerHandler implements ITypeHandler, ICollectionsHandler {

        @Override
        public void write(Field field, LinkedHashMap<String, LinkedHashMap<String, Object>> config) throws IllegalAccessException {
            PrimaryHandlers.write(field, config);
        }

        @Override
        public void read(String variable, String currentLine, BufferedReader reader, Class<?> configClass) throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException {
            String value = currentLine.trim().replace(variable, "").replace("=", "");
            Field field = HxCConfig.getField(configClass, variable);
            try {
                if (field.isAnnotationPresent(Config.flags.class) && (field.getAnnotation(Config.flags.class).value() & OVERWRITE) == OVERWRITE) {
                    if (field.get(null) == null || (Integer) field.get(null) == 0) {
                        if (!value.isEmpty()) field.set(configClass, Integer.valueOf(value));
                    }
                }else if (!value.isEmpty()) field.set(configClass, Integer.valueOf(value));
            } catch (NumberFormatException ignored) {}
        }

        @Override
        public List<String> writeInCollection(Field field, Object value, HashMap<String, Object> subDataWatcher, ParameterizedType parameterizedType) {
            return Collections.singletonList(String.valueOf(value));
        }

        @Override
        public Integer readFromCollection(HashMap<String, Object> subDataWatcher, String currentLine, BufferedReader reader, Map<String, Object> info) {
            return Integer.parseInt(currentLine);
        }

        @Override
        public Class<?>[] getTypes() {
            return new Class<?>[] {Integer.class, Integer.TYPE};
        }
    }

    public static class DoubleHandler implements ITypeHandler, ICollectionsHandler {

        @Override
        public void write(Field field, LinkedHashMap<String, LinkedHashMap<String, Object>> config) throws IllegalAccessException {
            PrimaryHandlers.write(field, config);
        }

        @Override
        public void read(String variable, String currentLine, BufferedReader reader, Class<?> configClass) throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException {
            String value = currentLine.trim().replace(variable, "").replace("=", "");
            Field field = HxCConfig.getField(configClass, variable);

            try {
                if (field.isAnnotationPresent(Config.flags.class) && (field.getAnnotation(Config.flags.class).value() & OVERWRITE) == OVERWRITE) {
                    if (field.get(null) == null || (Double) field.get(null) == 0) {
                        if (!value.isEmpty()) field.set(configClass, Double.valueOf(value));
                    }
                }else if (!value.isEmpty()) field.set(configClass, Double.valueOf(value));
            } catch (NumberFormatException ignored) {}
        }

        @Override
        public List<String> writeInCollection(Field field, Object value, HashMap<String, Object> subDataWatcher, ParameterizedType parameterizedType) {
            return Collections.singletonList(String.valueOf(value));
        }

        @Override
        public Double readFromCollection(HashMap<String, Object> subDataWatcher, String currentLine, BufferedReader reader, Map<String, Object> info) {
            return Double.parseDouble(currentLine);
        }

        @Override
        public Class<?>[] getTypes() {
            return new Class<?>[] {Double.class, Double.TYPE};
        }
    }

    public static class CharacterHandler implements ITypeHandler, ICollectionsHandler {

        @Override
        public void write(Field field, LinkedHashMap<String, LinkedHashMap<String, Object>> config) throws IllegalAccessException {
            PrimaryHandlers.write(field, config);
        }

        @Override
        public void read(String variable, String currentLine, BufferedReader reader, Class<?> configClass) throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException {
            String value = currentLine.trim().replace(variable, "").replace("=", "");
            Field field = HxCConfig.getField(configClass, variable);

            try {
                if (field.isAnnotationPresent(Config.flags.class) && (field.getAnnotation(Config.flags.class).value() & OVERWRITE) == OVERWRITE) {
                    if (field.get(null) == null || (Character) field.get(null) == ' ') {
                        if (!value.isEmpty()) field.set(configClass, value.charAt(0));
                    }
                }else if (!value.isEmpty()) field.set(configClass, value.charAt(0));
            } catch (NumberFormatException ignored) {}
        }

        @Override
        public List<String> writeInCollection(Field field, Object value, HashMap<String, Object> subDataWatcher, ParameterizedType parameterizedType) {
            return Collections.singletonList(String.valueOf(value));
        }

        @Override
        public Character readFromCollection(HashMap<String, Object> subDataWatcher, String currentLine, BufferedReader reader, Map<String, Object> info) {
            return currentLine.charAt(0);
        }

        @Override
        public Class<?>[] getTypes() {
            return new Class<?>[] {Character.class, Character.TYPE};
        }
    }

    public static class BooleanHandler implements ITypeHandler, ICollectionsHandler {

        @Override
        public void write(Field field, LinkedHashMap<String, LinkedHashMap<String, Object>> config) throws IllegalAccessException {
            PrimaryHandlers.write(field, config);
        }

        @Override
        public void read(String variable, String currentLine, BufferedReader reader, Class<?> configClass) throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException {
            String value = currentLine.trim().replace(variable, "").replace("=", "");
            Field field = HxCConfig.getField(configClass, variable);

            try {
                if (field.isAnnotationPresent(Config.flags.class) && (field.getAnnotation(Config.flags.class).value() & OVERWRITE) == OVERWRITE) {
                    if (field.get(null) == null || !((Boolean) field.get(null))) {
                        if (!value.isEmpty()) field.set(configClass, Boolean.valueOf(value));
                    }
                }else if (!value.isEmpty()) field.set(configClass, Boolean.valueOf(value));
            } catch (NumberFormatException ignored) {}
        }

        @Override
        public List<String> writeInCollection(Field field, Object value, HashMap<String, Object> subDataWatcher, ParameterizedType parameterizedType) {
            return Collections.singletonList(String.valueOf(value));
        }

        @Override
        public Boolean readFromCollection(HashMap<String, Object> subDataWatcher, String currentLine, BufferedReader reader, Map<String, Object> info) {
            return Boolean.valueOf(currentLine);
        }

        @Override
        public Class<?>[] getTypes() {
            return new Class<?>[] {Boolean.class, Boolean.TYPE};
        }
    }

    public static class FloatHandler implements ITypeHandler, ICollectionsHandler {

        @Override
        public void write(Field field, LinkedHashMap<String, LinkedHashMap<String, Object>> config) throws IllegalAccessException {
            PrimaryHandlers.write(field, config);
        }

        @Override
        public void read(String variable, String currentLine, BufferedReader reader, Class<?> configClass) throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException {
            String value = currentLine.trim().replace(variable, "").replace("=", "");
            Field field = HxCConfig.getField(configClass, variable);

            try {
                if (field.isAnnotationPresent(Config.flags.class) && (field.getAnnotation(Config.flags.class).value() & OVERWRITE) == OVERWRITE) {
                    if (field.get(null) == null || (Float) field.get(null) == 0) {
                        if (!value.isEmpty()) field.set(configClass, Float.valueOf(value));
                    }
                }else if (!value.isEmpty()) field.set(configClass, Float.valueOf(value));
            } catch (NumberFormatException ignored) {}
        }

        @Override
        public List<String> writeInCollection(Field field, Object value, HashMap<String, Object> subDataWatcher, ParameterizedType parameterizedType) {
            return Collections.singletonList(String.valueOf(value));
        }

        @Override
        public Float readFromCollection(HashMap<String, Object> subDataWatcher, String currentLine, BufferedReader reader, Map<String, Object> info) {
            return Float.valueOf(currentLine);
        }

        @Override
        public Class<?>[] getTypes() {
            return new Class<?>[] {Float.class, Float.TYPE};
        }
    }

    public static class ShortHandler implements ITypeHandler, ICollectionsHandler {

        @Override
        public void write(Field field, LinkedHashMap<String, LinkedHashMap<String, Object>> config) throws IllegalAccessException {
            PrimaryHandlers.write(field, config);
        }

        @Override
        public void read(String variable, String currentLine, BufferedReader reader, Class<?> configClass) throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException {
            String value = currentLine.trim().replace(variable, "").replace("=", "");
            Field field = HxCConfig.getField(configClass, variable);

            try {
                if (field.isAnnotationPresent(Config.flags.class) && (field.getAnnotation(Config.flags.class).value() & OVERWRITE) == OVERWRITE) {
                    if (field.get(null) == null || (Short) field.get(null) == 0) {
                        if (!value.isEmpty()) field.set(configClass, Short.valueOf(value));
                    }
                }else if (!value.isEmpty()) field.set(configClass, Short.valueOf(value));
            } catch (NumberFormatException ignored) {}
        }

        @Override
        public List<String> writeInCollection(Field field, Object value, HashMap<String, Object> subDataWatcher, ParameterizedType parameterizedType) {
            return Collections.singletonList(String.valueOf(value));
        }

        @Override
        public Short readFromCollection(HashMap<String, Object> subDataWatcher, String currentLine, BufferedReader reader, Map<String, Object> info) {
            return Short.valueOf(currentLine);
        }

        @Override
        public Class<?>[] getTypes() {
            return new Class<?>[] {Short.class, Short.TYPE};
        }
    }

    public static class LongHandler implements ITypeHandler, ICollectionsHandler {

        @Override
        public void write(Field field, LinkedHashMap<String, LinkedHashMap<String, Object>> config) throws IllegalAccessException {
            PrimaryHandlers.write(field, config);
        }

        @Override
        public void read(String variable, String currentLine, BufferedReader reader, Class<?> configClass) throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException {
            String value = currentLine.trim().replace(variable, "").replace("=", "");
            Field field = HxCConfig.getField(configClass, variable);

            try {
                if (field.isAnnotationPresent(Config.flags.class) && (field.getAnnotation(Config.flags.class).value() & OVERWRITE) == OVERWRITE) {
                    if (field.get(null) == null || (Long) field.get(null) == 0) {
                        if (!value.isEmpty()) field.set(configClass, Long.valueOf(value));
                    }
                }else if (!value.isEmpty()) field.set(configClass, Long.valueOf(value));
            } catch (NumberFormatException ignored) {}
        }

        @Override
        public List<String> writeInCollection(Field field, Object value, HashMap<String, Object> subDataWatcher, ParameterizedType parameterizedType) {
            return Collections.singletonList(String.valueOf(value));
        }

        @Override
        public Long readFromCollection(HashMap<String, Object> subDataWatcher, String currentLine, BufferedReader reader, Map<String, Object> info) {
            return Long.valueOf(currentLine);
        }

        @Override
        public Class<?>[] getTypes() {
            return new Class<?>[] {Long.class, Long.TYPE};
        }
    }

    public static class ByteHandler implements ITypeHandler, ICollectionsHandler {

        @Override
        public void write(Field field, LinkedHashMap<String, LinkedHashMap<String, Object>> config) throws IllegalAccessException {
            PrimaryHandlers.write(field, config);
        }

        @Override
        public void read(String variable, String currentLine, BufferedReader reader, Class<?> configClass) throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException {
            String value = currentLine.trim().replace(variable, "").replace("=", "");
            Field field = HxCConfig.getField(configClass, variable);

            try {
                if (field.isAnnotationPresent(Config.flags.class) && (field.getAnnotation(Config.flags.class).value() & OVERWRITE) == OVERWRITE) {
                    if (field.get(null) == null || (Byte) field.get(null) == 0) {
                        if (!value.isEmpty()) field.set(configClass, Byte.valueOf(value));
                    }
                }else if (!value.isEmpty()) field.set(configClass, Byte.valueOf(value));
            } catch (NumberFormatException ignored) {}
        }

        @Override
        public List<String> writeInCollection(Field field, Object value, HashMap<String, Object> subDataWatcher, ParameterizedType parameterizedType) {
            return Collections.singletonList(String.valueOf(value));
        }

        @Override
        public Byte readFromCollection(HashMap<String, Object> subDataWatcher, String currentLine, BufferedReader reader, Map<String, Object> info) {
            return Byte.valueOf(currentLine);
        }

        @Override
        public Class<?>[] getTypes() {
            return new Class<?>[] {Byte.class, Byte.TYPE};
        }
    }
}