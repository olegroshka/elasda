package com.elasda.data.processor;

import com.elasda.data.model.Record;
import com.elasda.data.model.DataType;
import com.elasda.data.model.Key;
import com.hazelcast.map.AbstractEntryProcessor;

import java.util.ConcurrentModificationException;
import java.util.Map;

public class DeleteRecordProcessor extends AbstractEntryProcessor<Key, Record> {
    private final Record data;
    private final DataType type;

    public DeleteRecordProcessor(DataType type, Record data) {
        super(true);
        this.data = data;
        this.type = type;
    }

    public Object process(Map.Entry<Key, Record> entry) {
        Record v = entry.getValue();
        if (v != null && data.getVersion() != v.getVersion()) {
            throw new ConcurrentModificationException("Stale data, type: " + type.getName() +
                    ", key: " + entry.getKey() +
                    ", current: " + v.getVersion() +
                    ", provided: " + data.getVersion());
        }
        return entry.setValue(null);
    }
}
