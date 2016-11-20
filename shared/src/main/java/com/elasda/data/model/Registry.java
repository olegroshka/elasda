package com.elasda.data.model;

import java.util.Collection;

public interface Registry {
    DataType getType(String name);
    void saveType(DataType type);

    String addTypeListener(TypeListener listener);
    boolean removeTypeListener(String id);

    Collection<Record> getData(DataType type);
    Record getData(DataType type, Key key);
    void saveData(DataType type, Record data);
    void saveData(DataType type, Collection<Record> data);

    void deleteData(DataType type, Record data);
    void deleteData(DataType type, Collection<Record> data);

    String addDataListener(String dataTypeName, RecordListener listener);
    boolean removeDataListener(String dataTypeName, String listener);
}
