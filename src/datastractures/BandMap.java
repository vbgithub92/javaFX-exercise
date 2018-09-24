package datastractures;

import Ex2.Band;

public interface BandMap {
    public void clear();
    /** Return true if the specified key is in the map */
    public boolean containsKey(String key);
    /** Return true if this map contains the specified value */
    public boolean containsValue(Band value);
    /** Return a set of entries in the map */
    public java.util.Set<BandEntry<String, Band>> entrySet();
    /** Return the first value that matches the specified key */
    public Band get(String key);
    /** Return true if this map contains no entries */
    public boolean isEmpty();
    /** Return a set consisting of the keys in this map */
    public java.util.Set<String> keySet();
    /** Add an entry (key, value) into the map */
    public Band put(String key, Band value);
    /** Remove the entries for the specified key */
    public void remove(String key);
    /** Return the number of mappings in this map */
    public int size();
    /** Return a set consisting of the values in this map */
    public java.util.Set<Band> values();
    /** Define inner class for Entry */
    public static class BandEntry<String, Band> {
        String key;
        Band value;
        public BandEntry(String key, Band value)
        { this.key = key;
            this.value = value;
        }
        public String getKey()
        { return key;
        }
        public Band getValue()
        { return value;
        }
        @Override
        public java.lang.String toString()
        { return "[" + key + ", " + value + "]";
        }
    }
}
