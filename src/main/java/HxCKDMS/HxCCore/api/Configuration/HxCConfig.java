package HxCKDMS.HxCCore.api.Configuration;

import HxCKDMS.HxCCore.api.Configuration.Exceptions.InvalidConfigClassException;
import HxCKDMS.HxCCore.api.Configuration.Handlers.*;
import HxCKDMS.HxCCore.api.Utils.LogHelper;
import HxCKDMS.HxCCore.api.Utils.StringHelper;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static HxCKDMS.HxCCore.api.Configuration.Flags.COLLECTION_HANDLER;
import static HxCKDMS.HxCCore.api.Configuration.Flags.TYPE_HANDLER;


@SuppressWarnings({"WeakerAccess", "unused"})
public class HxCConfig {
    private Class<?> configClass;
    private File configFile, configDirectory;
    private LinkedHashMap<String, LinkedHashMap<String, Object>> configWritingData = new LinkedHashMap<>();
    private static HashMap<Class<?>, ITypeHandler> typeHandlers = new HashMap<>();
    private static HashMap<Class<?>, ICollectionsHandler> collectionsHandlers = new HashMap<>();
    private HashMap<String, String> categoryComments = new HashMap<>();
    private HashMap<String, HashMap<String, String>> valueComments = new HashMap<>();
    private String app_name;

    static {
        //Basic types
        registerHandler(new PrimaryHandlers.StringHandler(), TYPE_HANDLER | COLLECTION_HANDLER);
        registerHandler(new PrimaryHandlers.IntegerHandler(), TYPE_HANDLER | COLLECTION_HANDLER);
        registerHandler(new PrimaryHandlers.DoubleHandler(), TYPE_HANDLER | COLLECTION_HANDLER);
        registerHandler(new PrimaryHandlers.CharacterHandler(), TYPE_HANDLER | COLLECTION_HANDLER);
        registerHandler(new PrimaryHandlers.FloatHandler(), TYPE_HANDLER | COLLECTION_HANDLER);
        registerHandler(new PrimaryHandlers.LongHandler(), TYPE_HANDLER | COLLECTION_HANDLER);
        registerHandler(new PrimaryHandlers.ShortHandler(), TYPE_HANDLER | COLLECTION_HANDLER);
        registerHandler(new PrimaryHandlers.ByteHandler(), TYPE_HANDLER | COLLECTION_HANDLER);
        registerHandler(new PrimaryHandlers.BooleanHandler(), TYPE_HANDLER | COLLECTION_HANDLER);

        //Lists
        registerHandler(new CollectionsHandlers.ListHandler(), TYPE_HANDLER | COLLECTION_HANDLER);
        registerHandler(new CollectionsHandlers.ArrayListHandler(), TYPE_HANDLER | COLLECTION_HANDLER);
        registerHandler(new CollectionsHandlers.LinkedListHandler(), TYPE_HANDLER | COLLECTION_HANDLER);

        //Maps
        registerHandler(new CollectionsHandlers.MapHandler(), TYPE_HANDLER | COLLECTION_HANDLER);
        registerHandler(new CollectionsHandlers.HashMapHandler(), TYPE_HANDLER | COLLECTION_HANDLER);
        registerHandler(new CollectionsHandlers.LinkedHashMapHandler(), TYPE_HANDLER | COLLECTION_HANDLER);

        //Special
        registerHandler(new SpecialHandlers.SpecialClassHandler(), TYPE_HANDLER | COLLECTION_HANDLER);
    }

    @Deprecated
    public static void registerTypeHandler(ITypeHandler handler) {
        registerHandler(handler, TYPE_HANDLER);
    }

    public static void registerHandler(Object handler, int flag) {
        if ((flag & TYPE_HANDLER) == TYPE_HANDLER) Arrays.stream(((ITypeHandler)handler).getTypes()).forEach(clazz -> typeHandlers.putIfAbsent(clazz, (ITypeHandler) handler));
        if ((flag & COLLECTION_HANDLER) == COLLECTION_HANDLER) Arrays.stream(((ICollectionsHandler)handler).getTypes()).forEach(clazz -> collectionsHandlers.putIfAbsent(clazz, (ICollectionsHandler) handler));
    }

    public static ICollectionsHandler getCollectionsHandler(Class<?> type) {
        if (collectionsHandlers.containsKey(type)) return collectionsHandlers.get(type);
        else throw new NullPointerException(String.format("No collections handler for type: %s exists.", type.getCanonicalName()));
    }

