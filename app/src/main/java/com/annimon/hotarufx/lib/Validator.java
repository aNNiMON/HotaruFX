package com.annimon.hotarufx.lib;

import com.annimon.hotarufx.exceptions.ArgumentsMismatchException;
import com.annimon.hotarufx.exceptions.TypeException;

public class Validator {

    public static Validator with(Value[] args) {
        return new Validator(args);
    }

    private final Value[] args;

    private Validator(Value[] args) {
        this.args = args;
    }

    public Validator check(int expected) {
        if (args.length != expected) throw new ArgumentsMismatchException(String.format(
                "%d %s expected, got %d", expected, pluralize(expected), args.length));
        return this;
    }

    public Validator checkAtLeast(int expected) {
        if (args.length < expected) throw new ArgumentsMismatchException(String.format(
                "At least %d %s expected, got %d", expected, pluralize(expected), args.length));
        return this;
    }

    public Validator checkOrOr(int expectedOne, int expectedTwo) {
        if (expectedOne != args.length && expectedTwo != args.length)
            throw new ArgumentsMismatchException(String.format(
                    "%d or %d arguments expected, got %d", expectedOne, expectedTwo, args.length));
        return this;
    }

    public Validator checkRange(int from, int to) {
        if (from > args.length || args.length > to)
            throw new ArgumentsMismatchException(String.format(
                    "From %d to %d arguments expected, got %d", from, to, args.length));
        return this;
    }

    public ArrayValue requireArrayAt(int index) {
        checkAtLeast(index + 1);
        final var value = args[index];
        if (value.type() != Types.ARRAY) {
            throw new TypeException(String.format("Array required at %d argument", index));
        }
        return (ArrayValue) value;
    }

    public MapValue requireMapAt(int index) {
        checkAtLeast(index + 1);
        final var value = args[index];
        if (value.type() != Types.MAP) {
            throw new TypeException(String.format("Map required at %d argument", index));
        }
        return (MapValue) value;
    }

    private static String pluralize(int count) {
        return (count == 1) ? "argument" : "arguments";
    }
}
