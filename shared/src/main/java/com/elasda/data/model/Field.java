package com.elasda.data.model;

import java.io.Serializable;

public class Field implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String name;
    private final FieldType type;
    private final boolean key;
    private final int index;

    public Field(String name, FieldType type, boolean key, int index) {
        this.name = name;
        this.type = type;
        this.key = key;
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public FieldType getType() {
        return type;
    }

    public boolean isKey() {
        return key;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Field field = (Field) o;

        if (key != field.key) return false;
        if (index != field.index) return false;
        if (!name.equals(field.name)) return false;
        return type.equals(field.type);

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + type.hashCode();
        result = 31 * result + (key ? 1 : 0);
        result = 31 * result + index;
        return result;
    }

    @Override
    public String toString() {
        return "Field{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", key=" + key +
                ", index=" + index +
                '}';
    }
}
