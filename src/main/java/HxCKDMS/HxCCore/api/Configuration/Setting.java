package HxCKDMS.HxCCore.api.Configuration;

import java.lang.reflect.Field;

class Setting<T> {
    private Class<?> clazz;
    private String comment;
    private Field field;
    private Type type;
    private String name;

    private double minValue;
    private double maxValue;

    private String[] validValues;

    public Setting(Class<?> clazz, String comment, Field field, Type type, String name) {
        this.clazz = clazz;
        this.comment = comment;
        this.field = field;
        this.type = type;
        this.name = name;
    }

    public Setting(Class<?> clazz, String comment, Field field, Type type, String name, double minValue, double maxValue) {
        this.clazz = clazz;
        this.comment = comment;
        this.field = field;
        this.type = type;
        this.name = name;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public Setting(Class<?> clazz, String comment, Field field, Type type, String name, String[] validValues) {
        this.clazz = clazz;
        this.comment = comment;
        this.field = field;
        this.type = type;
        this.name = name;
        this.validValues = validValues;
    }

    public Setting(String[] validValues) {
        this.validValues = validValues;
    }

    @SuppressWarnings("unchecked")
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

    public double getMaxValue() {
        return maxValue;
    }

    public double getMinValue() {
        return minValue;
    }

    public String[] getValidValues() {
        return validValues;
    }

    enum Type {
        STRING, INTEGER, MAP, LIST, LONG, FLOAT, DOUBLE, BOOLEAN
    }
}
