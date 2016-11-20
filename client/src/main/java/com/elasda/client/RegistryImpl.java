package com.elasda.client;

import com.elasda.data.model.*;
import com.elasda.data.processor.*;
import com.hazelcast.core.EntryEvent;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.core.MapEvent;
import com.hazelcast.map.impl.MapListenerAdapter;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

class RegistryImpl implements Registry {

    private final HazelcastInstance hz;
    private final IMap<String, DataType> types;

    public RegistryImpl(HazelcastInstance hz) {
        this.hz = hz;
        this.types = hz.getMap(Constants.TYPES);
    }

    public DataType getType(String name) {
        if( name == null || name.length() == 0) {
            throw new IllegalArgumentException("Type name not provided");
        }
        return types.get(name);
    }

    public void saveType(DataType type) {
        if( type.getName() == null || type.getName().length() == 0) {
            throw new IllegalArgumentException("Type name not provided: " + type);
        }
        types.executeOnKey(type.getName(), new SaveTypeProcessor(type));
    }

    public String addTypeListener(final TypeListener listener) {
        return types.addEntryListener(new DataMapListenerAdapter(listener), true);
    }

    public boolean removeTypeListener(String id) {
        return types.removeEntryListener(id);
    }

    public Collection<Record> getData(DataType type) {
        return getDataCache(type.getName()).values();
    }

    public Record getData(DataType type, Key key) {
        return getDataCache(type.getName()).get(key);
    }

    public void saveData(DataType type, Collection<Record> data) {
        Map<Key, Record> map = data.stream().collect(Collectors.toMap(d -> type.newKey(d), Function.identity()));
        final IMap<Key, Record> cache = getDataCache(type.getName());
        Map<Key, Object> result = cache.executeOnKeys(map.keySet(), new SaveRecordsProcessor(type, map));

        Optional<Object> option = result.values().stream().findAny().filter(o -> o instanceof Exception);
        if( option.isPresent()) {
            throw new RuntimeException("Could not save all data", (Exception) option.get());
        }
    }

    @Override
    public void deleteData(DataType type, Record data) {
        final IMap<Key, Record> cache = getDataCache(type.getName());
        Object result = cache.executeOnKey(type.newKey(data), new DeleteRecordProcessor(type, data));

        if( result instanceof RuntimeException) {
            throw (RuntimeException) result;
        }
    }

    @Override
    public void deleteData(DataType type, Collection<Record> data) {
        Map<Key, Record> map = data.stream().collect(Collectors.toMap(d -> type.newKey(d), Function.identity()));
        final IMap<Key, Record> cache = getDataCache(type.getName());
        Map<Key, Object> result = cache.executeOnKeys(map.keySet(), new DeleteRecordsProcessor(type, map));

        Optional<Object> option = result.values().stream().findAny().filter(o -> o instanceof Exception);
        if( option.isPresent()) {
            throw new RuntimeException("Could not save all data", (Exception) option.get());
        }

    }

    public void saveData(DataType type, Record data) {
        final IMap<Key, Record> cache = getDataCache(type.getName());
        Object result = cache.executeOnKey(type.newKey(data), new SaveRecordProcessor(type, data));

        if( result instanceof RuntimeException) {
            throw (RuntimeException) result;
        }
    }

    public String addDataListener(String dataTypeName, RecordListener listener) {
        IMap<Key, Record> map = getDataCache(dataTypeName);
        return map.addEntryListener(new DataMapListenerAdapter(listener), true);
    }

    private IMap<Key, Record> getDataCache(String dataTypeName) {
        IMap<Key, Record> map = hz.getMap(dataTypeName);
        if( map == null ) {
            throw new RuntimeException("Can't find data for: " + dataTypeName);
        }
        return map;
    }

    public boolean removeDataListener(String dataTypeName, String id) {
        return getDataCache(dataTypeName).removeEntryListener(id);
    }

    private static class DataMapListenerAdapter<K, V> extends MapListenerAdapter<K, V> {
        private final Listener<K, V> listener;

        public DataMapListenerAdapter(Listener<K, V> listener) {
            this.listener = listener;
        }

        @Override
        public void onEntryEvent(EntryEvent<K, V> event) {
            switch (event.getEventType()) {
                case ADDED:
                case UPDATED:
                    listener.onSave(event.getKey(), event.getValue());
                    break;
                case REMOVED:
                    listener.onDelete(event.getKey(), event.getOldValue());
                    break;
                default:
                    throw new RuntimeException("Unexpected type event " + event);
            }
        }

        @Override
        public void onMapEvent(MapEvent event) {
            System.out.println("type map event: " + event);
        }
    }
}
