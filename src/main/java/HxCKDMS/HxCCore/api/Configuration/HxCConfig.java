package HxCKDMS.HxCCore.api.Configuration;

import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * {} categories
 * # comments
 * <> lists
 * [] maps
 */
public class HxCConfig {
    private HashMap<String, Category> categories = new HashMap<>();

    public void handleConfig(Class<?> clazz, File file) {
        //parseFile(file);

        for(Field field : clazz.getDeclaredFields()) {
            for(Annotation annotation : field.getAnnotations()) {
                try {
                    if (annotation.annotationType() == Config.String.class) {
                        handleString(clazz, field, (Config.String) annotation);
                    } else if (annotation.annotationType() == Config.Integer.class) {
                        handleInteger(clazz, field, (Config.Integer) annotation);
                    } else if (annotation.annotationType() == Config.Boolean.class) {
                        handleBoolean(clazz, field, (Config.Boolean) annotation);
                    } else if (annotation.annotationType() == Config.List.class) {
                        handleList(clazz, field, (Config.List) annotation);
                    } else if (annotation.annotationType() == Config.Map.class) {
                        handleMap(clazz, field, (Config.Map) annotation);
                    } else if (annotation.annotationType() == Config.Long.class) {
                        handleLong(clazz, field, (Config.Long) annotation);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        for(Category category : categories.values()) {
            category.write(stringBuilder);
        }
        String fileString = stringBuilder.toString();

        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
            writer.append(fileString);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleString(Class<?> clazz, Field field, Config.String annotation) throws IllegalAccessException {
        if(!categories.keySet().contains(annotation.category())) categories.put(annotation.category(), new Category(annotation.category(), null));
        Setting<String> setting = new Setting<>(annotation.description(), (String) field.get(clazz), Setting.Type.STRING, field.getName());
        categories.put(annotation.category(), categories.get(annotation.category()).addSetting(setting));
    }

    private void handleInteger(Class<?> clazz, Field field, Config.Integer annotation) throws IllegalAccessException {
        if(!categories.keySet().contains(annotation.category())) categories.put(annotation.category(), new Category(annotation.category(), null));
        Setting<Integer> setting = new Setting<>(annotation.description(), (Integer) field.get(clazz), Setting.Type.INTEGER, field.getName(), annotation.minValue(), annotation.maxValue());
        categories.put(annotation.category(), categories.get(annotation.category()).addSetting(setting));
    }

    private void handleBoolean(Class<?> clazz, Field field, Config.Boolean annotation) throws IllegalAccessException {
        if(!categories.keySet().contains(annotation.category())) categories.put(annotation.category(), new Category(annotation.category(), null));
        Setting<Boolean> setting = new Setting<>(annotation.description(), (Boolean) field.get(clazz), Setting.Type.BOOLEAN, field.getName());
        categories.put(annotation.category(), categories.get(annotation.category()).addSetting(setting));
    }

    private void handleList(Class<?> clazz, Field field, Config.List annotation) throws IllegalAccessException {
        if(!categories.keySet().contains(annotation.category())) categories.put(annotation.category(), new Category(annotation.category(), null));
        Setting<List> setting = new Setting<>(annotation.description(), (List) field.get(clazz), Setting.Type.LIST, field.getName());
        categories.put(annotation.category(), categories.get(annotation.category()).addSetting(setting));
    }

    private void handleMap(Class<?> clazz, Field field, Config.Map annotation) throws IllegalAccessException {
        if(!categories.keySet().contains(annotation.category())) categories.put(annotation.category(), new Category(annotation.category(), null));
        Setting<Map> setting = new Setting<>(annotation.description(), (Map) field.get(clazz), Setting.Type.MAP, field.getName());
        categories.put(annotation.category(), categories.get(annotation.category()).addSetting(setting));
    }

    private void handleLong(Class<?> clazz, Field field, Config.Long annotation) throws IllegalAccessException {
        if(!categories.keySet().contains(annotation.category())) categories.put(annotation.category(), new Category(annotation.category(), null));
        Setting<Long> setting = new Setting<>(annotation.description(), (Long) field.get(clazz), Setting.Type.LONG, field.getName());
        categories.put(annotation.category(), categories.get(annotation.category()).addSetting(setting));
    }

    private void parseFile(File file) {

        try {
            if(!file.exists()) file.createNewFile();

            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));

            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while((line = reader.readLine()) != null) stringBuilder.append(line);

            System.out.println(stringBuilder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void registerCategory(Category category) {
        if(!categories.keySet().contains(category.getName())) categories.put(category.getName(), category);
    }
}
