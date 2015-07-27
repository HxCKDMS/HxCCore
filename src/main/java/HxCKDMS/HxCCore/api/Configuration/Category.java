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
                        readList(line, clazz, reader);
                    } else if (line.contains("[") && !line.contains("=")) {
                        readMap(line, clazz, reader);
                    } else {
                        readNormalVariable(line, clazz);
                    }
                }
            }catch (NumberFormatException | NoSuchFieldException| IllegalAccessException ignored) {}
        }
    }

    public void readNormalVariable(String line, Class<?> clazz) throws NoSuchFieldException, IllegalAccessException, NumberFormatException {
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
                if(clazz.getField(variableName).getDeclaredAnnotation(Config.Integer.class).forceReset() && Integer.parseInt((String) clazz.getField(variableName).get(clazz)) != 0) return;
                clazz.getField(variableName).set(clazz, Integer.parseInt(contents));
                break;
            case "S":
                if(clazz.getField(variableName).getDeclaredAnnotation(Config.String.class).forceReset() && !(clazz.getField(variableName).get(clazz)).equals("")) return;
                clazz.getField(variableName).set(clazz, contents);
                break;
            case "B":
                if(clazz.getField(variableName).getDeclaredAnnotation(Config.Boolean.class).forceReset() && !Boolean.parseBoolean((String) clazz.getField(variableName).get(clazz))) return;
                clazz.getField(variableName).set(clazz, Boolean.parseBoolean(contents));
                break;
            case "L":
                if(clazz.getField(variableName).getDeclaredAnnotation(Config.Long.class).forceReset() && Long.parseLong((String) clazz.getField(variableName).get(clazz)) != 0L) return;
                clazz.getField(variableName).set(clazz, Long.parseLong(contents));
                break;
            case "F":
                if(clazz.getField(variableName).getDeclaredAnnotation(Config.Float.class).forceReset() && Float.parseFloat((String) clazz.getField(variableName).get(clazz)) != 0F) return;
                clazz.getField(variableName).set(clazz, Float.parseFloat(contents));
                break;
            case "D":
                if(clazz.getField(variableName).getDeclaredAnnotation(Config.Double.class).forceReset() && Double.parseDouble((String) clazz.getField(variableName).get(clazz)) != 0D) return;
                clazz.getField(variableName).set(clazz, Double.parseDouble(contents));
                break;
        }
    }

    public void readMap(String line, Class<?> clazz, BufferedReader reader) throws NoSuchFieldException, IllegalAccessException, IOException, NumberFormatException {
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

        if(clazz.getField(variableName).getDeclaredAnnotation(Config.Map.class).forceReset() && clazz.getField(variableName).get(clazz) != null) return;

        while ((line = reader.readLine()) != null) {
            if (line.startsWith("\t]")) break;
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
        }

            switch (keyType) {
                case "I":
                    switch (valueType) {
                        case "I":
                            Map<Integer, Integer> map1 = new LinkedHashMap<>();
                            for (String mapKey : values.keySet()) map1.put(Integer.parseInt(mapKey), Integer.parseInt(values.get(mapKey)));
                            clazz.getField(variableName).set(clazz, map1);
                            break;
                        case "S":
                            Map<Integer, String> map2 = new LinkedHashMap<>();
                            for (String mapKey : values.keySet()) map2.put(Integer.parseInt(mapKey), values.get(mapKey));
                            clazz.getField(variableName).set(clazz, map2);
                            break;
                        case "B":
                            Map<Integer, Boolean> map3 = new LinkedHashMap<>();
                            for (String mapKey : values.keySet()) map3.put(Integer.parseInt(mapKey), Boolean.parseBoolean(values.get(mapKey)));
                            clazz.getField(variableName).set(clazz, map3);
                            break;
                        case "L":
                            Map<Integer, Long> map4 = new LinkedHashMap<>();
                            for (String mapKey : values.keySet()) map4.put(Integer.parseInt(mapKey), Long.parseLong(values.get(mapKey)));
                            clazz.getField(variableName).set(clazz, map4);
                            break;
                        case "F":
                            Map<Integer, Float> map5 = new LinkedHashMap<>();
                            for (String mapKey : values.keySet()) map5.put(Integer.parseInt(mapKey), Float.parseFloat(values.get(mapKey)));
                            clazz.getField(variableName).set(clazz, map5);
                            break;
                        case "D":
                            Map<Integer, Double> map6 = new LinkedHashMap<>();
                            for (String mapKey : values.keySet()) map6.put(Integer.parseInt(mapKey), Double.parseDouble(values.get(mapKey)));
                            clazz.getField(variableName).set(clazz, map6);
                            break;
                    }
                    break;
                case "S":
                    switch (valueType) {
                        case "I":
                            Map<String, Integer> map1 = new LinkedHashMap<>();
                            for (String mapKey : values.keySet()) map1.put(mapKey, Integer.parseInt(values.get(mapKey)));
                            clazz.getField(variableName).set(clazz, map1);
                            break;
                        case "S":
                            Map<String, String> map2 = new LinkedHashMap<>();
                            for (String mapKey : values.keySet()) map2.put(mapKey, values.get(mapKey));
                            clazz.getField(variableName).set(clazz, map2);
                            break;
                        case "B":
                            Map<String, Boolean> map3 = new LinkedHashMap<>();
                            for (String mapKey : values.keySet()) map3.put(mapKey, Boolean.parseBoolean(values.get(mapKey)));
                            clazz.getField(variableName).set(clazz, map3);
                            break;
                        case "L":
                            Map<String, Long> map4 = new LinkedHashMap<>();
                            for (String mapKey : values.keySet()) map4.put(mapKey, Long.parseLong(values.get(mapKey)));
                            clazz.getField(variableName).set(clazz, map4);
                            break;
                        case "F":
                            Map<String, Float> map5 = new LinkedHashMap<>();
                            for (String mapKey : values.keySet()) map5.put(mapKey, Float.parseFloat(values.get(mapKey)));
                            clazz.getField(variableName).set(clazz, map5);
                            break;
                        case "D":
                            Map<String, Double> map6 = new LinkedHashMap<>();
                            for (String mapKey : values.keySet()) map6.put(mapKey, Double.parseDouble(values.get(mapKey)));
                            clazz.getField(variableName).set(clazz, map6);
                            break;
                    }
                    break;
                case "B":
                    switch (valueType) {
                        case "I":
                            Map<Boolean, Integer> map1 = new LinkedHashMap<>();
                            for (String mapKey : values.keySet()) map1.put(Boolean.parseBoolean(mapKey), Integer.parseInt(values.get(mapKey)));
                            clazz.getField(variableName).set(clazz, map1);
                            break;
                        case "S":
                            Map<Boolean, String> map2 = new LinkedHashMap<>();
                            for (String mapKey : values.keySet()) map2.put(Boolean.parseBoolean(mapKey), values.get(mapKey));
                            clazz.getField(variableName).set(clazz, map2);
                            break;
                        case "B":
                            Map<Boolean, Boolean> map3 = new LinkedHashMap<>();
                            for (String mapKey : values.keySet()) map3.put(Boolean.parseBoolean(mapKey), Boolean.parseBoolean(values.get(mapKey)));
                            clazz.getField(variableName).set(clazz, map3);
                            break;
                        case "L":
                            Map<Boolean, Long> map4 = new LinkedHashMap<>();
                            for (String mapKey : values.keySet()) map4.put(Boolean.parseBoolean(mapKey), Long.parseLong(values.get(mapKey)));
                            clazz.getField(variableName).set(clazz, map4);
                            break;
                        case "F":
                            Map<Boolean, Float> map5 = new LinkedHashMap<>();
                            for (String mapKey : values.keySet()) map5.put(Boolean.parseBoolean(mapKey), Float.parseFloat(values.get(mapKey)));
                            clazz.getField(variableName).set(clazz, map5);
                            break;
                        case "D":
                            Map<Boolean, Double> map6 = new LinkedHashMap<>();
                            for (String mapKey : values.keySet()) map6.put(Boolean.parseBoolean(mapKey), Double.parseDouble(values.get(mapKey)));
                            clazz.getField(variableName).set(clazz, map6);
                            break;
                    }
                    break;
                case "L":
                    switch (valueType) {
                        case "I":
                            Map<Long, Integer> map1 = new LinkedHashMap<>();
                            for (String mapKey : values.keySet()) map1.put(Long.parseLong(mapKey), Integer.parseInt(values.get(mapKey)));
                            clazz.getField(variableName).set(clazz, map1);
                            break;
                        case "S":
                            Map<Long, String> map2 = new LinkedHashMap<>();
                            for (String mapKey : values.keySet()) map2.put(Long.parseLong(mapKey), values.get(mapKey));
                            clazz.getField(variableName).set(clazz, map2);
                            break;
                        case "B":
                            Map<Long, Boolean> map3 = new LinkedHashMap<>();
                            for (String mapKey : values.keySet()) map3.put(Long.parseLong(mapKey), Boolean.parseBoolean(values.get(mapKey)));
                            clazz.getField(variableName).set(clazz, map3);
                            break;
                        case "L":
                            Map<Long, Long> map4 = new LinkedHashMap<>();
                            for (String mapKey : values.keySet()) map4.put(Long.parseLong(mapKey), Long.parseLong(values.get(mapKey)));
                            clazz.getField(variableName).set(clazz, map4);
                            break;
                        case "F":
                            Map<Long, Float> map5 = new LinkedHashMap<>();
                            for (String mapKey : values.keySet()) map5.put(Long.parseLong(mapKey), Float.parseFloat(values.get(mapKey)));
                            clazz.getField(variableName).set(clazz, map5);
                            break;
                        case "D":
                            Map<Long, Double> map6 = new LinkedHashMap<>();
                            for (String mapKey : values.keySet()) map6.put(Long.parseLong(mapKey), Double.parseDouble(values.get(mapKey)));
                            clazz.getField(variableName).set(clazz, map6);
                            break;
                    }
                    break;
                case "F":
                    switch (valueType) {
                        case "I":
                            Map<Float, Integer> map1 = new LinkedHashMap<>();
                            for (String mapKey : values.keySet()) map1.put(Float.parseFloat(mapKey), Integer.parseInt(values.get(mapKey)));
                            clazz.getField(variableName).set(clazz, map1);
                            break;
                        case "S":
                            Map<Float, String> map2 = new LinkedHashMap<>();
                            for (String mapKey : values.keySet()) map2.put(Float.parseFloat(mapKey), values.get(mapKey));
                            clazz.getField(variableName).set(clazz, map2);
                            break;
                        case "B":
                            Map<Float, Boolean> map3 = new LinkedHashMap<>();
                            for (String mapKey : values.keySet()) map3.put(Float.parseFloat(mapKey), Boolean.parseBoolean(values.get(mapKey)));
                            clazz.getField(variableName).set(clazz, map3);
                            break;
                        case "L":
                            Map<Float, Long> map4 = new LinkedHashMap<>();
                            for (String mapKey : values.keySet()) map4.put(Float.parseFloat(mapKey), Long.parseLong(values.get(mapKey)));
                            clazz.getField(variableName).set(clazz, map4);
                            break;
                        case "F":
                            Map<Float, Float> map5 = new LinkedHashMap<>();
                            for (String mapKey : values.keySet()) map5.put(Float.parseFloat(mapKey), Float.parseFloat(values.get(mapKey)));
                            clazz.getField(variableName).set(clazz, map5);
                            break;
                        case "D":
                            Map<Float, Double> map6 = new LinkedHashMap<>();
                            for (String mapKey : values.keySet()) map6.put(Float.parseFloat(mapKey), Double.parseDouble(values.get(mapKey)));
                            clazz.getField(variableName).set(clazz, map6);
                            break;
                    }
                    break;
                case "D":
                    switch (valueType) {
                        case "I":
                            Map<Double, Integer> map1 = new LinkedHashMap<>();
                            for (String mapKey : values.keySet()) map1.put(Double.parseDouble(mapKey), Integer.parseInt(values.get(mapKey)));
                            clazz.getField(variableName).set(clazz, map1);
                            break;
                        case "S":
                            Map<Double, String> map2 = new LinkedHashMap<>();
                            for (String mapKey : values.keySet()) map2.put(Double.parseDouble(mapKey), values.get(mapKey));
                            clazz.getField(variableName).set(clazz, map2);
                            break;
                        case "B":
                            Map<Double, Boolean> map3 = new LinkedHashMap<>();
                            for (String mapKey : values.keySet()) map3.put(Double.parseDouble(mapKey), Boolean.parseBoolean(values.get(mapKey)));
                            clazz.getField(variableName).set(clazz, map3);
                            break;
                        case "L":
                            Map<Double, Long> map4 = new LinkedHashMap<>();
                            for (String mapKey : values.keySet()) map4.put(Double.parseDouble(mapKey), Long.parseLong(values.get(mapKey)));
                            clazz.getField(variableName).set(clazz, map4);
                            break;
                        case "F":
                            Map<Double, Float> map5 = new LinkedHashMap<>();
                            for (String mapKey : values.keySet()) map5.put(Double.parseDouble(mapKey), Float.parseFloat(values.get(mapKey)));
                            clazz.getField(variableName).set(clazz, map5);
                            break;
                        case "D":
                            Map<Double, Double> map6 = new LinkedHashMap<>();
                            for (String mapKey : values.keySet()) map6.put(Double.parseDouble(mapKey), Double.parseDouble(values.get(mapKey)));
                            clazz.getField(variableName).set(clazz, map6);
                            break;
                    }
                    break;
            }
    }

    public void readList(String line, Class<?> clazz, BufferedReader reader) throws IOException, NoSuchFieldException, IllegalAccessException, NumberFormatException {
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

        if(clazz.getField(variableName).getDeclaredAnnotation(Config.List.class).forceReset() && clazz.getField(variableName).get(clazz) != null) return;

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
            case "F":
                List<Float> list5 = new ArrayList<>();
                for (String value : values) list5.add(Float.parseFloat(value));
                clazz.getField(variableName).set(clazz, list5);
                break;
            case "D":
                List<Double> list6 = new ArrayList<>();
                for (String value : values) list6.add(Double.parseDouble(value));
                clazz.getField(variableName).set(clazz, list6);
                break;
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
        } else stringBuilder.append("\n");


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
            } else if(setting.getType() == Setting.Type.FLOAT) {
                Setting<Float> aSetting = (Setting<Float>) setting;
                stringBuilder.append("\tI:").append(aSetting.getName()).append("=").append(aSetting.getValue()).append("\n");
            } else if(setting.getType() == Setting.Type.DOUBLE) {
                Setting<Double> aSetting = (Setting<Double>) setting;
                stringBuilder.append("\tB:").append(aSetting.getName()).append("=").append(aSetting.getValue()).append("\n");
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
        if (list.get(0) instanceof Float) return "F";
        if (list.get(0) instanceof Double) return "D";
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
            } else if (object instanceof Float) {
                stringBuilder.append("F");
                break;
            } else if (object instanceof Double) {
                stringBuilder.append("D");
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
            } else if (object instanceof Float) {
                stringBuilder.append("F");
                break;
            } else if (object instanceof Double) {
                stringBuilder.append("D");
                break;
            }
        }
        return stringBuilder.toString();
    }
}
