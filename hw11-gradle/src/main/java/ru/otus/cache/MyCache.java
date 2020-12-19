package ru.otus.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class MyCache<K, V> implements HwCache<K, V> {
    //Надо реализовать эти методы
    private static final Logger logger = LoggerFactory.getLogger(MyCache.class);
    private final String PUT = "put";
    private final String REMOVE = "remove";
    private final Set<HwListener<K, V>> listeners = Collections.newSetFromMap(new WeakHashMap<>());
    private final Map<K, V> values = new WeakHashMap<>();

    @Override
    public void put(K key, V value) {
        values.put(key, value);
        notifyListeners(key, value, PUT);
    }

    @Override
    public void remove(K key) {
        if(values.containsKey(key)){
            var value = values.remove(key);
            notifyListeners(key, value, REMOVE);
        }
    }

    @Override
    public V get(K key) {
        return values.get(key);
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.remove(listener);
    }

    private void notifyListeners(K key, V value, String action) {
        listeners.forEach(listener -> {
            try {
                listener.notify(key, value, action);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        });
    }
}