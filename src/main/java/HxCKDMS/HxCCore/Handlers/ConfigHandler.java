package HxCKDMS.HxCCore.Handlers;

import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.lang.reflect.Field;
import java.util.HashMap;

@SuppressWarnings({"unused"})
public class ConfigHandler {
    private File configFile;

    public ConfigHandler(File configFile) {
        this.configFile = configFile;
        
        if(!configFile.exists())
            try {
                configFile.createNewFile();
            } catch (IOException ignored) {}

    }

    /**
     * *
     * @param key: the key that's used to find the value in the config file.
     * @param value: the value that's being stored, currently supported value types: String.
     * param valueClass: the class of the value being stored.
     * @param comments: the comments added to the config value.
     */
    public <T> void setValue(final String key, T value/*, Class<T> valueClass*/, String... comments){
        value.getClass();
        DataSet dataset = new DataSet();
        
        if(value.getClass() == String.class){
            dataset.value = (String) value;
            dataset.test = new HashMap<Integer, String>();
            dataset.test.put(5, "test");
            dataset.test.put(10, "test2");
            GsonBuilder builder = new GsonBuilder();
            builder.setPrettyPrinting().serializeNulls();
            builder.setFieldNamingStrategy(new FieldNamingStrategy() {
                @Override
                public String translateName(Field f) {
                    if(f.getName().equals("value")) return key;
                    return f.getName();
                }
            });
            
            Gson gson  = builder.create();
            
            String json = gson.toJson(dataset);
            System.out.println(json);
            
            try{
                BufferedWriter writer = new BufferedWriter(new FileWriter(configFile));
                writer.write(json + "\n");
                writer.close();
            }catch(IOException e){
                e.printStackTrace();
                System.out.println("Failed to add JSON config file system to file.");
            }
        }
    }
    
    public <T> T getValue(String key, Class<T> valueClass){
        return (T) "";
    }
    
    public class DataSet {
        public String value;
        public HashMap<Integer, String> test;
    }
}
