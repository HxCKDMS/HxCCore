package hxckdms.hxccore.configs;

import hxckdms.hxcconfig.Config;
import hxckdms.hxcconfig.HxCConfig;
import hxckdms.hxcconfig.handlers.ICollectionsHandler;
import hxckdms.hxcconfig.handlers.ITypeHandler;
import net.minecraft.nbt.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.stream.Collectors;

import static hxckdms.hxcconfig.Flags.OVERWRITE;

public class NBTHandler  implements ITypeHandler, ICollectionsHandler {

    @SuppressWarnings("ConstantConditions")
    @Override
    public void write(Field field, LinkedHashMap<String, LinkedHashMap<String, Object>> config, HxCConfig HxCConfigClass) throws IllegalAccessException {
        ICollectionsHandler cHandler = HxCConfigClass.getCollectionsHandler(NBTBase.class);
        String categoryName = field.isAnnotationPresent(Config.category.class) ? field.getAnnotation(Config.category.class).value() : "General";
        LinkedHashMap<String, Object> categoryValues = config.getOrDefault(categoryName, new LinkedHashMap<>());

        NBTBase base = (NBTBase) field.get(null);

        switch (NBTBase.NBT_TYPES[base.getId()]) {
            case "STRING":
                categoryValues.putIfAbsent(field.getName(), "STR:" + ((NBTTagString) base).getString());
            case "INT":
                categoryValues.putIfAbsent(field.getName(), "I:" + ((NBTTagInt) base).getInt());
            case "DOUBLE":
                categoryValues.putIfAbsent(field.getName(), "D:" + ((NBTTagDouble) base).getDouble());
            case "FLOAT":
                categoryValues.putIfAbsent(field.getName(), "F:" + ((NBTTagFloat) base).getFloat());
            case "SHORT":
                categoryValues.putIfAbsent(field.getName(), "S:" + ((NBTTagShort) base).getShort());
            case "LONG":
                categoryValues.putIfAbsent(field.getName(), "L:" + ((NBTTagLong) base).getLong());
            case "BYTE":
                categoryValues.putIfAbsent(field.getName(), "B:" + ((NBTTagByte) base).getByte());
            case "COMPOUND":
                NBTTagCompound tagCompound = (NBTTagCompound) base;
                StringBuilder tagCompoundBuilder = new StringBuilder();
                tagCompoundBuilder.append('[');
                for (String key : tagCompound.getKeySet()) tagCompoundBuilder.append('\n').append("\t\t").append(key).append('=').append(cHandler.writeInCollection(field, tagCompound.getTag(key), null, null, HxCConfigClass).stream().reduce((a, b) -> a + "\n\t\t" + b).orElse(""));
                tagCompoundBuilder.append('\n').append('\t').append(']');
                categoryValues.putIfAbsent(field.getName(), "COMPOUND:" + tagCompoundBuilder.toString());
                break;
            case "LIST":
                NBTTagList tagList = (NBTTagList) base;
                StringBuilder tagListBuilder = new StringBuilder();
                tagListBuilder.append('[');
                for (int i = 0; i < tagList.tagCount(); i++) tagListBuilder.append('\n').append(cHandler.writeInCollection(field, tagList.get(i), null, null, HxCConfigClass).stream().map(str -> "\t\t" + str).reduce((a, b) -> a + "\n" + b).orElse(""));
                tagListBuilder.append('\n').append('\t').append(']');
                categoryValues.putIfAbsent(field.getName(), "LIST:" + tagListBuilder.toString());
                break;
        }
        config.put(categoryName, categoryValues);
    }

    @Override
    public void read(String variable, String currentLine, BufferedReader reader, Class<?> configClass, HxCConfig HxCConfigClass) throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException, IOException {
        ICollectionsHandler cHandler = HxCConfigClass.getCollectionsHandler(NBTBase.class);
        String[] splitLine = currentLine.trim().replace(variable, "").replace("=", "").split(":");
        Field field = HxCConfig.getField(configClass, variable);
        String rest = "";
        NBTBase value;
        for (int i = 1; i < splitLine.length; i++) rest += splitLine[i];

        switch (splitLine[0]) {
            case "STR":
                value = new NBTTagString(rest);
                break;
            case "I":
                value = new NBTTagInt(Integer.parseInt(rest));
                break;
            case "D":
                value = new NBTTagDouble(Double.parseDouble(rest));
                break;
            case "F":
                value = new NBTTagFloat(Float.parseFloat(rest));
                break;
            case "S":
                value = new NBTTagShort(Short.parseShort(rest));
                break;
            case "L":
                value = new NBTTagLong(Long.parseLong(rest));
                break;
            case "B":
                value = new NBTTagByte(Byte.parseByte(rest));
                break;
            case "COMPOUND":
                value = new NBTTagCompound();
                String key = null;
                String line;
                while ((line = reader.readLine()) != null && !line.trim().equals("]")) {
                    if (key == null) {
                        key = line.split("=")[0].trim();
                    }

                    if (line.contains("=")) {
                        ((NBTTagCompound) value).setTag(key, (NBTBase) cHandler.readFromCollection(null, line.trim().split("=")[1], reader, null, HxCConfigClass));
                        key = null;
                    }
                }
                break;
            case "LIST":
                value = new NBTTagList();
                while ((line = reader.readLine()) != null && !line.trim().equals("]")) ((NBTTagList) value).appendTag((NBTBase) cHandler.readFromCollection(null, line.trim(), reader, null, HxCConfigClass));
                break;
            default:
                value = null;
                break;
        }

        if (field.isAnnotationPresent(Config.flags.class) && (field.getAnnotation(Config.flags.class).value() & OVERWRITE) == OVERWRITE) {
            if ("".equals(field.get(null)) || field.get(null) == null) {
                field.set(configClass, value);
            }
        } else field.set(configClass, value);
    }

