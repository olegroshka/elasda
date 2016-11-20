package com.elasda.data.model;

interface KeyFactory {
    Key create(Field[] keyFields, Object... keyValues);
}
