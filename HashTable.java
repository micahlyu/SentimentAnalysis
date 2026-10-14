/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author micah
 */
import java.util.LinkedList;

public class HashTable<Key, Value> {

    // One entry stored in a bucket
    private static class Entry<K, V> {
        K key;
        V value;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private LinkedList<Entry<Key, Value>>[] buckets;
    private int capacity;
    private int size;
    private static final double LOAD_FACTOR_THRESHOLD = 0.5;

    @SuppressWarnings("unchecked")
    public HashTable(int initialCapacity) {
        this.capacity = initialCapacity;
        this.size = 0;

        buckets =
                (LinkedList<Entry<Key, Value>>[])
                        new LinkedList[capacity];

        // Initialize every bucket with an empty linked list
        for (int i = 0; i < capacity; i++) {
            buckets[i] = new LinkedList<>();
        }
    }

    /**
     * Insert or update a key-value pair.
     * Rehashes when the table would become more than half full.
     */
    public void put(Key key, Value value) {
        int index = hash(key);

        // If the key already exists, update its value.
        for (Entry<Key, Value> entry : buckets[index]) {
            if (entry.key.equals(key)) {
                entry.value = value;
                return;
            }
        }

        // Rehash before inserting if the new item
        // would make the table more than half full.
        if ((size + 1.0) / capacity
                > LOAD_FACTOR_THRESHOLD) {

            rehash();

            // Recalculate because the capacity changed.
            index = hash(key);
        }

        buckets[index].add(new Entry<>(key, value));
        size++;
    }

    /**
     * Convenience alias used by some versions
     * of the assignment.
     */
    public void insert(Key key, Value value) {
        put(key, value);
    }

    /**
     * Return the value associated with key,
     * or null if absent.
     */
    public Value get(Key key) {
        int index = hash(key);

        for (Entry<Key, Value> entry
                : buckets[index]) {

            if (entry.key.equals(key)) {
                return entry.value;
            }
        }

        return null;
    }

    /**
     * Alias for get() to match the original
     * HashTable API.
     */
    public Value lookup(Key key) {
        return get(key);
    }

    /**
     * Double the number of buckets and
     * rehash every existing entry.
     */
    @SuppressWarnings("unchecked")
    private void rehash() {

        int newCapacity = capacity * 2;

        LinkedList<Entry<Key, Value>>[] newBuckets =
                (LinkedList<Entry<Key, Value>>[])
                        new LinkedList[newCapacity];

        // Initialize all new buckets.
        for (int i = 0; i < newCapacity; i++) {
            newBuckets[i] = new LinkedList<>();
        }

        // Move every existing entry into its
        // new bucket using the new capacity.
        for (LinkedList<Entry<Key, Value>> bucket
                : buckets) {

            for (Entry<Key, Value> entry : bucket) {

                int newIndex =
                        Math.floorMod(
                                entry.key.hashCode(),
                                newCapacity);

                newBuckets[newIndex].add(entry);
            }
        }

        buckets = newBuckets;
        capacity = newCapacity;
    }

    /**
     * Calculate the bucket index for a key.
     */
    private int hash(Key key) {
        return Math.floorMod(
                key.hashCode(),
                capacity);
    }

    public int size() {
        return size;
    }

    public int capacity() {
        return capacity;
    }
}
