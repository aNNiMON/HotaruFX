package com.annimon.hotarufx.lib;

import com.annimon.hotarufx.exceptions.TypeException;
import java.util.Arrays;
import java.util.Iterator;
import lombok.Getter;

public class ArrayValue implements Value, Iterable<Value> {

    @Getter
    private final Value[] elements;

    public ArrayValue(int size) {
        this.elements = new Value[size];
    }

    public ArrayValue(Value[] elements) {
        this.elements = new Value[elements.length];
        System.arraycopy(elements, 0, this.elements, 0, elements.length);
    }

    public ArrayValue(ArrayValue array) {
        this(array.elements);
    }

    public Value[] getCopyElements() {
        final Value[] result = new Value[elements.length];
        System.arraycopy(elements, 0, result, 0, elements.length);
        return result;
    }

    @Override
    public int type() {
        return Types.ARRAY;
    }

    public int size() {
        return elements.length;
    }

    public Value get(int index) {
        return elements[index];
    }

    public void set(int index, Value value) {
        elements[index] = value;
    }

    @Override
    public Object raw() {
        return elements;
    }

    @Override
    public int asInt() {
        throw new TypeException("Cannot cast array to integer");
    }

    @Override
    public double asNumber() {
        throw new TypeException("Cannot cast array to number");
    }

    @Override
    public String asString() {
        return Arrays.toString(elements);
    }

    @Override
    public Iterator<Value> iterator() {
        return Arrays.asList(elements).iterator();
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + Arrays.deepHashCode(this.elements);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass())
            return false;
        final ArrayValue other = (ArrayValue) obj;
        return Arrays.deepEquals(this.elements, other.elements);
    }

    @Override
    public int compareTo(Value o) {
        if (o.type() == Types.ARRAY) {
            final int lengthCompare = Integer.compare(size(), ((ArrayValue) o).size());
            if (lengthCompare != 0) return lengthCompare;
        }
        return asString().compareTo(o.asString());
    }

    @Override
    public String toString() {
        return asString();
    }
}