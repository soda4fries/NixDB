package org.NixDB.Datastructures;

import java.util.BitSet;

public class MyBloomFilter<E> {
    BitSet bloomFilter;
    int size;


    MyBloomFilter(int size) {
        this.bloomFilter = new BitSet(size);
        this.size = size;
    }

    public void put(E item) {
        int hashCode = item.hashCode()%size;
        bloomFilter.set(hashCode, true);
    }

    public boolean contains(E item) {
        int hashCode = item.hashCode()%size;
        return bloomFilter.get(hashCode) == true;
    }
}
