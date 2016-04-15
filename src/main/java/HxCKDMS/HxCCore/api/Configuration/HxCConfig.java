package HxCKDMS.HxCCore.api.Configuration;

import HxCKDMS.HxCCore.api.Configuration.Handlers.AdvancedHandlers;
import HxCKDMS.HxCCore.api.Configuration.Exceptions.InvalidConfigClassException;
import HxCKDMS.HxCCore.api.Configuration.Handlers.BasicHandlers;
import HxCKDMS.HxCCore.api.Utils.LogHelper;
import HxCKDMS.HxCCore.api.Utils.StringHelper;
import HxCKDMS.HxCCore.lib.References;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@SuppressWarnings("WeakerAccess")
public class HxCConfig {
    private Class<?> configClass;
    private HashMap<String, HashMap<String, HashMap<String, String>>> configDataWatcherTest = new HashMap<>();
    private File configFile, dataWatcherFile, configDirectory, dataWatcherDirectory;
    private LinkedHashMap<String, LinkedHashMap<String, Object>> configWritingData = new LinkedHashMap<>();
    private static HashMap<Class<?>, AbstractTypeHandler> TypeHandlers = new HashMap<>();
    private HashMap<String, String> CategoryComments = new HashMap<>();
    private HashMap<String, HashMap<String, String>> valueComments = new HashMap<>();

    static {
        //Basic types
        registerTypeHandler(new BasicHandlers.StringHandler());
        registerTypeHandler(new BasicHandlers.IntegerHandler());
        registerTypeHandler(new BasicHandlers.DoubleHandler());
        registerTypeHandler(new BasicHandlers.CharacterHandler());
        registerTypeHandler(new BasicHandlers.FloatHandler());
        registerTypeHandler(new BasicHandlers.LongHandler());
        registerTypeHandler(new BasicHandlers.ShortHandler());
        registerTypeHandler(new BasicHandlers.ByteHandler());
        registerTypeHandler(new BasicHandlers.BooleanHandler());

        //Lists
        registerTypeHandler(new AdvancedHandlers.ListHandler());
        registerTypeHandler(new AdvancedHandlers.ArrayListHandler());
        registerTypeHandler(new AdvancedHandlers.LinkedListHandler());

        //Maps
        registerTypeHandler(new AdvancedHandlers.MapHandler());
        registerTypeHandler(new AdvancedHandlers.HashMapHandler());
        registerTypeHandler(new AdvancedHandlers.LinkedHashMapHandler());
    }

    public static void registerTypeHandler(AbstractTypeHandler typeHandler) {
        Arrays.stream(typeHandler.getTypes()).forEach(clazz -> TypeHandlers.putIfAbsent(clazz, typeHandler));
    }

    public void setCategoryComment(String category, String comment) {
        CategoryComments.put(category, comment);
    }

