package com.elasda.client;

import com.elasda.data.model.*;

import java.util.Properties;

public class Example {
    public static void main(String[] args) {
        Client client = Client.newClient();
        Registry registry = client.getRegistry();
        registry.addTypeListener(new TypeListener() {
            @Override
            public void onSave(String s, DataType type) {
                System.out.println("type saved: " + type);
            }

            @Override
            public void onDelete(String s, DataType type) {
                System.out.println("type deleted: " + type);
            }
        });

        DataType testType = registry.getType("test");
        if( testType == null ) {
            testType = new DataType("test", new Field[]{
                    new Field("field1", FieldType.TEXT, true,  0),
                    new Field("field2", FieldType.TEXT, false, 1),
                    new Field("field3", FieldType.TEXT, false, 2),
                    new Field("field4", FieldType.DOUBLE, false, 3),
                    new Field("field5", FieldType.BOOLEAN, false, 4),
            });
            registry.saveType(testType);
        }

        registry.addDataListener("test", new RecordListener() {
            @Override
            public void onSave(Key key, Record record) {
                if(record.getVersion() == 0 ) {
                    System.out.println("added record, key=" + key + ", row=" + record);
                } else {
                    System.out.println("updated record, key=" + key + ", row=" + record);
                }
            }

            @Override
            public void onDelete(Key key, Record record) {
                System.out.println("deleted data, key=" + key + ", row=" + record);
            }
        });

        Field field1 = testType.getField("field1");
        Field field2 = testType.getField("field2");
        Field field3 = testType.getField("field3");
        Field field4 = testType.getField("field4");
        Field field5 = testType.getField("field5");
        int savedRowCount = registry.getData(testType).size();

        for (int i = savedRowCount; i < savedRowCount + 100; i++) {
            Record record = testType.newDataRow();
            record.setValue(field1.getIndex(), "fld1="+i);
            record.setValue(field2.getIndex(), "fld2="+i);
            record.setValue(field3.getIndex(), "fld3="+i);
            record.setValue(field4.getIndex(), (double)i);
            record.setValue(field5.getIndex(), i%2==0);

            registry.saveData(testType, record);
        }

        for (int i = savedRowCount; i < savedRowCount + 100; i++) {
            Record data = registry.getData(testType, testType.newKey("fld1=" + i));
            data.setValue(1, data.getValue(1) + "_zzz");
            registry.saveData(testType, data);
        }

        for (int i = savedRowCount; i < savedRowCount + 100; i++) {
            Record data = registry.getData(testType, testType.newKey("fld1=" + i));
            registry.deleteData(testType, data);
        }

    }
}
