package com.elasda.data.processor;

import com.elasda.data.model.Record;
import com.elasda.data.model.DataType;
import com.elasda.data.model.Key;
import com.hazelcast.map.AbstractEntryProcessor;

import java.util.ConcurrentModificationException;
import java.util.Map;

public class SaveRecordProcessor extends AbstractEntryProcessor<Key, Record> {
    private final Record data;
    private final DataType type;

    public SaveRecordProcessor(DataType type, Record record) {
        super(true);
        this.data = record;
        this.type = type;
    }

    public Object process(Map.Entry<Key, Record> entry) {
        Record v = entry.getValue();
        long version = data.getVersion();
        if (v != null && version != v.getVersion()) {
            throw new ConcurrentModificationException("Stale data, type: " + type.getName() +
                    ", key: " + entry.getKey() +
                    ", current: " + v.getVersion() +
                    ", provided: " + version);
        }
        data.setVersion(version + 1);
        return entry.setValue(data);
    }
}
