package HxCKDMS.HxCCore.api.Configuration.Handlers;

import HxCKDMS.HxCCore.api.Configuration.AbstractTypeHandler;
import HxCKDMS.HxCCore.api.Configuration.Config;

import java.io.BufferedReader;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static HxCKDMS.HxCCore.api.Configuration.Flags.overwrite;

public class BasicHandlers {
    private static void write(Field field, LinkedHashMap<String, LinkedHashMap<String, Object>> config) throws IllegalAccessException {
        String categoryName = field.isAnnotationPresent(Config.category.class) ? field.getAnnotation(Config.category.class).value() : "General";

        LinkedHashMap<String, Object> categoryValues = config.getOrDefault(categoryName, new LinkedHashMap<>());
        categoryValues.putIfAbsent(field.getName(), field.get(null));
        config.put(categoryName, categoryValues);
    }

    public static class StringHandler extends AbstractTypeHandler {

        @Override
        public void write(Field field, LinkedHashMap<String, LinkedHashMap<String, Object>> config, HashMap<String, String> DataWatcher) throws IllegalAccessException {
            BasicHandlers.write(field, config);
            DataWatcher.put("Type", String.class.getCanonicalName());
        }

        @Override
        public void read(String variable, HashMap<String, String> DataWatcher, String currentLine, BufferedReader reader, Class<?> configClass) throws NoSuchFieldException, ClassNotFoundException, IllegalAccessException {
            String value = currentLine.trim().replace(variable, "").replace("=", "");
            Field field = configClass.getField(variable);

            if (field.isAnnotationPresent(Config.flags.class) && (field.getAnnotation(Config.flags.class).value() & overwrite) == 2) {
                if (field.get(null) == "" || field.get(null) == null) {
                    if (!value.isEmpty()) field.set(configClass, value);
                }
            } else if (!value.isEmpty()) field.set(configClass, value);
        }

        @Override
        public Class<?>[] getTypes() {
            return new Class<?>[] {String.class};
        }
    }

    public static class IntegerHandler extends AbstractTypeHandler {

        @Override
        public void write(Field field, LinkedHashMap<String, LinkedHashMap<String, Object>> config, HashMap<String, String> DataWatcher) throws IllegalAccessException {
            BasicHandlers.write(field, config);
            DataWatcher.put("Type", Integer.class.getCanonicalName());
        }

        @Override
        public void read(String variable, HashMap<String, String> DataWatcher, String currentLine, BufferedReader reader, Class<?> configClass) throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException {
            String value = currentLine.trim().replace(variable, "").replace("=", "");
            Field field = configClass.getField(variable);
            try {
                if (field.isAnnotationPresent(Config.flags.class) && (field.getAnnotation(Config.flags.class).value() & overwrite) == 2) {
                    if (field.get(null) == null || (Integer) field.get(null) == 0) {
                        if (!value.isEmpty()) field.set(configClass, Integer.valueOf(value));
                    }
                }else if (!value.isEmpty()) field.set(configClass, Integer.valueOf(value));
            } catch (NumberFormatException ignored) {}
        }

        @Override
        public Class<?>[] getTypes() {
            return new Class<?>[] {Integer.class, Integer.TYPE};
        }
    }

    public static class DoubleHandler extends AbstractTypeHandler {

        @Override
        public void write(Field field, LinkedHashMap<String, LinkedHashMap<String, Object>> config, HashMap<String, String> DataWatcher) throws IllegalAccessException {
            BasicHandlers.write(field, config);
            DataWatcher.put("Type", Double.class.getCanonicalName());
        }

