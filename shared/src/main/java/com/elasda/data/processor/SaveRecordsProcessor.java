package com.elasda.data.processor;

import com.elasda.data.model.Record;
import com.elasda.data.model.DataType;
import com.elasda.data.model.Key;
import com.hazelcast.map.AbstractEntryProcessor;

import java.util.ConcurrentModificationException;
import java.util.Map;

public class SaveRecordsProcessor extends AbstractEntryProcessor<Key, Record> {
    private final Map<Key, Record> map;
    private final DataType type;

    public SaveRecordsProcessor(DataType type, Map<Key, Record> map) {
        super(true);
        this.map = map;
        this.type = type;
    }

    public Object process(Map.Entry<Key, Record> entry) {
        Record record = map.get(entry.getKey());
        long version = record.getVersion();
        if (version != entry.getValue().getVersion()) {
            throw new ConcurrentModificationException("Stale data, type: " + type.getName() +
                    ", key: " + entry.getKey() +
                    ", current: " + entry.getValue().getVersion() +
                    ", provided: " + version);
        }
        record.setVersion(version + 1);
        return entry.setValue(record);
    }
}
