package HxCKDMS.HxCCore.api.Configuration.New;

import HxCKDMS.HxCCore.api.Configuration.New.Exceptions.InvalidConfigClassException;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;

import java.io.*;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class HxCConfig {
    private Class<?> configClass;
    //TODO: replace with own file type.
    private NBTTagCompound configDataWatcher;
    private File configFile, dataWatcherFile, configDirectory, dataWatcherDirectory;
    private LinkedHashMap<String, LinkedHashMap<String, Object>> configWritingData = new LinkedHashMap<>(), configReadingData = new LinkedHashMap<>(), configData = new LinkedHashMap<>();
    private static HashMap<Class<?>, AbstractTypeHandler> TypeHandlers = new HashMap<>();

    public static boolean registerTypeHandler(AbstractTypeHandler typeHandler) {
        Arrays.stream(typeHandler.getTypes()).forEach(clazz -> TypeHandlers.putIfAbsent(clazz, typeHandler));
        return true;
    }

    public HxCConfig(Class<?> clazz, String configName, File configDirectory, String extension) {
        this.configClass = clazz;
        this.configFile = new File(configDirectory, configName + "." + extension);
        this.configDataWatcher = new NBTTagCompound();
        this.configDirectory = configDirectory;
        this.dataWatcherDirectory = new File(configDirectory + "/datawatcher/");
        this.dataWatcherFile = new File(dataWatcherDirectory, configName + ".dat");

        registerTypeHandler(new BasicHandlers.StringHandler());
        registerTypeHandler(new BasicHandlers.IntegerHandler());
        registerTypeHandler(new BasicHandlers.DoubleHandler());
    }

    public boolean initConfiguration() throws IOException {
        System.out.println(TypeHandlers);

        configDirectory.mkdirs();
        if(!configFile.exists()) configFile.createNewFile();
        dataWatcherDirectory.mkdirs();
        if(!dataWatcherFile.exists()) dataWatcherFile.createNewFile();

        System.out.println(configFile.getCanonicalPath());


        read();
        write();
        return true;
    }

    private void read() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(configFile), "UTF-8"));
        reader.reset();
        String line;
        String category = "";
        while ((line = reader.readLine()) != null) {
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
                if (character == '\t' || character == '=') break;
                nameBuilder.append(character);
                firstIteration = false;
            }

            if (nameBuilder.length() == 0) continue;
            String variableName = nameBuilder.toString();
            System.out.printf("%1$s: %2$s\n", category, variableName);
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
            stringBuilder.append(entry.getKey()).append(" {\n");

            for (Map.Entry<String, Object> entry2 : entry.getValue().entrySet()) {
                stringBuilder.append("\t").append(entry2.getKey()).append('=').append(entry2.getValue()).append('\n');
            }
            stringBuilder.append("}\n\n");
        }

        writer.write(stringBuilder.toString().trim());
        writer.close();

        CompressedStreamTools.write(configDataWatcher, dataWatcherFile);
    }

    private void handleClass(Class clazz) {

    }

    private void handleFieldReading(Field field) {

    }

    private void handleFieldWriting(Field field) {
        if (TypeHandlers.containsKey(field.getType())) try {
            NBTTagCompound data = new NBTTagCompound();
            String categoryName = field.isAnnotationPresent(Config.category.class) ? field.getAnnotation(Config.category.class).value() : "Default";
            NBTTagCompound category = configDataWatcher.getCompoundTag(categoryName);

            TypeHandlers.get(field.getType()).write(field, configWritingData, data);

            category.setTag(field.getName(), data);
            configDataWatcher.setTag(categoryName, category);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        else System.out.println(field.getType());
    }
}
