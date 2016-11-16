package hxckdms.hxccore.configs;

import hxckdms.hxcconfig.HxCConfig;
import hxckdms.hxcconfig.handlers.IConfigurationHandler;
import net.minecraft.nbt.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class NBTHandler implements IConfigurationHandler {
    @Override
    public List<String> write(Field field, Object value, ParameterizedType parameterizedType, HxCConfig mainInstance) {
        IConfigurationHandler cHandler = mainInstance.getConfigurationTypeHandler(NBTBase.class);
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
                    LinkedList<String> iLines = new LinkedList<>(cHandler.write(field, tagCompound.getTag(key), null, mainInstance).stream().map(str -> "\t" + str).collect(Collectors.toList()));
                    lines.add("\t" + key + "=" + iLines.removeFirst().trim());
                    lines.addAll(iLines);
                }
                lines.add("]");
                return lines;
            case "LIST":
                NBTTagList tagList = (NBTTagList) base;
                lines = new LinkedList<>();
                lines.add("LIST:[");
                for (int i = 0; i < tagList.tagCount(); i++) lines.addAll(cHandler.write(field, tagList.get(i), null, mainInstance).stream().map(str -> "\t" + str).collect(Collectors.toList()));
                lines.add("]");
                return lines;
            default:
                return Collections.emptyList();
        }
    }

    @Override
    public Object read(String value, HxCConfig mainInstance, Map<String, Object> info) throws IOException {
        IConfigurationHandler cHandler = mainInstance.getConfigurationTypeHandler(NBTBase.class);
        String[] splitLine = value.split(":");
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
                while ((line = mainInstance.getNextLine(true)) != null && !line.trim().startsWith("]")) {
                    if (key == null) key = line.trim().split("=")[0];
                    if (line.contains("=")) {
                        tagCompound.setTag(key, (NBTBase) cHandler.read(line.trim().split("=")[1], mainInstance, null));
                        key = null;
                    }
                }
                return tagCompound;
            case "LIST":
                NBTTagList tagList = new NBTTagList();
                while ((line = mainInstance.getNextLine(true)) != null && !line.trim().startsWith("]")) tagList.appendTag((NBTBase) cHandler.read(line.trim(), mainInstance, null));
                return tagList;
            default:
                return null;
        }
    }

    @Override
    public boolean isTypeAccepted(Class<?> type) {
        return type == NBTBase.class || type == NBTTagString.class || type == NBTTagInt.class || type == NBTTagDouble.class || type == NBTTagFloat.class || type == NBTTagShort.class || type == NBTTagLong.class || type == NBTTagByte.class || type == NBTTagCompound.class || type == NBTTagList.class;
    }
}