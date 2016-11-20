package com.elasda.data.model;

import java.io.Serializable;
import java.util.Arrays;

public class Record implements Serializable {
    private static final long serialVersionUID = 1L;

    protected final Object[] data;
    private long version = -1;

    Record() { //for serializer
        this.data = null;
    }

    Record(Object[] data) {
        this.data = data;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public Object getValue(int index) {
        return data[index];
    }

    public void setValue(int index, Object value) {
        this.data[index] = value;
    }

    public void setValue(int index, double value) {
        this.data[index] = value;
    }

    public void setValue(int index, long value) {
        this.data[index] = value;
    }

    public void setValue(int index, int value) {
        this.data[index] = value;
    }

    public void setValue(int index, boolean value) {
        this.data[index] = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Record record = (Record) o;

        if (version != record.version) return false;
        return Arrays.equals(data, record.data);

    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(data);
        result = 31 * result + (int) (version ^ (version >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Record{" +
                "version=" + version +
                ", data=" + Arrays.toString(data) +
                '}';
    }

}