    public void setCategoryComment(String category, String comment) {
        categoryComments.put(category, comment);
    }

    public HxCConfig(Class<?> clazz, String configName, File configDirectory, String extension, String app_name) {
        this.configClass = clazz;
        this.configFile = new File(configDirectory, configName + "." + extension);
        this.configDirectory = configDirectory;
        this.app_name = app_name;

        setCategoryComment("Default", "This is the default category.");
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public final void initConfiguration() {
        configWritingData.clear();

        try {
            configDirectory.mkdirs();
            if (!configFile.exists()) configFile.createNewFile();

            //deSerialize();
            read();
            //configDataWatcherTest.clear();
            write();
            //serialize();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void read() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(configFile), "UTF-8"));
        String line;
        //String category = "";
        while ((line = reader.readLine()) != null) {
            if (line.trim().startsWith("#")) continue;
            boolean firstIteration = true;
            line = line.replaceFirst("    ", "\t");
            char[] characters = line.toCharArray();
            StringBuilder nameBuilder = new StringBuilder();

            /*if (line.endsWith("{")) {
                StringBuilder categoryBuilder = new StringBuilder();
                for (int i = 0; i < characters.length; i++) {
                    if (i == 0) continue;
                    if (characters[i] == '{' && characters[i - 1] == ' ') break;
                    categoryBuilder.append(characters[i - 1]);
                }

                if (categoryBuilder.length() != 0) category = categoryBuilder.toString();
            }*/

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
                Class<?> type = HxCConfig.getField(configClass, variableName).getType();

                typeHandlers.get(type).read(variableName, line, reader, configClass);
            } catch (IllegalArgumentException | ClassNotFoundException | IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
            }
        }

        reader.close();
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

            if (categoryComments.containsKey(entry.getKey())) {
                stringBuilder.append('#').append(' ').append(categoryComments.get(entry.getKey())).append('\n');
                stringBuilder.append(StringHelper.repeat('#', 106)).append("\n");
            }
            stringBuilder.append('\n');

            stringBuilder.append(entry.getKey()).append(" {\n");

            boolean first = true;
            for (Map.Entry<String, Object> entry2 : entry.getValue().entrySet()) {
                if (!first && hasComment(entry.getKey(), entry2.getKey())) stringBuilder.append('\n');

                if (hasComment(entry.getKey(), entry2.getKey())) stringBuilder.append('\t').append('#').append(' ').append(getComment(entry.getKey(), entry2.getKey())).append('\n');
                stringBuilder.append('\t').append(entry2.getKey()).append('=').append(entry2.getValue()).append('\n');
                first = false;
            }
            stringBuilder.append("}\n\n");
        }

        writer.write(stringBuilder.toString().trim());
        writer.close();
    }

    private void handleClass(Class clazz) {

    }

    private void handleFieldWriting(Field field) {
        if (typeHandlers.containsKey(field.getType())) try {

            setPublicStatic(field);
            if (!Modifier.isPublic(field.getModifiers())) return;

            String categoryName = field.isAnnotationPresent(Config.category.class) ? field.getAnnotation(Config.category.class).value() : "General";
            typeHandlers.get(field.getType()).write(field, configWritingData);

            HashMap<String, String> comment = valueComments.getOrDefault(categoryName, new HashMap<>());
            if(field.isAnnotationPresent(Config.comment.class)) comment.put(field.getName(), field.getAnnotation(Config.comment.class).value());
            valueComments.put(categoryName, comment);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        else LogHelper.error(String.format("Configuration type: %1$s is unsupported!", field.getType().getCanonicalName()), app_name);
    }

    //helper methods

    private boolean hasComment(String category, String variable) {
        return valueComments.containsKey(category) && valueComments.get(category).containsKey(variable);
    }

    private String getComment(String category, String variable) {
        return valueComments.get(category).get(variable);
    }

    public static Field getField(Class<?> clazz, String variable) throws NoSuchFieldException {
        Field field = clazz.getDeclaredField(variable);
        setPublicStatic(field);
        return field;
    }

    private static void setPublicStatic(Field field) {
        if (field.isAnnotationPresent(Config.force.class)) try {
            Field modField = Field.class.getDeclaredField("modifiers");
            modField.setAccessible(true);
            modField.setInt(field, field.getModifiers() & ~Modifier.PRIVATE);
            modField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
            modField.setInt(field, field.getModifiers() | Modifier.PUBLIC);
            modField.setInt(field, field.getModifiers() | Modifier.STATIC);
        } catch (NoSuchFieldException | IllegalAccessException ignored) {}
    }
}