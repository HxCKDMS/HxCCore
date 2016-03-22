package HxCKDMS.HxCCore.api.Configuration.New.Handlers;

import HxCKDMS.HxCCore.api.Configuration.New.AbstractTypeHandler;
import HxCKDMS.HxCCore.api.Configuration.New.Config;
import net.minecraft.nbt.NBTTagCompound;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class AdvancedHandlers {
    public static class ListHandler extends AbstractTypeHandler {

        @Override
        public void write(Field field, LinkedHashMap<String, LinkedHashMap<String, Object>> config, NBTTagCompound DataWatcher) throws IllegalAccessException {
            List<Object> tempList = (List<Object>) field.get(null);

            String categoryName = field.isAnnotationPresent(Config.category.class) ? field.getAnnotation(Config.category.class).value() : "General";
            StringBuilder listTextBuilder = new StringBuilder();

            listTextBuilder.append('[');
            tempList.forEach(item -> listTextBuilder.append('\n').append("\t\t").append(item));
            listTextBuilder.append('\n').append('\t').append(']');

            LinkedHashMap<String, Object> categoryValues = config.getOrDefault(categoryName, new LinkedHashMap<>());
            categoryValues.putIfAbsent(field.getName(), listTextBuilder.toString());
            config.put(categoryName, categoryValues);

            DataWatcher.setString("Type", List.class.getCanonicalName());
            ParameterizedType parameterizedType = (ParameterizedType)field.getGenericType();
            DataWatcher.setString("ListType", parameterizedType.getActualTypeArguments()[0].getClass().getCanonicalName());
        }

        @Override
        public void read(String variable, NBTTagCompound DataWatcher, String currentLine, BufferedReader reader, Class<?> configClass) throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException, IOException {
            System.out.println(variable);
            internalRead(variable, DataWatcher, currentLine, reader, configClass);
        }

        private <T> void internalRead (String variable, NBTTagCompound DataWatcher, String currentLine, BufferedReader reader, Class<?> configClass) throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException, IOException {
            Field field = configClass.getField(variable);
            Class<T> listType = (Class<T>) Class.forName(DataWatcher.getString("ListType"));
            List<T> tempList = new ArrayList<>();

            String line;
            A: while ((line = reader.readLine()) != null && !line.trim().equals("]")) {
                System.out.println(line.trim());
                tempList.add((T)line.trim());
            }
            field.set(configClass, tempList);
        }

        @Override
        public Class<?>[] getTypes() {
            return new Class<?>[] {List.class};
        }
    }
}
