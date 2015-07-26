package HxCKDMS.HxCCore.api.Configuration;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

    @SuppressWarnings("unchecked")
    public StringBuilder write(StringBuilder stringBuilder) {
        stringBuilder.append("\n").append(StringUtils.repeat('#', 106)).append("\n");
        stringBuilder.append("# ").append(name).append("\n");
        stringBuilder.append("#").append(StringUtils.repeat('#', 104)).append("#\n");
        stringBuilder.append("# ").append(comment).append("\n");
        stringBuilder.append(StringUtils.repeat('#', 106)).append("\n").append("\n");

        stringBuilder.append(name).append(" {\n");

        Iterator<Setting> iterator = settings.iterator();
        while(iterator.hasNext()){
            Setting setting = iterator.next();

            stringBuilder.append("\t# ").append(setting.getComment()).append("\n");
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
                stringBuilder.append("\tList-").append(getTypeLetterForList(aSetting.getValue())).append(":").append(aSetting.getName()).append(" <\n");
                for(Object object : aSetting.getValue()) stringBuilder.append("\t\t").append(object).append("\n");
                stringBuilder.append("\t>\n");
            } else if(setting.getType() == Setting.Type.MAP) {
                Setting<Map> aSetting = (Setting<Map>) setting;
                stringBuilder.append("\tMap-").append(getTypeLettersForMap(aSetting.getValue())).append(":").append(aSetting.getName()).append(" [\n");
                for(Object object : aSetting.getValue().keySet()) stringBuilder.append("\t\t").append(object).append(" > ").append(aSetting.getValue().get(object)).append("\n");
                stringBuilder.append("\t]\n");
            } else if(setting.getType() == Setting.Type.LONG) {
                Setting<Long> aSetting = (Setting<Long>) setting;
                stringBuilder.append("\tL:").append(aSetting.getName()).append("=").append(aSetting.getValue()).append("\n");
            }
            if(iterator.hasNext()) stringBuilder.append("\n");
        }

        stringBuilder.append("}\n\n");
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
        stringBuilder.append("*");
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