    public HxCConfig(Class<?> clazz, String configName, File configDirectory, String extension) {
        this.configClass = clazz;
        this.configFile = new File(configDirectory, configName + "." + extension);
        //this.configDataWatcher = new NBTTagCompound();
        this.configDirectory = configDirectory;
        this.dataWatcherDirectory = new File(configDirectory + "/.datawatcher/");
        this.dataWatcherFile = new File(dataWatcherDirectory, configName + ".dat");

        setCategoryComment("Default", "This is the default category.");
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void initConfiguration() {
        configWritingData.clear();

        try {
            configDirectory.mkdirs();
            if(!configFile.exists()) configFile.createNewFile();
            dataWatcherDirectory.mkdirs();
            if(!dataWatcherFile.exists()) dataWatcherFile.createNewFile();

            Path path = dataWatcherDirectory.toPath();
            Files.setAttribute(path, "dos:hidden", true);
            deSerialize();

            read();
            write();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public void deSerialize() {
        try {
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(dataWatcherFile));
            configDataWatcherTest = (HashMap<String, HashMap<String, HashMap<String, String>>>) inputStream.readObject();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void read() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(configFile), "UTF-8"));
        String line;
        String category = "";
        while ((line = reader.readLine()) != null) {
            if (line.trim().startsWith("#")) continue;
            boolean firstIteration = true;
            line = line.replaceFirst("    ", "\t");
            char[] characters = line.toCharArray();
            StringBuilder nameBuilder = new StringBuilder();

            if (line.endsWith("{")) {
                StringBuilder categoryBuilder = new StringBuilder();
                for (int i = 0; i < characters.length; i++) {
                    if (i == 0) continue;
                    if (characters[i] == '{' && characters[i-1] == ' ') break;
                    categoryBuilder.append(characters[i-1]);
                }

                if (categoryBuilder.length() != 0) category = categoryBuilder.toString();
            }

            for (char character : characters) {
                if (firstIteration && character == '\t') {
                    firstIteration = false;
                    continue;
                } else if (firstIteration) break;
                if (character == '\t' || character == '=' || character == ']') break;
                nameBuilder.append(character);
                firstIteration = false;
            }

            if (nameBuilder.length() == 0) continue;
            String variableName = nameBuilder.toString();

            try {
                Class<?> type = Class.forName(configDataWatcherTest.get(category).get(variableName).get("Type"));

                TypeHandlers.get(type).read(variableName, configDataWatcherTest.get(category).get(variableName), line, reader, configClass);
            } catch (ClassNotFoundException | IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
            }
        }

        reader.close();
    }

    private void serialize() {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(dataWatcherFile));
            outputStream.writeObject(configDataWatcherTest);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void write() throws IOException {
        if (!configClass.isAnnotationPresent(Config.class)) throw new InvalidConfigClassException(configClass.getCanonicalName(), "Class doesn't have @Config annotation.");
        Arrays.stream(configClass.getDeclaredFields()).filter(field -> !field.isAnnotationPresent(Config.ignore.class)).forEachOrdered(this::handleFieldWriting);
        Arrays.stream(configClass.getDeclaredClasses()).filter(clazz -> !clazz.isAnnotationPresent(Config.ignore.class)).forEachOrdered(this::handleClass);

        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(configFile), "UTF-8"));

        StringBuilder stringBuilder = new StringBuilder();

        for (Map.Entry<String, LinkedHashMap<String, Object>> entry : configWritingData.entrySet()) {
            stringBuilder.append(StringHelper.repeat('#', 106)).append('\n');
            stringBuilder.append('#').append(' ').append(entry.getKey()).append('\n');
            stringBuilder.append(StringHelper.repeat('#', 106)).append('\n');

            if (CategoryComments.containsKey(entry.getKey())) {
                stringBuilder.append('#').append(' ').append(CategoryComments.get(entry.getKey())).append('\n');
                stringBuilder.append(StringHelper.repeat('#', 106)).append("\n");
            }
            stringBuilder.append('\n');

            stringBuilder.append(entry.getKey()).append(" {\n");

            for (Map.Entry<String, Object> entry2 : entry.getValue().entrySet()) {
                if (valueComments.containsKey(entry.getKey()) && valueComments.get(entry.getKey()).containsKey(entry2.getKey())) stringBuilder.append('\t').append('#').append(' ').append(valueComments.get(entry.getKey()).get(entry2.getKey())).append('\n');
                stringBuilder.append('\t').append(entry2.getKey()).append('=').append(entry2.getValue()).append("\n\n");
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            stringBuilder.append("}\n\n");
        }

        writer.write(stringBuilder.toString().trim());
        writer.close();

        serialize();
    }

    private void handleClass(Class clazz) {

    }

    private void handleFieldWriting(Field field) {
        if (TypeHandlers.containsKey(field.getType())) try {
            HashMap<String, String> data = new HashMap<>();
            String categoryName = field.isAnnotationPresent(Config.category.class) ? field.getAnnotation(Config.category.class).value() : "General";
            HashMap<String, HashMap<String, String>> category = configDataWatcherTest.getOrDefault(categoryName, new HashMap<>());

            TypeHandlers.get(field.getType()).write(field, configWritingData, data);

            category.put(field.getName(), data);
            configDataWatcherTest.put(categoryName, category);

            HashMap<String, String> comment = valueComments.getOrDefault(categoryName, new HashMap<>());
            if(field.isAnnotationPresent(Config.comment.class)) comment.put(field.getName(), field.getAnnotation(Config.comment.class).value());
            valueComments.put(categoryName, comment);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        else LogHelper.fatal(String.format("Configuration type: %1$s is unsupported!", field.getType().getCanonicalName()), References.MOD_NAME);
    }
}
