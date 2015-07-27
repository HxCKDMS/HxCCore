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
        for(Field field : clazz.getDeclaredFields()) {
            for(Annotation annotation : field.getAnnotations()) {
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
            }
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"))) {
            String line;
            while((line = reader.readLine()) != null) {
                if(line.contains("{")) {
                    char[] chars = line.toCharArray();
                    boolean hasNotEncounteredText = true;
                    StringBuilder stringBuilder = new StringBuilder();
                    for(Character character : chars) {
                        if(!character.equals(' ') && !character.equals('\t') || hasNotEncounteredText) {
                            hasNotEncounteredText = false;
                            stringBuilder.append(character);
                        } else if(character.equals(' ')) break;
                    }
                    categories.get(stringBuilder.toString()).read(clazz, reader);
                }
            }
        } catch (IOException | IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }

        StringBuilder stringBuilder = new StringBuilder();
        for(Category category : categories.values()) {
            category.write(stringBuilder);
        }
        String fileString = stringBuilder.toString();

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"))) {
            writer.append(fileString);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleString(Class<?> clazz, Field field, Config.String annotation) {
        if(!categories.keySet().contains(annotation.category())) categories.put(annotation.category(), new Category(annotation.category(), null));
        Setting<String> setting = new Setting<>(clazz, annotation.description(), field, Setting.Type.STRING, field.getName(), annotation.validValues());
        categories.put(annotation.category(), categories.get(annotation.category()).addSetting(setting));
    }

    private void handleInteger(Class<?> clazz, Field field, Config.Integer annotation) {
        if(!categories.keySet().contains(annotation.category())) categories.put(annotation.category(), new Category(annotation.category(), null));
        Setting<Integer> setting = new Setting<>(clazz, annotation.description(), field, Setting.Type.INTEGER, field.getName(), annotation.minValue(), annotation.maxValue());
        categories.put(annotation.category(), categories.get(annotation.category()).addSetting(setting));
    }

    private void handleBoolean(Class<?> clazz, Field field, Config.Boolean annotation) {
        if(!categories.keySet().contains(annotation.category())) categories.put(annotation.category(), new Category(annotation.category(), null));
        Setting<Boolean> setting = new Setting<>(clazz, annotation.description(), field, Setting.Type.BOOLEAN, field.getName());
        categories.put(annotation.category(), categories.get(annotation.category()).addSetting(setting));
    }

    private void handleList(Class<?> clazz, Field field, Config.List annotation) {
        if(!categories.keySet().contains(annotation.category())) categories.put(annotation.category(), new Category(annotation.category(), null));
        Setting<List> setting = new Setting<>(clazz, annotation.description(), field, Setting.Type.LIST, field.getName());
        categories.put(annotation.category(), categories.get(annotation.category()).addSetting(setting));
    }

    private void handleMap(Class<?> clazz, Field field, Config.Map annotation) {
        if(!categories.keySet().contains(annotation.category())) categories.put(annotation.category(), new Category(annotation.category(), null));
        Setting<Map> setting = new Setting<>(clazz, annotation.description(), field, Setting.Type.MAP, field.getName());
        categories.put(annotation.category(), categories.get(annotation.category()).addSetting(setting));
    }

    private void handleLong(Class<?> clazz, Field field, Config.Long annotation) {
        if(!categories.keySet().contains(annotation.category())) categories.put(annotation.category(), new Category(annotation.category(), null));
        Setting<Long> setting = new Setting<>(clazz, annotation.description(), field, Setting.Type.LONG, field.getName());
        categories.put(annotation.category(), categories.get(annotation.category()).addSetting(setting));
    }

    public void registerCategory(Category category) {
        if(!categories.keySet().contains(category.getName())) categories.put(category.getName(), category);
    }
}
