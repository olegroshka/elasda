package com.elasda.node;

import com.elasda.data.model.*;
import com.elasda.data.serializer.RecordSerializer;
import com.hazelcast.config.Config;
import com.hazelcast.config.SerializerConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.Properties;

import static com.elasda.data.model.Constants.HZ_INSTANCE_NAME;

public class Node {

    private static final Logger LOG = LoggerFactory.getLogger(Node.class);

    public static void main(String[] args) {
        Config config = new Config();

        SerializerConfig productSerializer =
                new SerializerConfig()
                        .setTypeClass(Record.class)
                        .setImplementation(new RecordSerializer());

        config.getSerializationConfig().addSerializerConfig(productSerializer);

        HazelcastInstance h = Hazelcast.newHazelcastInstance(config);
        String name = h.getName();

        Properties properties = System.getProperties();
        properties.setProperty(HZ_INSTANCE_NAME, name);
        IMap<String, Properties> appProperties = h.getMap("elasda.node.configuration");
        appProperties.put(name, properties);

        IMap<String, DataType> types = h.getMap(Constants.TYPES);
        DataType usersType = new DataType("users", new Field[]{new Field("userName", FieldType.TEXT, true, 0)});
        types.putIfAbsent("users", usersType);
        IMap<Object, Object> users = h.getMap("users");
        Record admin = usersType.newDataRow();
        admin.setValue(0, "admin");

        users.put(usersType.newKey(admin), admin);

        LOG.info("Node has started: properties for k=" + name + "  " + appProperties.get(name));
    }
}
