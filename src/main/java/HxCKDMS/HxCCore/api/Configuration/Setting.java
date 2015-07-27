package HxCKDMS.HxCCore.api.Configuration;

import java.lang.reflect.Field;

class Setting<T> {
    private Class<?> clazz;
    private String comment;
    private Field field;
    private Type type;
    private String name;
    private boolean force;

    private long minValue;
    private long maxValue;

    private String[] validValues;

    public Setting(Class<?> clazz, String comment, Field field, Type type, String name, boolean force) {
        this.clazz = clazz;
        this.comment = comment;
        this.field = field;
        this.type = type;
        this.name = name;
        this.force = force;
    }

    public Setting(Class<?> clazz, String comment, Field field, Type type, String name, boolean force, long minValue, long maxValue) {
        this.clazz = clazz;
        this.comment = comment;
        this.field = field;
        this.type = type;
        this.name = name;
        this.force = force;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public Setting(Class<?> clazz, String comment, Field field, Type type, String name, boolean force, String[] validValues) {
        this.clazz = clazz;
        this.comment = comment;
        this.field = field;
        this.type = type;
        this.name = name;
        this.force = force;
        this.validValues = validValues;
    }

    public Setting(String[] validValues) {
        this.validValues = validValues;
    }

    public T getValue() {
        try {
            return (T) field.get(clazz);
        } catch (IllegalAccessException e) {
            return null;
        }
    }

    public String getComment() {
        return comment;
    }

    public Type getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public boolean isForced() {
        return force;
    }

    public long getMaxValue() {
        return maxValue;
    }

    public long getMinValue() {
        return minValue;
    }

    public String[] getValidValues() {
        return validValues;
    }

    enum Type {
        STRING, INTEGER, MAP, LIST, LONG, BOOLEAN
    }
}