        @Override
        public void read(String variable, HashMap<String, String> DataWatcher, String currentLine, BufferedReader reader, Class<?> configClass) throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException {
            String value = currentLine.trim().replace(variable, "").replace("=", "");
            Field field = configClass.getField(variable);

            try {
                if (field.isAnnotationPresent(Config.flags.class) && (field.getAnnotation(Config.flags.class).value() & overwrite) == 2) {
                    if (field.get(null) == null || (Double) field.get(null) == 0) {
                        if (!value.isEmpty()) field.set(configClass, Double.valueOf(value));
                    }
                }else if (!value.isEmpty()) field.set(configClass, Double.valueOf(value));
            } catch (NumberFormatException ignored) {}
        }

        @Override
        public Class<?>[] getTypes() {
            return new Class<?>[] {Double.class, Double.TYPE};
        }
    }

    public static class CharacterHandler extends AbstractTypeHandler {

        @Override
        public void write(Field field, LinkedHashMap<String, LinkedHashMap<String, Object>> config, HashMap<String, String> DataWatcher) throws IllegalAccessException {
            BasicHandlers.write(field, config);
            DataWatcher.put("Type", Character.class.getCanonicalName());
        }

        @Override
        public void read(String variable, HashMap<String, String> DataWatcher, String currentLine, BufferedReader reader, Class<?> configClass) throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException {
            String value = currentLine.trim().replace(variable, "").replace("=", "");
            Field field = configClass.getField(variable);

            try {
                if (field.isAnnotationPresent(Config.flags.class) && (field.getAnnotation(Config.flags.class).value() & overwrite) == 2) {
                    if (field.get(null) == null || (Character) field.get(null) == ' ') {
                        if (!value.isEmpty()) field.set(configClass, value.charAt(0));
                    }
                }else if (!value.isEmpty()) field.set(configClass, value.charAt(0));
            } catch (NumberFormatException ignored) {}
        }

        @Override
        public Class<?>[] getTypes() {
            return new Class<?>[] {Character.class, Character.TYPE};
        }
    }

    public static class BooleanHandler extends AbstractTypeHandler {

        @Override
        public void write(Field field, LinkedHashMap<String, LinkedHashMap<String, Object>> config, HashMap<String, String> DataWatcher) throws IllegalAccessException {
            BasicHandlers.write(field, config);
            DataWatcher.put("Type", Boolean.class.getCanonicalName());
        }

        @Override
        public void read(String variable, HashMap<String, String> DataWatcher, String currentLine, BufferedReader reader, Class<?> configClass) throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException {
            String value = currentLine.trim().replace(variable, "").replace("=", "");
            Field field = configClass.getField(variable);

            try {
                if (field.isAnnotationPresent(Config.flags.class) && (field.getAnnotation(Config.flags.class).value() & overwrite) == 2) {
                    if (field.get(null) == null || !((Boolean) field.get(null))) {
                        if (!value.isEmpty()) field.set(configClass, Boolean.valueOf(value));
                    }
                }else if (!value.isEmpty()) field.set(configClass, Boolean.valueOf(value));
            } catch (NumberFormatException ignored) {}
        }

        @Override
        public Class<?>[] getTypes() {
            return new Class<?>[] {Boolean.class, Boolean.TYPE};
        }
    }

    public static class FloatHandler extends AbstractTypeHandler {

        @Override
        public void write(Field field, LinkedHashMap<String, LinkedHashMap<String, Object>> config, HashMap<String, String> DataWatcher) throws IllegalAccessException {
            BasicHandlers.write(field, config);
            DataWatcher.put("Type", Float.class.getCanonicalName());
        }

        @Override
        public void read(String variable, HashMap<String, String> DataWatcher, String currentLine, BufferedReader reader, Class<?> configClass) throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException {
            String value = currentLine.trim().replace(variable, "").replace("=", "");
            Field field = configClass.getField(variable);

            try {
                if (field.isAnnotationPresent(Config.flags.class) && (field.getAnnotation(Config.flags.class).value() & overwrite) == 2) {
                    if (field.get(null) == null || (Float) field.get(null) == 0) {
                        if (!value.isEmpty()) field.set(configClass, Float.valueOf(value));
                    }
                }else if (!value.isEmpty()) field.set(configClass, Float.valueOf(value));
            } catch (NumberFormatException ignored) {}
        }

