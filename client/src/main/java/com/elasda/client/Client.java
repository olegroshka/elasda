package com.elasda.client;

import com.elasda.data.model.Record;
import com.elasda.data.model.Registry;
import com.elasda.data.serializer.RecordSerializer;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.config.*;
import com.hazelcast.core.HazelcastInstance;

import java.util.HashMap;
import java.util.Properties;

public final class Client {

    private final HazelcastInstance hz;
    private final Registry registry;

    private Client(HazelcastInstance client) {
        this.hz = client;
        this.registry = new RegistryImpl(hz);
    }

    public static Client newClient() {
        ClientConfig config = new ClientConfig();

        NearCacheConfig nearCacheConfig = new NearCacheConfig();
        nearCacheConfig.setInMemoryFormat(InMemoryFormat.OBJECT);
        EvictionConfig evictionConfig = new EvictionConfig();
        evictionConfig.setEvictionPolicy(EvictionPolicy.NONE);
        nearCacheConfig.setEvictionConfig(evictionConfig);
        nearCacheConfig.setLocalUpdatePolicy(NearCacheConfig.LocalUpdatePolicy.CACHE);
        nearCacheConfig.setCacheLocalEntries(true);
        nearCacheConfig.setInvalidateOnChange(true);

        HashMap<String, NearCacheConfig> nearCacheConfigMap = new HashMap<>();
        nearCacheConfigMap.put("*", nearCacheConfig);
        config.setNearCacheConfigMap(nearCacheConfigMap);

        SerializerConfig recordSerializer =
                new SerializerConfig()
                        .setTypeClass(Record.class)
                        .setImplementation(new RecordSerializer());

        config.getSerializationConfig().addSerializerConfig(recordSerializer);

        return new Client(HazelcastClient.newHazelcastClient(config));
    }

    public Registry getRegistry() {
        return registry;
    }

}
