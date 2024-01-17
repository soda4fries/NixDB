package org.NixDB.Datastructures;
//for the for each loop
import java.util.*;

@SuppressWarnings("unchecked")
public class MyHashTable<K, V> implements Iterable<K> {

    private static final int DEFAULT_CAPACITY = 3;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;

    private final double maxLoadFactor;
    private int capacity, threshold, size = 0;
    private MyLinkedList<Entry<K, V>>[] table;

    public MyHashTable() {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }


    static class Entry<K, V> {

        int hash;
        K key;
        V value;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
            this.hash = key.hashCode();
        }


        public boolean equals(Entry<K, V> other) {
            if (hash != other.hash) return false;
            return key.equals(other.key);
        }

        @Override
        public String toString() {
            return key + " => " + value;
        }
    }

    public MyHashTable(int capacity) {
        this(capacity, DEFAULT_LOAD_FACTOR);
    }


    public MyHashTable(int capacity, double maxLoadFactor) {
        if (capacity < 0) throw new IllegalArgumentException("Illegal capacity");
        if (maxLoadFactor <= 0 || Double.isNaN(maxLoadFactor) || Double.isInfinite(maxLoadFactor))
            throw new IllegalArgumentException("Illegal maxLoadFactor");
        this.maxLoadFactor = maxLoadFactor;
        this.capacity = Math.max(DEFAULT_CAPACITY, capacity);
        threshold = (int) (this.capacity * maxLoadFactor);
        table = new MyLinkedList[this.capacity];
    }


    public int size() {
        return size;
    }


    public boolean isEmpty() {
        return size == 0;
    }


    private int normalizeIndex(int keyHash) {
        return (keyHash & 0x7FFFFFFF) % capacity;
    }


    public void clear() {
        Arrays.fill(table, null);
        size = 0;
    }

    public boolean containsKey(K key) {
        return hasKey(key);
    }


    public boolean hasKey(K key) {
        int bucketIndex = normalizeIndex(key.hashCode());
        return bucketSeekEntry(bucketIndex, key) != null;
    }


    public V put(K key, V value) {
        return insert(key, value);
    }

    public V add(K key, V value) {
        return insert(key, value);
    }

    public V insert(K key, V value) {

        if (key == null) throw new IllegalArgumentException("Null key");
        Entry<K, V> newEntry = new Entry<>(key, value);
        int bucketIndex = normalizeIndex(newEntry.hash);
        return bucketInsertEntry(bucketIndex, newEntry);
    }


    public V get(K key) {

        if (key == null) return null;
        int bucketIndex = normalizeIndex(key.hashCode());
        Entry<K, V> entry = bucketSeekEntry(bucketIndex, key);
        if (entry != null) return entry.value;
        return null;
    }


    public V remove(K key) {

        if (key == null) return null;
        int bucketIndex = normalizeIndex(key.hashCode());
        return bucketRemoveEntry(bucketIndex, key);
    }

    private V bucketRemoveEntry(int bucketIndex, K key) {

        Entry<K, V> entry = bucketSeekEntry(bucketIndex, key);
        if (entry != null) {
            MyLinkedList<Entry<K, V>> links = table[bucketIndex];
            links.remove(entry);
            --size;
            return entry.value;
        } else return null;
    }


    private V bucketInsertEntry(int bucketIndex, Entry<K, V> entry) {

        MyLinkedList<Entry<K, V>> bucket = table[bucketIndex];
        if (bucket == null) table[bucketIndex] = bucket = new MyLinkedList<>();

        Entry<K, V> existentEntry = bucketSeekEntry(bucketIndex, entry.key);
        if (existentEntry == null) {
            bucket.add(entry);
            if (++size > threshold) resizeTable();
            return null; // Use null to indicate that there was no previous entry
        } else {
            V oldVal = existentEntry.value;
            existentEntry.value = entry.value;
            return oldVal;
        }
    }

    private Entry<K, V> bucketSeekEntry(int bucketIndex, K key) {

        if (key == null) return null;
        MyLinkedList<Entry<K, V>> bucket = table[bucketIndex];
        if (bucket == null) return null;
        for (Entry<K, V> entry : bucket) if (entry.key.equals(key)) return entry;
        return null;
    }


    private void resizeTable() {

        capacity *= 2;
        threshold = (int) (capacity * maxLoadFactor);

        MyLinkedList<Entry<K, V>>[] newTable = new MyLinkedList[capacity];

        for (int i = 0; i < table.length; i++) {
            if (table[i] != null) {

                for (Entry<K, V> entry : table[i]) {
                    int bucketIndex = normalizeIndex(entry.hash);
                    MyLinkedList<Entry<K, V>> bucket = newTable[bucketIndex];
                    if (bucket == null) newTable[bucketIndex] = bucket = new MyLinkedList<>();
                    bucket.add(entry);
                }


                table[i].clear();
                table[i] = null;
            }
        }

        table = newTable;
    }


    public List<K> keys() {

        List<K> keys = new ArrayList<>(size());
        for (MyLinkedList<Entry<K, V>> bucket : table)
            if (bucket != null) for (Entry<K, V> entry : bucket) keys.add(entry.key);
        return keys;
    }

    public List<V> values() {

        List<V> values = new ArrayList<>(size());
        for (MyLinkedList<Entry<K, V>> bucket : table)
            if (bucket != null) for (Entry<K, V> entry : bucket) values.add(entry.value);
        return values;
    }
    @Override
    public java.util.Iterator<K> iterator() {
        final int elementCount = size();
        return new java.util.Iterator<>() {

            int bucketIndex = 0;
            java.util.Iterator<Entry<K, V>> bucketIter = (table[0] == null) ? null : table[0].iterator();

            @Override
            public boolean hasNext() {


                if (elementCount != size) throw new java.util.ConcurrentModificationException();


                if (bucketIter == null || !bucketIter.hasNext()) {


                    while (++bucketIndex < capacity) {
                        if (table[bucketIndex] != null) {


                            java.util.Iterator<Entry<K, V>> nextIter = table[bucketIndex].iterator();
                            if (nextIter.hasNext()) {
                                bucketIter = nextIter;
                                break;
                            }
                        }
                    }
                }
                return bucketIndex < capacity;
            }

            @Override
            public K next() {
                return bucketIter.next().key;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override
    public String toString() {
        int count=0;
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (int i = 0; i < capacity; i++) {
            if (table[i] == null) continue;
            for (Entry<K, V> entry : table[i]) {
                sb.append(entry).append("\n, ");
                count++;
            }
        }
        sb.append("}\nCount: ").append(count);
        return sb.toString();
    }

    public static void main(String[] args) {
        MyHashTable<String, Integer> test = new MyHashTable<>();

        Random rand = new Random();
        for (int i = 0; i < 65; i++) {
            test.add(Integer.toString(i), i+rand.nextInt(100));
        }

        System.out.println(test.remove("25"));
        System.out.println(test.hasKey("24"));
        for (String s : test) {
            System.out.println(s);
        }

        System.out.println(test);




    }
}