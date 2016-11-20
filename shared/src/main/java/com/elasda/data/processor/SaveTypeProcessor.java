package com.elasda.data.processor;

import com.elasda.data.model.Constants;
import com.elasda.data.model.DataType;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.AbstractEntryProcessor;

import java.util.ConcurrentModificationException;
import java.util.Map;

public class SaveTypeProcessor extends AbstractEntryProcessor {
    private final DataType type;

    public SaveTypeProcessor(DataType type) {
        this.type = type;
    }

    @Override
    public Object process(Map.Entry entry) {
        DataType v = (DataType) entry.getValue();
        if( v == null ) {
            String instanceName = System.getProperty(Constants.HZ_INSTANCE_NAME);
            HazelcastInstance hz = Hazelcast.getHazelcastInstanceByName(instanceName);
            hz.getMap(type.getName());
            //todo

        } else if ( v.getVersion() != type.getVersion()) {
            throw new ConcurrentModificationException("Stale type, current version: " +
                    v.getVersion() + ", provided: " + type.getVersion());
        }
        return entry.setValue(type);
    }
}
