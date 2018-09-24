package datastractures;

import Ex2.Band;

import java.util.*;

public class BandsHashMap implements BandMap{
    // Define the default hash table size. Must be a power of 2
    private static int DEFAULT_INITIAL_CAPACITY = 4;
    // Define the maximum hash table size. 1 << 30 is same as 2^30
    private static int MAXIMUM_CAPACITY = 1 << 30;
    // Current hash table capacity. Capacity is a power of 2
    private int capacity;
    // Define default load factor
    private static float DEFAULT_MAX_LOAD_FACTOR = 0.75f;
    // Specify a load factor used in the hash table
    private float loadFactorThreshold;
    // The number of entries in the map
    private int size = 0;
    // Hash table is an array with each cell that is a linked list
    LinkedList<BandMap.BandEntry<String, Band>>[] table;
    /** Construct a map with the default capacity and load factor */
    public BandsHashMap()
    { this(DEFAULT_INITIAL_CAPACITY, DEFAULT_MAX_LOAD_FACTOR);
    }
    /** Construct a map with the specified initial capacity and 
     * default load factor */
    public BandsHashMap(int initialCapacity)
    { this(initialCapacity, DEFAULT_MAX_LOAD_FACTOR);
    }
    /** Construct a map with the specified initial capacity 
     * and load factor */
    public BandsHashMap(int initialCapacity, float loadFactorThreshold)
    { if (initialCapacity > MAXIMUM_CAPACITY)
        this.capacity = MAXIMUM_CAPACITY;
    else
        this.capacity = trimToPowerOf2(initialCapacity);
        this.loadFactorThreshold = loadFactorThreshold;
        table = new LinkedList[capacity];
    }
    @Override /** Remove all of the entries from this map */
    public void clear()
    { size = 0;
        removeEntries();
    }
    @Override /** Return true if the specified key is in the map */
    public boolean containsKey(String key)
    { return get(key) != null;
    }
    @Override /** Return true if this map contains the value */
    public boolean containsValue(Band value)
    { for (int i = 0; i < capacity; i++)
    { if (table[i] != null)
    { LinkedList<BandMap.BandEntry<String, Band>> bucket = table[i];
        for (BandMap.BandEntry<String, Band> BandEntry: bucket)
            if (BandEntry.getValue().equals(value))
                return true;
    }
    }
        return false;
    }
    @Override /** Return a set of entries in the map */
    public java.util.Set<BandMap.BandEntry<String, Band>> entrySet()
    { java.util.Set<BandMap.BandEntry<String, Band>> set =
            new java.util.HashSet<BandMap.BandEntry<String, Band>>();
        for (int i = 0; i < capacity; i++)
        { if (table[i] != null) {
            LinkedList<BandMap.BandEntry<String, Band>> bucket = table[i];
            for (BandMap.BandEntry<String, Band> BandEntry: bucket)
                set.add(BandEntry);
        }
        }
        return set;
    }
    @Override /** Return the value that matches the specified key */
    public Band get(String key)
    { int bucketIndex = hash(key.hashCode());
        if (table[bucketIndex] != null)
        { LinkedList<BandMap.BandEntry<String, Band>> bucket = table[bucketIndex];
            for (BandMap.BandEntry<String, Band> BandEntry: bucket)
                if (BandEntry.getKey().equals(key))
                    return BandEntry.getValue();
        }
        return null;
    }
    @Override /** Return true if this map contains no entries */
    public boolean isEmpty()
    { return size == 0;
    }
    @Override /** Return a set consisting of the keys in this map */
    public java.util.Set<String> keySet()
    { java.util.Set<String> set = new java.util.HashSet<String>();
        for (int i = 0; i < capacity; i++)
        { if (table[i] != null)
        { LinkedList<BandMap.BandEntry<String, Band>> bucket = table[i];
            for (BandMap.BandEntry<String, Band> BandEntry: bucket)
                set.add(BandEntry.getKey());
        }
        }
        return set;
    }
    @Override /** Add an BandEntry (key, value) into the map */
    public Band put(String key, Band value)
    { if (get(key) != null)
    { // The key is already in the map
        int bucketIndex = hash(key.hashCode());
        LinkedList<BandMap.BandEntry<String, Band>> bucket = table[bucketIndex];
        for (BandMap.BandEntry<String, Band> BandEntry: bucket)
            if (BandEntry.getKey().equals(key))
            {Band oldValue = BandEntry.getValue();
                // Replace old value with new value
                BandEntry.value = value; // update BandEntry by iterator
                // Return the old value for the key
                return oldValue;
            }
    }
        // Check load factor
        if (size >= capacity * loadFactorThreshold)
        { if (capacity == MAXIMUM_CAPACITY)
            throw new RuntimeException("Exceeding maximum capacity");
            rehash();
        }
        int bucketIndex = hash(key.hashCode());
        // Create a linked list for the bucket if it is not created
        if (table[bucketIndex] == null)
        {  table[bucketIndex] = new LinkedList<BandEntry<String, Band>>();
        }
        // Add a new BandEntry (key, value) to hashTable[index]
        table[bucketIndex].add(new BandMap.BandEntry<String, Band>(key, value));
        size++; // Increase size
        return value;
    }
    @Override /** Remove the entries for the specified key */
    public void remove(String key) {
        int bucketIndex = hash(key.hashCode());
        // Remove the first BandEntry that matches the key from a bucket
        if (table[bucketIndex] != null)
        { LinkedList<BandMap.BandEntry<String, Band>> bucket = table[bucketIndex];
            for (BandMap.BandEntry<String, Band> BandEntry: bucket)
                if (BandEntry.getKey().equals(key))
                { bucket.remove(BandEntry);
                    size--; // Decrease size
                    break; // Remove just one BandEntry that matches the key
                }
        }
    }
    @Override /** Return the number of entries in this map */
    public int size()
    { return size;
    }
    @Override /** Return a set consisting of the values in this map */
    public java.util.Set<Band> values()
    { java.util.Set<Band> set = new java.util.HashSet<Band>();
        for (int i = 0; i < capacity; i++) {
            if (table[i] != null)
            { LinkedList<BandEntry<String, Band>> bucket = table[i];
                for (BandEntry<String, Band> BandEntry: bucket)
                    set.add(BandEntry.getValue());
            }
        }
        return set;
    }
    /** Hash function */
    private int hash(int hashCode)
    { return supplementalHash(hashCode) & (capacity - 1);
    }
    /** Ensure the hashing is evenly distributed */
    private static int supplementalHash(int h)
    { h ^= (h >>> 20) ^ (h >>> 12);
        return h ^ (h >>> 7) ^ (h >>> 4);
    }
    /** Return a power of 2 for initialCapacity */
    private int trimToPowerOf2(int initialCapacity)
    { int capacity = 1;
        while (capacity < initialCapacity)
        { capacity <<= 1;
        }
        return capacity;
    }
    /** Remove all entries from each bucket */
    private void removeEntries()
    { for (int i = 0; i < capacity; i++)
    { if (table[i] != null)
    { table[i].clear();
    }
    }
    }
    /** Rehash the map */
    private void rehash()
    { java.util.Set<BandMap.BandEntry<String, Band>> set = entrySet(); // Get entries
        capacity <<= 1; // Double capacity    
        table = new LinkedList[capacity]; // Create a new hash table
        size = 0; // Reset size to 0
        for (BandMap.BandEntry<String, Band> BandEntry: set)
        {  put(BandEntry.getKey(), BandEntry.getValue()); // Store to new table
        }
    }
    @Override
    public String toString()
    { StringBuilder builder = new StringBuilder("[");
        for (int i = 0; i < capacity; i++)
        {  if (table[i] != null && table[i].size() > 0)
            for (BandEntry<String, Band> BandEntry: table[i])
                builder.append(BandEntry);
        }
        builder.append("]");
        return builder.toString();
    }
}
