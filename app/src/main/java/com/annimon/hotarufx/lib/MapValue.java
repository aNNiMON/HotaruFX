package com.annimon.hotarufx.lib;

import com.annimon.hotarufx.exceptions.TypeException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

public class MapValue implements Value, Iterable<Map.Entry<String, Value>> {

    public static final MapValue EMPTY = new MapValue(1);

    public static MapValue merge(MapValue map1, MapValue map2) {
        final MapValue result = new MapValue(map1.size() + map2.size());
        result.map.putAll(map1.map);
        result.map.putAll(map2.map);
        return result;
    }

    private final Map<String, Value> map;

    public MapValue(int size) {
        this.map = new HashMap<>(size);
    }

    public MapValue(Map<String, Value> map) {
        this.map = map;
    }

    @Override
    public int type() {
        return Types.MAP;
    }

    public int size() {
        return map.size();
    }

    public Map<String, Value> getMap() {
        return map;
    }

    @Override
    public Object raw() {
        return map;
    }

    @Override
    public int asInt() {
        throw new TypeException("Cannot cast map to integer");
    }

    @Override
    public double asNumber() {
        throw new TypeException("Cannot cast map to number");
    }

    @Override
    public String asString() {
        return map.toString();
    }

    @Override
    public Iterator<Map.Entry<String, Value>> iterator() {
        return map.entrySet().iterator();
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + Objects.hashCode(this.map);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass())
            return false;
        final MapValue other = (MapValue) obj;
        return Objects.equals(this.map, other.map);
    }

    @Override
    public int compareTo(Value o) {
        if (o.type() == Types.MAP) {
            final int lengthCompare = Integer.compare(size(), ((MapValue) o).size());
            if (lengthCompare != 0) return lengthCompare;
        }
        return asString().compareTo(o.asString());
    }

    @Override
    public String toString() {
        return asString();
    }
}