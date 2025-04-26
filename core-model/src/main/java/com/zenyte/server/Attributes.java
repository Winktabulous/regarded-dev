package com.zenyte.server;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * @author Kryeus / John J. Woloszyk
 */
public class Attributes {

    private final Map<String, Object> attributes = new HashMap<>();

    private final Map<String, Integer> intMap = new HashMap<>();

    private final Map<String, Double> doubleMap = new HashMap<>();

    private final Map<String, Boolean> booleanMap = new HashMap<>();

    private final Map<String, Long> longMap = new HashMap<>();

    private final Map<String, String> stringMap = new HashMap<>();

    private final Map<String, List<?>> listMap = new HashMap<>();

    private final Map<String, HashSet<?>> hashSetMap = new HashMap<>();

    public Attributes() {}

    public Object get(Object key) {
        return attributes.get(key);
    }

    public Object get(Object key, Object fail) {
        Object value = attributes.get(key);
        if (value == null) {
            return fail;
        }
        return value;
    }

    public void set(String key, Object value) {
        attributes.remove(key);

        attributes.put(key, value);
    }

    public boolean contains(Object key) {
        return attributes.containsKey(key);
    }

    public void remove(Object key) {
        attributes.remove(key);
    }

    public void setInt(String key, int set) {
        intMap.put(key, set);
    }

    public int getIntAndIncr(String key, int n) {
        int num = intMap.getOrDefault(key, 0);
        intMap.put(key, num + n);
        return num;
    }

    public int getIntAndDecr(String key, int n) {
        int num = intMap.getOrDefault(key, 0);
        intMap.put(key, num - n);
        return num;
    }

    public int incrAndGetInt(String key, int n) {
        int num = intMap.getOrDefault(key, 0);
        intMap.put(key, num + n);
        return getInt(key);
    }

    public int decrAndGetInt(String key, int n) {
        int num = intMap.getOrDefault(key, 0);
        intMap.put(key, num - n);
        return getInt(key);
    }

    public void removeInt(String key) {
        intMap.remove(key);
    }

    public int getInt(String key) {
        return intMap.getOrDefault(key, -1);
    }

    public int getInt(String key, int fail) {
        return intMap.getOrDefault(key, fail);
    }

    public boolean containsInt(String key) {
        return intMap.containsKey(key);
    }

    public void setDouble(String key, double set) {
        doubleMap.put(key, set);
    }

    public void removeDouble(String key) {
        doubleMap.remove(key);
    }

    public double getDouble(String key) {
        return doubleMap.getOrDefault(key, -1d);
    }

    public double getDouble(String key, double fail) {
        return doubleMap.getOrDefault(key, fail);
    }

    public boolean containsDouble(String key) {
        return doubleMap.containsKey(key);
    }

    public void setBoolean(String key, boolean set) {
        booleanMap.put(key, set);
    }

    public boolean flipBoolean(String key) {
        if (getBoolean(key)) {
            setBoolean(key, false);
        } else {
            setBoolean(key, true);
        }

        return getBoolean(key);
    }

    public void removeBoolean(String key) {
        booleanMap.remove(key);
    }

    public boolean getBoolean(String key) {
        return booleanMap.getOrDefault(key, false);
    }

    public boolean getBoolean(String key, boolean fail) {
        return booleanMap.getOrDefault(key, fail);
    }

    public boolean containsBoolean(String key) {
        return booleanMap.containsKey(key);
    }

    public void setLong(String key, long set) {
        longMap.put(key, set);
    }

    public void removeLong(String key) {
        longMap.remove(key);
    }

    public long getLong(String key) {
        return longMap.getOrDefault(key, -1L);
    }

    public long getLong(String key, long fail) {
        return longMap.getOrDefault(key, fail);
    }

    public boolean containsLong(String key) {
        return longMap.containsKey(key);
    }

    public void setString(String key, String set) {
        stringMap.put(key, set);
    }

    public void removeString(String key) {
        stringMap.remove(key);
    }

    public String getString(String key) {
        return stringMap.getOrDefault(key, null);
    }

    public String getString(String key, String fail) {
        return stringMap.getOrDefault(key, fail);
    }

    public List<?> getList(String key) {
        return listMap.get(key);
    }

    public List<?> getList(String key, List fail) {
        return listMap.getOrDefault(key, fail);
    }

    public void setList(String key, List list) {
        listMap.put(key, list);
    }

    public HashSet<?> getHashSet(String key) {
        return hashSetMap.get(key);
    }

    public void setHashSet(String key, HashSet hashSet) {
        hashSetMap.put(key, hashSet);
    }

    public boolean containsString(String key) {
        return stringMap.containsKey(key);
    }
}

