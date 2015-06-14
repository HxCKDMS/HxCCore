package HxCKDMS.HxCCore.api;

import HxCKDMS.HxCCore.HxCCore;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class JsonHelper {
    JsonWriter writer;
    JsonReader reader;

    public void init(String file) throws IOException {
        File HxCFile = new File(HxCCore.HxCCoreDir + file);
        if (!HxCFile.exists()) HxCFile.createNewFile();
        writer = new JsonWriter(new FileWriter(HxCCore.HxCCoreDir + file));
        reader = new JsonReader(new FileReader(HxCCore.HxCCoreDir + file));
    }

    public void setString(String name, String string) {
        try {
            writer.beginObject();
            writer.name(name).value(string);
            writer.endObject();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getString(String name) {
        try {
            reader.beginObject();
            while (reader.hasNext()) {
                String str = reader.nextName();
                if (str.matches(name)) return reader.nextString();
                reader.endObject();
                reader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getString(String name, String Default) {
        try {
            reader.beginObject();
            while (reader.hasNext()) {
                String str = reader.nextName();
                if (str.matches(name)) return reader.nextString();
                reader.endObject();
                reader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        setString(name, Default);
        return Default;
    }

    public void setBoolean(String name, boolean bool) {
        try {
            writer.beginObject();
            writer.name(name).value(bool);
            writer.endObject();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean getBoolean(String name) {
        try {
            reader.beginObject();
            while (reader.hasNext()) {
                String str = reader.nextName();
                if (str.matches(name)) return reader.nextBoolean();
                reader.endObject();
                reader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean getBoolean(String name, boolean Default) {
        try {
            reader.beginObject();
            while (reader.hasNext()) {
                String str = reader.nextName();
                if (str.matches(name)) return reader.nextBoolean();
                reader.endObject();
                reader.close();
            }
        } catch (IOException e) {e.printStackTrace();}
        setBoolean(name, Default);
        return Default;
    }

    public void setInt(String name, int integer) {
        try {
            writer.beginObject();
            writer.name(name).value(integer);
            writer.endObject();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getInt(String name) {
        try {
            reader.beginObject();
            while (reader.hasNext()) {
                String str = reader.nextName();
                if (str.matches(name)) return reader.nextInt();
                reader.endObject();
                reader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getInt(String name, int Default) {
        try {
            reader.beginObject();
            while (reader.hasNext()) {
                String str = reader.nextName();
                if (str.matches(name)) return reader.nextInt();
                reader.endObject();
                reader.close();
            }
        } catch (IOException e) {e.printStackTrace();}
        setInt(name, Default);
        return Default;
    }

    public void setDouble(String name, double dbl) {
        try {
            writer.beginObject();
            writer.name(name).value(dbl);
            writer.endObject();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public double getDouble(String name) {
        try {
            reader.beginObject();
            while (reader.hasNext()) {
                String str = reader.nextName();
                if (str.matches(name)) return reader.nextDouble();
                reader.endObject();
                reader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public double getDouble(String name, double Default) {
        try {
            reader.beginObject();
            while (reader.hasNext()) {
                String str = reader.nextName();
                if (str.matches(name)) return reader.nextDouble();
                reader.endObject();
                reader.close();
            }
        } catch (IOException e) {e.printStackTrace();}
        setDouble(name, Default);
        return Default;
    }

    public void setLong(String name, long lng) {
        try {
            writer.beginObject();
            writer.name(name).value(lng);
            writer.endObject();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public long getLong(String name) {
        try {
            reader.beginObject();
            while (reader.hasNext()) {
                String str = reader.nextName();
                if (str.matches(name)) return reader.nextLong();
                reader.endObject();
                reader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public long getLong(String name, long Default) {
        try {
            reader.beginObject();
            while (reader.hasNext()) {
                String str = reader.nextName();
                if (str.matches(name)) return reader.nextLong();
                reader.endObject();
                reader.close();
            }
        } catch (IOException e) {e.printStackTrace();}
        setLong(name, Default);
        return Default;
    }

    public void setStringList(String name, List<String> list) {
        try {
            writer.beginObject();
            writer.name(name);
            writer.beginArray();
            for (String str : list) writer.value(str);
            writer.endArray();
            writer.endObject();
            writer.close();
        } catch (IOException e) {e.printStackTrace();}
    }

    public List<String> getStringList(String name) {
        List<String> temp = new ArrayList<>();
        try {
            reader.beginObject();
            while (reader.hasNext()) {
                String str = reader.nextName();
                if (str.matches(name)) {
                    reader.beginArray();
                    while (reader.hasNext())
                        temp.add(reader.nextString());
                    reader.endArray();
                }
                reader.endObject();
                reader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return temp;
    }

    public List<String> getStringList(String name, List<String> Default) {
        List<String> temp = new ArrayList<>();
        try {
            reader.beginObject();
            while (reader.hasNext()) {
                String str = reader.nextName();
                if (str.matches(name)) {
                    reader.beginArray();
                    while (reader.hasNext())
                        temp.add(reader.nextString());
                    reader.endArray();
                }
                reader.endObject();
                reader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (temp.isEmpty()) {
            setStringList(name, Default);
            return Default;
        }
        return temp;
    }
}