        @Override
        public Class<?>[] getTypes() {
            return new Class<?>[] {Float.class, Float.TYPE};
        }
    }

    public static class ShortHandler extends AbstractTypeHandler {

        @Override
        public void write(Field field, LinkedHashMap<String, LinkedHashMap<String, Object>> config, HashMap<String, String> DataWatcher) throws IllegalAccessException {
            BasicHandlers.write(field, config);
            DataWatcher.put("Type", Short.class.getCanonicalName());
        }

        @Override
        public void read(String variable, HashMap<String, String> DataWatcher, String currentLine, BufferedReader reader, Class<?> configClass) throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException {
            String value = currentLine.trim().replace(variable, "").replace("=", "");
            Field field = configClass.getField(variable);

            try {
                if (field.isAnnotationPresent(Config.flags.class) && (field.getAnnotation(Config.flags.class).value() & overwrite) == 2) {
                    if (field.get(null) == null || (Short) field.get(null) == 0) {
                        if (!value.isEmpty()) field.set(configClass, Short.valueOf(value));
                    }
                }else if (!value.isEmpty()) field.set(configClass, Short.valueOf(value));
            } catch (NumberFormatException ignored) {}
        }

        @Override
        public Class<?>[] getTypes() {
            return new Class<?>[] {Short.class, Short.TYPE};
        }
    }

    public static class LongHandler extends AbstractTypeHandler {

        @Override
        public void write(Field field, LinkedHashMap<String, LinkedHashMap<String, Object>> config, HashMap<String, String> DataWatcher) throws IllegalAccessException {
            BasicHandlers.write(field, config);
            DataWatcher.put("Type", Long.class.getCanonicalName());
        }

        @Override
        public void read(String variable, HashMap<String, String> DataWatcher, String currentLine, BufferedReader reader, Class<?> configClass) throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException {
            String value = currentLine.trim().replace(variable, "").replace("=", "");
            Field field = configClass.getField(variable);

            try {
                if (field.isAnnotationPresent(Config.flags.class) && (field.getAnnotation(Config.flags.class).value() & overwrite) == 2) {
                    if (field.get(null) == null || (Long) field.get(null) == 0) {
                        if (!value.isEmpty()) field.set(configClass, Long.valueOf(value));
                    }
                }else if (!value.isEmpty()) field.set(configClass, Long.valueOf(value));
            } catch (NumberFormatException ignored) {}
        }

        @Override
        public Class<?>[] getTypes() {
            return new Class<?>[] {Long.class, Long.TYPE};
        }
    }

    public static class ByteHandler extends AbstractTypeHandler {

        @Override
        public void write(Field field, LinkedHashMap<String, LinkedHashMap<String, Object>> config, HashMap<String, String> DataWatcher) throws IllegalAccessException {
            BasicHandlers.write(field, config);
            DataWatcher.put("Type", Byte.class.getCanonicalName());
        }

        @Override
        public void read(String variable, HashMap<String, String> DataWatcher, String currentLine, BufferedReader reader, Class<?> configClass) throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException {
            String value = currentLine.trim().replace(variable, "").replace("=", "");
            Field field = configClass.getField(variable);

            try {
                if (field.isAnnotationPresent(Config.flags.class) && (field.getAnnotation(Config.flags.class).value() & overwrite) == 2) {
                    if (field.get(null) == null || (Byte) field.get(null) == 0) {
                        if (!value.isEmpty()) field.set(configClass, Byte.valueOf(value));
                    }
                }else if (!value.isEmpty()) field.set(configClass, Byte.valueOf(value));
            } catch (NumberFormatException ignored) {}
        }

        @Override
        public Class<?>[] getTypes() {
            return new Class<?>[] {Byte.class, Byte.TYPE};
        }
    }
}
