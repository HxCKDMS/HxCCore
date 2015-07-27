package HxCKDMS.HxCCore.api.Configuration;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

public class Category {
    private String name;
    private List<Setting> settings = new ArrayList<>();
    private String comment;

    public Category(String name, String comment) {
        this.name = name;
        this.comment = comment;
    }

    public Category addSetting(Setting setting) {
        settings.add(setting);
        return this;
    }

    public String getName() {
        return name;
    }

    public void read(Class<?> clazz, BufferedReader reader) throws IOException {
        String line;
        while((line = reader.readLine()) != null) {
            try {

                if (line.equals("}")) return;
                if (line.contains("#")) continue;

                if (line.contains(":")) {
                    if (line.contains("<") && !line.contains("=")) {
                        boolean hasNotEncounteredText = true;
                        boolean hasEncounteredColumn = false;
                        char[] chars = line.toCharArray();
                        List<String> values = new ArrayList<>();
                        String type = "";
                        String variableName = "";

                        for (Character character : chars) {
                            if (!character.equals(' ') && !character.equals('\t') && hasNotEncounteredText) {
                                hasNotEncounteredText = false;
                                type = character.toString();
                            } else if (character.equals(':')) {
                                hasEncounteredColumn = true;
                            } else if (!character.equals(' ') && !character.equals('\t') && hasEncounteredColumn) {
                                variableName += character.toString();
                            } else if (character.equals(' ') && !hasNotEncounteredText && hasEncounteredColumn) {
                                break;
                            }
                        }

                        while ((line = reader.readLine()) != null) {
                            if (line.contains("\t>")) break;
                            chars = line.toCharArray();
                            String value = "";
                            for (Character character : chars) {
                                if (!character.equals('\t')) {
                                    value += character.toString();
                                }
                            }
                            if (!value.equals("")) values.add(value);
                        }

                        switch (type) {
                            case "I":
                                List<Integer> list1 = new ArrayList<>();
                                for (String value : values) list1.add(Integer.parseInt(value));
                                clazz.getField(variableName).set(clazz, list1);
                                break;
                            case "S":
                                List<String> list2 = new ArrayList<>();
                                for (String value : values) list2.add(value);
                                clazz.getField(variableName).set(clazz, list2);
                                break;
                            case "B":
                                List<Boolean> list3 = new ArrayList<>();
                                for (String value : values) list3.add(Boolean.parseBoolean(value));
                                clazz.getField(variableName).set(clazz, list3);
                                break;
                            case "L":
                                List<Long> list4 = new ArrayList<>();
                                for (String value : values) list4.add(Long.parseLong(value));
                                clazz.getField(variableName).set(clazz, list4);
                                break;
                        }
                    } else if (line.contains("[") && !line.contains("=")) {
                        boolean hasNotEncounteredText = true;
                        boolean hasEncounteredColumn = false;
                        char[] chars = line.toCharArray();
                        Character prevChar = ' ';
                        Map<String, String> values = new LinkedHashMap<>();
                        String keyType = "";
                        String valueType = "";
                        String variableName = "";

                        for (Character character : chars) {
                            if (!character.equals(' ') && !character.equals('\t') && hasNotEncounteredText) {
                                hasNotEncounteredText = false;
                                keyType = character.toString();
                            } else if (prevChar.equals('-')) {
                                valueType = character.toString();
                            } else if (character.equals(':')) {
                                hasEncounteredColumn = true;
                            } else if (!character.equals(' ') && !character.equals('\t') && hasEncounteredColumn) {
                                variableName += character.toString();
                            } else if (character.equals(' ') && !hasNotEncounteredText && hasEncounteredColumn) {
                                break;
                            }
                            prevChar = character;
                        }

                        while ((line = reader.readLine()) != null) {
                            if (line.startsWith("\t]")) break;
                            System.out.println(line);
                            chars = line.toCharArray();
                            boolean hasEncounteredEquals = false;
                            String key = "";
                            String value = "";

                            for (Character character : chars) {
                                if (character.equals('=') && !hasEncounteredEquals) {
                                    hasEncounteredEquals = true;
                                } else if (!character.equals('\t') && !hasEncounteredEquals) {
                                    key += character.toString();
                                } else if (!character.equals('\t') && hasEncounteredEquals) {
                                    value += character.toString();
                                }
                            }
                            if (!key.equals("")) values.put(key, value);

                            switch (keyType) {
                                case "I":
                                    switch (valueType) {
                                        case "I":
                                            Map<Integer, Integer> map1 = new LinkedHashMap<>();
                                            for (String mapKey : values.keySet())
                                                map1.put(Integer.valueOf(mapKey), Integer.valueOf(values.get(mapKey)));
                                            clazz.getField(variableName).set(clazz, map1);
                                            break;
                                        case "S":
                                            Map<Integer, String> map2 = new LinkedHashMap<>();
                                            for (String mapKey : values.keySet())
                                                map2.put(Integer.valueOf(mapKey), values.get(mapKey));
                                            clazz.getField(variableName).set(clazz, map2);
                                            break;
                                        case "B":
                                            Map<Integer, Boolean> map3 = new LinkedHashMap<>();
                                            for (String mapKey : values.keySet())
                                                map3.put(Integer.valueOf(mapKey), Boolean.valueOf(values.get(mapKey)));
                                            clazz.getField(variableName).set(clazz, map3);
                                            break;
                                        case "L":
                                            Map<Integer, Long> map4 = new LinkedHashMap<>();
                                            for (String mapKey : values.keySet())
                                                map4.put(Integer.valueOf(mapKey), Long.valueOf(values.get(mapKey)));
                                            clazz.getField(variableName).set(clazz, map4);
                                            break;
                                    }
                                    break;
                                case "S":
                                    switch (valueType) {
                                        case "I":
                                            Map<String, Integer> map1 = new LinkedHashMap<>();
                                            for (String mapKey : values.keySet())
                                                map1.put(mapKey, Integer.valueOf(values.get(mapKey)));
                                            clazz.getField(variableName).set(clazz, map1);
                                            break;
                                        case "S":
                                            Map<String, String> map2 = new LinkedHashMap<>();
                                            for (String mapKey : values.keySet()) map2.put(mapKey, values.get(mapKey));
                                            clazz.getField(variableName).set(clazz, map2);
                                            break;
                                        case "B":
                                            Map<String, Boolean> map3 = new LinkedHashMap<>();
                                            for (String mapKey : values.keySet())
                                                map3.put(mapKey, Boolean.valueOf(values.get(mapKey)));
                                            clazz.getField(variableName).set(clazz, map3);
                                            break;
                                        case "L":
                                            Map<String, Long> map4 = new LinkedHashMap<>();
                                            for (String mapKey : values.keySet())
                                                map4.put(mapKey, Long.valueOf(values.get(mapKey)));
                                            clazz.getField(variableName).set(clazz, map4);
                                            break;
                                    }
                                    break;
                                case "B":
                                    switch (valueType) {
                                        case "I":
                                            Map<Boolean, Integer> map1 = new LinkedHashMap<>();
                                            for (String mapKey : values.keySet())
                                                map1.put(Boolean.valueOf(mapKey), Integer.valueOf(values.get(mapKey)));
                                            clazz.getField(variableName).set(clazz, map1);
                                            break;
                                        case "S":
                                            Map<Boolean, String> map2 = new LinkedHashMap<>();
                                            for (String mapKey : values.keySet())
                                                map2.put(Boolean.valueOf(mapKey), values.get(mapKey));
                                            clazz.getField(variableName).set(clazz, map2);
                                            break;
                                        case "B":
                                            Map<Boolean, Boolean> map3 = new LinkedHashMap<>();
                                            for (String mapKey : values.keySet())
                                                map3.put(Boolean.valueOf(mapKey), Boolean.valueOf(values.get(mapKey)));
                                            clazz.getField(variableName).set(clazz, map3);
                                            break;
                                        case "L":
                                            Map<Boolean, Long> map4 = new LinkedHashMap<>();
                                            for (String mapKey : values.keySet())
                                                map4.put(Boolean.valueOf(mapKey), Long.valueOf(values.get(mapKey)));
                                            clazz.getField(variableName).set(clazz, map4);
                                            break;
                                    }
                                    break;
                                case "L":
                                    switch (valueType) {
                                        case "I":
                                            Map<Long, Integer> map1 = new LinkedHashMap<>();
                                            for (String mapKey : values.keySet())
                                                map1.put(Long.valueOf(mapKey), Integer.valueOf(values.get(mapKey)));
                                            clazz.getField(variableName).set(clazz, map1);
                                            break;
                                        case "S":
                                            Map<Long, String> map2 = new LinkedHashMap<>();
                                            for (String mapKey : values.keySet())
                                                map2.put(Long.valueOf(mapKey), values.get(mapKey));
                                            clazz.getField(variableName).set(clazz, map2);
                                            break;
                                        case "B":
                                            Map<Long, Boolean> map3 = new LinkedHashMap<>();
                                            for (String mapKey : values.keySet())
                                                map3.put(Long.valueOf(mapKey), Boolean.valueOf(values.get(mapKey)));
                                            clazz.getField(variableName).set(clazz, map3);
                                            break;
                                        case "L":
                                            Map<Long, Long> map4 = new LinkedHashMap<>();
                                            for (String mapKey : values.keySet())
                                                map4.put(Long.valueOf(mapKey), Long.valueOf(values.get(mapKey)));
                                            clazz.getField(variableName).set(clazz, map4);
                                            break;
                                    }
                                    break;
                            }
                        }
                    } else {
                        boolean hasNotEncounteredText = true;
                        boolean hasEncounteredColumn = false;
                        boolean hasEncounteredEquals = false;
                        char[] chars = line.toCharArray();
                        String type = "";
                        String variableName = "";
                        String contents = "";
                        for (Character character : chars) {
                            if (!character.equals(' ') && !character.equals('\t') && hasNotEncounteredText) {
                                hasNotEncounteredText = false;
                                type = character.toString();
                            } else if (character.equals(':')) {
                                hasEncounteredColumn = true;
                            } else if (!character.equals(' ') && !character.equals('\t') && hasEncounteredColumn && character.equals('=')) {
                                hasEncounteredEquals = true;
                            } else if (!character.equals(' ') && !character.equals('\t') && hasEncounteredColumn && !hasEncounteredEquals) {
                                variableName += character;
                            } else if (hasEncounteredColumn && hasEncounteredEquals) {
                                contents += character;
                            }
                        }

                        switch (type) {
                            case "I":
                                clazz.getField(variableName).set(clazz, Integer.parseInt(contents));
                                break;
                            case "S":
                                clazz.getField(variableName).set(clazz, contents);
                                break;
                            case "B":
                                clazz.getField(variableName).set(clazz, Boolean.parseBoolean(contents));
                                break;
                            case "L":
                                clazz.getField(variableName).set(clazz, Long.parseLong(contents));
                                break;
                        }
                    }
                }
            }catch (NumberFormatException | NoSuchFieldException| IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressWarnings("unchecked")
    public StringBuilder write(StringBuilder stringBuilder) {
        stringBuilder.append(StringUtils.repeat('#', 106)).append("\n");
        stringBuilder.append("# ").append(name).append("\n");
        stringBuilder.append(StringUtils.repeat('#', 106)).append("\n");

        if (!comment.equals("")) {
            stringBuilder.append("# ").append(comment).append("\n");
            stringBuilder.append(StringUtils.repeat('#', 106)).append("\n").append("\n");
        }


        stringBuilder.append(name).append(" {\n");

        Iterator<Setting> iterator = settings.iterator();
        while(iterator.hasNext()){
            Setting setting = iterator.next();

            if(!setting.getComment().equals("")) stringBuilder.append("\t# ").append(setting.getComment()).append("\n");
            if(setting.getType() == Setting.Type.STRING) {
                Setting<String> aSetting = (Setting<String>) setting;
                stringBuilder.append("\tS:").append(aSetting.getName()).append("=").append(aSetting.getValue()).append("\n");
            } else if(setting.getType() == Setting.Type.INTEGER) {
                Setting<Integer> aSetting = (Setting<Integer>) setting;
                stringBuilder.append("\tI:").append(aSetting.getName()).append("=").append(aSetting.getValue()).append("\n");
            } else if(setting.getType() == Setting.Type.BOOLEAN) {
                Setting<Boolean> aSetting = (Setting<Boolean>) setting;
                stringBuilder.append("\tB:").append(aSetting.getName()).append("=").append(aSetting.getValue()).append("\n");
            } else if(setting.getType() == Setting.Type.LIST) {
                Setting<List> aSetting = (Setting<List>) setting;
                stringBuilder.append("\t").append(getTypeLetterForList(aSetting.getValue())).append(":").append(aSetting.getName()).append(" <\n");
                for(Object object : aSetting.getValue()) stringBuilder.append("\t\t").append(object).append("\n");
                stringBuilder.append("\t>\n");
            } else if(setting.getType() == Setting.Type.MAP) {
                Setting<Map> aSetting = (Setting<Map>) setting;
                stringBuilder.append("\t").append(getTypeLettersForMap(aSetting.getValue())).append(":").append(aSetting.getName()).append(" [\n");
                for(Object object : aSetting.getValue().keySet()) stringBuilder.append("\t\t").append(object).append("=").append(aSetting.getValue().get(object)).append("\n");
                stringBuilder.append("\t]\n");
            } else if(setting.getType() == Setting.Type.LONG) {
                Setting<Long> aSetting = (Setting<Long>) setting;
                stringBuilder.append("\tL:").append(aSetting.getName()).append("=").append(aSetting.getValue()).append("\n");
            }
            if(iterator.hasNext()) stringBuilder.append("\n");
        }

        stringBuilder.append("}\n\n\n");
        return stringBuilder;
    }

    private String getTypeLetterForList(List list) {
        if (list.get(0) instanceof String) return "S";
        if (list.get(0) instanceof Integer) return "I";
        if (list.get(0) instanceof Boolean) return "B";
        if (list.get(0) instanceof Long) return "L";
        return null;
    }

    private String getTypeLettersForMap(Map map) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Object object : map.keySet()) {
            if (object instanceof String) {
                stringBuilder.append("S");
                break;
            } else if (object instanceof Integer) {
                stringBuilder.append("I");
                break;
            } else if (object instanceof Boolean) {
                stringBuilder.append("B");
                break;
            } else if (object instanceof Long) {
                stringBuilder.append("L");
                break;
            }
        }
        stringBuilder.append("-");
        for(Object object : map.values()) {
            if (object instanceof String) {
                stringBuilder.append("S");
                break;
            } else if (object instanceof Integer) {
                stringBuilder.append("I");
                break;
            } else if (object instanceof Boolean) {
                stringBuilder.append("B");
                break;
            } else if (object instanceof Long) {
                stringBuilder.append("L");
                break;
            }
        }
        return stringBuilder.toString();
    }
}
