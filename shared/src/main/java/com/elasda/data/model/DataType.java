package com.elasda.data.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class DataType implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String name;
    private final Field[] fields;
    private long version = -1;
    private transient Map<String, Field> fieldByName;
    private transient Field[] keyFields;
    private transient KeyFactory keyFactory;

    public DataType(String name, Field... fields) {
        this.name = name;
        this.fields = fields;
        init(name, fields);
    }

    private void init(String name, Field[] fields) {
        if( fieldByName == null || keyFields == null) {
            this.fieldByName = new HashMap<>(fields.length);
            ArrayList<Field> keyFields = new ArrayList<>(fields.length);
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                fieldByName.put(field.getName(), field);
                if (field.isKey()) {
                    keyFields.add(field);
                }
            }
            this.keyFields = keyFields.toArray(new Field[keyFields.size()]);
            if (keyFields.size() == 0) {
                throw new RuntimeException("At least one filed has to be key, type: " + name);
            }
            this.keyFactory = new StringKeyFactor();
        }
    }

    public Record newDataRow() { //todo defaults
        return new Record(new Object[fields.length]);
    }

    public Key newKey(Record record) {
        return keyFactory.create(keyFields, record.data);
    }

    public Key newKey(Object... keyValues) {
        return keyFactory.create(keyFields, keyValues);
    }

    public String getName() {
        return name;
    }

    public Field getField(int i) {
        return fields[i];
    }

    public Field getField(String name) {
        return fieldByName.get(name);
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    private void readObject(ObjectInputStream is) throws ClassNotFoundException, IOException {
        is.defaultReadObject();
        init(name, fields);
    }

    private void writeObject(ObjectOutputStream os) throws IOException {
        os.defaultWriteObject();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataType dataType = (DataType) o;

        if (version != dataType.version) return false;
        if (!name.equals(dataType.name)) return false;
        return Arrays.equals(fields, dataType.fields);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + Arrays.hashCode(fields);
        result = 31 * result + (int) (version ^ (version >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "DataType{" +
                "name='" + name + '\'' +
                ", fields=" + Arrays.toString(fields) +
                ", version=" + version +
                '}';
    }
}
