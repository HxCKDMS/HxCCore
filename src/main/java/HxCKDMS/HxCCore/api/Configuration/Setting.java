package HxCKDMS.HxCCore.api.Configuration;

class Setting<T> {
    private String comment;
    private T value;
    private Type type;
    private String name;

    private long minValue;
    private long maxValue;

    private String[] validValues;

    public Setting(String comment, T value, Type type, String name) {
        this.comment = comment;
        this.value = value;
        this.type = type;
        this.name = name;
    }

    public Setting(String comment, T value, Type type, String name, long minValue, long maxValue) {
        this.comment = comment;
        this.value = value;
        this.type = type;
        this.name = name;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public Setting(String[] validValues) {
        this.validValues = validValues;
    }

    public T getValue() {
        return value;
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
