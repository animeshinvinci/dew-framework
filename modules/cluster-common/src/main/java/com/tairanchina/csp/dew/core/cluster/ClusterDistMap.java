package com.tairanchina.csp.dew.core.cluster;

import java.util.Map;
import java.util.function.Consumer;

public interface ClusterDistMap<M> {

    void put(String key, M value);

    void putAsync(String key, M value);

    void putIfAbsent(String key, M value);

    boolean containsKey(String key);

    Map<String, M> getAll();

    M get(String key);

    void remove(String key);

    void removeAsync(String key);

    void clear();



    /**
     * 针对hazelcast，在插入的时候打印日志
     * @param fun
     * @return
     */
    default ClusterDistMap<M> regEntryAddedEvent(Consumer<EntryEvent<M>> fun) {
        return this;
    }

    /**
     * 针对hazelcast，
     * @param fun
     * @return
     */
    default ClusterDistMap<M> regEntryRemovedEvent(Consumer<EntryEvent<M>> fun) {
        return this;
    }

    /**
     * 针对hazelcast，
     * @param fun
     * @return
     */
    default ClusterDistMap<M> regEntryUpdatedEvent(Consumer<EntryEvent<M>> fun) {
        return this;
    }

    /**
     * 针对hazelcast，
     * @param fun
     * @return
     */
    default ClusterDistMap<M> regMapClearedEvent(VoidProcessFun fun) {
        return this;
    }

    //V即M
    class EntryEvent<V> {
        private String key;
        private V oldValue;
        private V value;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public V getOldValue() {
            return oldValue;
        }

        public void setOldValue(V oldValue) {
            this.oldValue = oldValue;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }

    }
}
