package hxckdms.hxccore.configs;

import hxckdms.hxcconfig.HxCConfig;
import hxckdms.hxcconfig.handlers.IConfigurationHandler;
import net.minecraft.nbt.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.stream.Collectors;

public class NBTHandler  implements IConfigurationHandler {
    @Override
    public List<String> write(Field field, Object value, ParameterizedType parameterizedType, HxCConfig mainInstance) {
        IConfigurationHandler cHandler = mainInstance.getConfigurationTypeHandler(NBTBase.class);
        NBTBase base = (NBTBase) value;

        switch (NBTBase.NBTTypes[base.getId()]) {
            case "STRING":
                return Collections.singletonList("STR:" + ((NBTTagString) base).func_150285_a_());
            case "INT":
                return Collections.singletonList("I:" + ((NBTTagInt) base).func_150287_d());
            case "DOUBLE":
                return Collections.singletonList("D:" + ((NBTTagDouble) base).func_150286_g());
            case "FLOAT":
                return Collections.singletonList("F:" + ((NBTTagFloat) base).func_150288_h());
            case "SHORT":
                return Collections.singletonList("S:" + ((NBTTagShort) base).func_150289_e());
            case "LONG":
                return Collections.singletonList("L:" + ((NBTTagLong) base).func_150291_c());
            case "BYTE":
                return Collections.singletonList("B:" + ((NBTTagByte) base).func_150290_f());
            case "COMPOUND":
                NBTTagCompound tagCompound = (NBTTagCompound) base;
                LinkedList<String> lines = new LinkedList<>();
                lines.add("COMPOUND:[");
                for (String key : (Set<String>) tagCompound.func_150296_c()) {
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
                for (int i = 0; i < tagList.tagCount(); i++) lines.addAll(cHandler.write(field, tagList.tagList.get(i), null, mainInstance).stream().map(str -> "\t" + str).collect(Collectors.toList()));
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
    public Class<?>[] getTypes() {
        return new Class<?>[]{NBTBase.class, NBTTagString.class, NBTTagInt.class, NBTTagDouble.class, NBTTagFloat.class, NBTTagShort.class, NBTTagLong.class, NBTTagByte.class, NBTTagCompound.class, NBTTagList.class};
    }
}