package com.elasda.data.model;

public interface Listener<K, V> {
    void onSave(K k, V v);
    void onDelete(K k, V v);
}
