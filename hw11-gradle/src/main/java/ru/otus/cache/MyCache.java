package ru.otus.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class MyCache<K, V> implements HwCache<K, V> {
    public class WHMReferenceKey<K> {
        private final K key;

        public WHMReferenceKey(K key) {
            this.key = key;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            WHMReferenceKey<?> that = (WHMReferenceKey<?>) o;
            return key.equals(that.key);
        }

        @Override
        public int hashCode() {
            return Objects.hash(key);
        }
    }

    //Надо реализовать эти методы
    private static final Logger logger = LoggerFactory.getLogger(MyCache.class);
    private static final String PUT = "put";
    private static final String REMOVE = "remove";
    private final Set<HwListener<K, V>> listeners = Collections.newSetFromMap(new WeakHashMap<>());
    private final Map<WHMReferenceKey<K>, V> values = new WeakHashMap<>();

    @Override
    public void put(K key, V value) {
        values.put(new WHMReferenceKey<>(key), value);
        notifyListeners(key, value, PUT);
    }

    @Override
    public void remove(K key) {
        if (values.containsKey(new WHMReferenceKey<>(key))) {
            var value = values.remove(new WHMReferenceKey<>(key));
            notifyListeners(key, value, REMOVE);
        }
    }

    @Override
    public V get(K key) {
        if (values.containsKey(new WHMReferenceKey<>(key))) {
            return values.get(new WHMReferenceKey<>(key));
        }

        return null;
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