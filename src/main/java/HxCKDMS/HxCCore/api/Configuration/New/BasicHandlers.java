package HxCKDMS.HxCCore.api.Configuration.New;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;

public class BasicHandlers {
    private static boolean write(Field field, LinkedHashMap<String, LinkedHashMap<String, String>> config, NBTTagList DataWatcher) throws IllegalAccessException {
        String categoryName = field.isAnnotationPresent(Config.category.class) ? field.getAnnotation(Config.category.class).value() : "Default";

        LinkedHashMap<String, String> categoryValues = config.getOrDefault(categoryName, new LinkedHashMap<>());
        categoryValues.putIfAbsent(field.getName(), field.get(null).toString());
        config.put(categoryName, categoryValues);

        NBTTagCompound type = new NBTTagCompound(), category = new NBTTagCompound();
        type.setString("Type", field.getType().toString());
        category.setString("Category", categoryName);
        DataWatcher.appendTag(type);
        DataWatcher.appendTag(category);

        return true;
    }

    public static class StringHandler extends AbstractTypeHandler {

        @Override
        public boolean write(Field field, LinkedHashMap<String, LinkedHashMap<String, String>> config, NBTTagList DataWatcher) throws IllegalAccessException {
            BasicHandlers.write(field, config, DataWatcher);

            System.out.printf("STRING: %1$s: %2$s\n", field.getName(), field.get(null));
            return true;
        }

        @Override
        public boolean read(Field field, LinkedHashMap<String, LinkedHashMap<String, String>> config, NBTTagList DataWatcher) {
            return false;
        }

        @Override
        public Class<?>[] getTypes() {
            return new Class<?>[] {String.class};
        }
    }

    public static class IntegerHandler extends AbstractTypeHandler {

        @Override
        public boolean write(Field field, LinkedHashMap<String, LinkedHashMap<String, String>> config, NBTTagList DataWatcher) throws IllegalAccessException {
            BasicHandlers.write(field, config, DataWatcher);

            System.out.printf("INTEGER: %1$s: %2$d\n", field.getName(), field.get(null));
            return true;
        }

        @Override
        public boolean read(Field field, LinkedHashMap<String, LinkedHashMap<String, String>> config, NBTTagList DataWatcher) {
            return false;
        }

        @Override
        public Class<?>[] getTypes() {
            return new Class<?>[] {Integer.class, Integer.TYPE};
        }
    }

    public static class DoubleHandler extends AbstractTypeHandler {

        @Override
        public boolean write(Field field, LinkedHashMap<String, LinkedHashMap<String, String>> config, NBTTagList DataWatcher) throws IllegalAccessException {
            BasicHandlers.write(field, config, DataWatcher);

            System.out.printf("DOUBLE: %1$s: %2$f\n", field.getName(), field.get(null));
            return false;
        }

        @Override
        public boolean read(Field field, LinkedHashMap<String, LinkedHashMap<String, String>> config, NBTTagList DataWatcher) {
            return false;
        }

        @Override
        public Class<?>[] getTypes() {
            return new Class<?>[] {Double.class, Double.TYPE};
        }
    }
}
