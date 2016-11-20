package com.elasda.data.model;

public class StringKey implements Key {
    private static final long serialVersionUID = 1L;

    private final String key;

    public StringKey(String key) {
        this.key = key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StringKey stringKey = (StringKey) o;

        return key.equals(stringKey.key);

    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }

    @Override
    public String toString() {
        return "StringKey{" +
                "key='" + key + '\'' +
                '}';
    }
}
