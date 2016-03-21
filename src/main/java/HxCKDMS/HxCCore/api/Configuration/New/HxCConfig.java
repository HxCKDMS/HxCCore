package HxCKDMS.HxCCore.api.Configuration.New;

import HxCKDMS.HxCCore.api.Configuration.New.Exceptions.InvalidConfigClassException;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.io.*;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class HxCConfig {
    private Class<?> configClass;
    private File configFile;
    //TODO: replace with own file type.
    private NBTTagCompound configDataWatcher;
    private File dataWatcherFile;
    private File configDirectory;
    private File dataWatcherDirectory;

    private LinkedHashMap<String, LinkedHashMap<String, String>> configData = new LinkedHashMap<>();

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

    private void read() {

    }

    private void write() throws IOException {
        if (!configClass.isAnnotationPresent(Config.class)) throw new InvalidConfigClassException(configClass.getCanonicalName(), "Class doesn't have @Config annotation.");
        Arrays.stream(configClass.getDeclaredFields()).filter(field -> !field.isAnnotationPresent(Config.ignore.class)).forEachOrdered(this::handleFieldWriting);
        Arrays.stream(configClass.getDeclaredClasses()).filter(clazz -> !clazz.isAnnotationPresent(Config.ignore.class)).forEachOrdered(this::handleClass);

        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(configFile)));

        StringBuilder stringBuilder = new StringBuilder();

        for (Map.Entry<String, LinkedHashMap<String, String>> entry : configData.entrySet()) {
            stringBuilder.append(entry.getKey()).append(" {\n");

            for (Map.Entry<String, String> entry2 : entry.getValue().entrySet()) {
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
        NBTTagList data = configDataWatcher.getTagList(field.getName(), 0);
        System.out.println(data);

        TypeHandlers.get(field.getType());
    }

    private void handleFieldWriting(Field field) {
        if (TypeHandlers.containsKey(field.getType())) try {
            NBTTagList data = new NBTTagList();
            TypeHandlers.get(field.getType()).write(field, configData, data);
            configDataWatcher.setTag(field.getName(), data);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        else System.out.println(field.getType());
    }
}
