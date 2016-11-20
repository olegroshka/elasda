package com.elasda.data.model;

import java.io.Serializable;

public final class FieldType implements Serializable {

    public static final FieldType TEXT = new FieldType('s');
    public static final FieldType BOOLEAN = new FieldType('b');
    public static final FieldType INT = new FieldType('i');
    public static final FieldType LONG = new FieldType('l');
    public static final FieldType DOUBLE = new FieldType('f');
    public static final FieldType DATETIME = new FieldType('t');
    public static final FieldType DATE = new FieldType('d');

    public static FieldType valueOf(char t) {
        switch (t) {
            case 's':
                return TEXT;
            case 'b':
                return BOOLEAN;
            case 'i':
                return INT;
            case 'l':
                return LONG;
            case 'f':
                return DOUBLE;
            case 't':
                return DATETIME;
            case 'd':
                return DATE;

            default:
                throw new RuntimeException("unknown type: " + t);
        }
    }

    private final char name;

    private FieldType(char name) {
        this.name = name;
    }

    public char getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FieldType fieldType = (FieldType) o;

        return name == fieldType.name;

    }

    @Override
    public int hashCode() {
        return (int) name;
    }

    @Override
    public String toString() {
        return "FieldType{" +
                "name=" + name +
                '}';
    }
}
