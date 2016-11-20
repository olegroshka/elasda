package com.elasda.data.serializer;

import com.elasda.data.model.Record;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.StreamSerializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class RecordSerializer implements StreamSerializer<Record> {

    private static final ThreadLocal<Kryo> kryoThreadLocal = new ThreadLocal<Kryo>() {
        @Override
        protected Kryo initialValue() {
            Kryo kryo = new Kryo();
            kryo.register(Record.class);
            return kryo;
        }
    };

    public int getTypeId() {
        return 3;
    }

    public void write(ObjectDataOutput objectDataOutput, Record record) throws IOException {
        Kryo kryo = kryoThreadLocal.get();

        Output output = new Output((OutputStream) objectDataOutput);
        kryo.writeObject(output, record);
        output.flush();
    }

    public Record read(ObjectDataInput objectDataInput) throws IOException {
        InputStream in = (InputStream) objectDataInput;
        Input input = new Input(in);
        Kryo kryo = kryoThreadLocal.get();
        return kryo.readObject(input, Record.class);
    }

    public void destroy() {
    }
}
