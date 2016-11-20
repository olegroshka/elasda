package com.elasda.data.model;

class StringKeyFactor implements KeyFactory {
    @Override
    public Key create(Field[] keyFields, Object... keyValues) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < keyFields.length; i++) {
            Field kf = keyFields[i];
            Object v = keyValues[kf.getIndex()];
            if( v == null ) {
                throw new RuntimeException("Can't create key, field: " + kf + " is not initialized");
            }
            sb.append(v.toString());
            if (i < (keyFields.length-1)) {
                sb.append(':');
            }
        }
        return new StringKey(sb.toString());
    }
}