    @Override
    public List<String> writeInCollection(Field field, Object value, HashMap<String, Object> subDataWatcher, ParameterizedType parameterizedType, HxCConfig HxCConfigClass) {
        ICollectionsHandler cHandler = HxCConfigClass.getCollectionsHandler(NBTBase.class);
        NBTBase base = (NBTBase) value;

        switch (NBTBase.NBT_TYPES[base.getId()]) {
            case "STRING":
                return Collections.singletonList("STR:" + ((NBTTagString) base).getString());
            case "INT":
                return Collections.singletonList("I:" + ((NBTTagInt) base).getInt());
            case "DOUBLE":
                return Collections.singletonList("D:" + ((NBTTagDouble) base).getDouble());
            case "FLOAT":
                return Collections.singletonList("F:" + ((NBTTagFloat) base).getFloat());
            case "SHORT":
                return Collections.singletonList("S:" + ((NBTTagShort) base).getShort());
            case "LONG":
                return Collections.singletonList("L:" + ((NBTTagLong) base).getLong());
            case "BYTE":
                return Collections.singletonList("B:" + ((NBTTagByte) base).getByte());
            case "COMPOUND":
                NBTTagCompound tagCompound = (NBTTagCompound) base;
                LinkedList<String> lines = new LinkedList<>();
                lines.add("COMPOUND:[");
                for (String key : tagCompound.getKeySet()) {
                    LinkedList<String> iLines = new LinkedList<>(cHandler.writeInCollection(field, tagCompound.getTag(key), null, null, HxCConfigClass).stream().map(str -> "\t" + str).collect(Collectors.toList()));
                    lines.add("\t" + key + "=" + iLines.removeFirst().trim());
                    lines.addAll(iLines);
                }
                lines.add("]");
                return lines;
            case "LIST":
                NBTTagList tagList = (NBTTagList) base;
                lines = new LinkedList<>();
                lines.add("LIST:[");
                for (int i = 0; i < tagList.tagCount(); i++) lines.addAll(cHandler.writeInCollection(field, tagList.get(i), null, null, HxCConfigClass).stream().map(str -> "\t" + str).collect(Collectors.toList()));
                lines.add("]");
                return lines;
            default:
                return Collections.emptyList();
        }
    }

    @Override
    public NBTBase readFromCollection(HashMap<String, Object> subDataWatcher, String currentLine, BufferedReader reader, Map<String, Object> info, HxCConfig HxCConfigClass) throws IOException {
        ICollectionsHandler cHandler = HxCConfigClass.getCollectionsHandler(NBTBase.class);
        String[] splitLine = currentLine.split(":");
        String rest = "";
        for (int i = 1; i < splitLine.length; i++) rest += splitLine[i];

        switch (splitLine[0]) {
            case "STR":
                return new NBTTagString(rest);
            case "I":
                return new NBTTagInt(Integer.parseInt(rest));
            case "D":
                return new NBTTagDouble(Double.parseDouble(rest));
            case "F":
                return new NBTTagFloat(Float.parseFloat(rest));
            case "S":
                return new NBTTagShort(Short.parseShort(rest));
            case "L":
                return new NBTTagLong(Long.parseLong(rest));
            case "B":
                return new NBTTagByte(Byte.parseByte(rest));
            case "COMPOUND":
                NBTTagCompound tagCompound = new NBTTagCompound();
                String line;
                String key = null;
                while ((line = reader.readLine()) != null && !line.trim().startsWith("]")) {
                    if (key == null) key = line.trim().split("=")[0];
                    if (line.contains("=")) {
                        tagCompound.setTag(key, (NBTBase) cHandler.readFromCollection(null, line.trim().split("=")[1], reader, null, HxCConfigClass));
                        key = null;
                    }
                }
                return tagCompound;
            case "LIST":
                NBTTagList tagList = new NBTTagList();
                while ((line = reader.readLine()) != null && !line.trim().startsWith("]")) tagList.appendTag((NBTBase) cHandler.readFromCollection(null, line.trim(), reader, null, HxCConfigClass));
                return tagList;
            default:
                return null;
        }
    }

    @Override
    public Class<?>[] getTypes() {
        return new Class<?>[]{NBTBase.class, NBTTagString.class, NBTTagInt.class, NBTTagDouble.class, NBTTagFloat.class, NBTTagShort.class, NBTTagLong.class, NBTTagByte.class, NBTTagCompound.class, NBTTagList.class};
    }
}