package com.annimon.hotarufx.lib;

import com.annimon.hotarufx.exceptions.ArgumentsMismatchException;
import com.annimon.hotarufx.exceptions.TypeException;
import lombok.RequiredArgsConstructor;
import lombok.val;

@RequiredArgsConstructor(staticName = "with")
public class Validator {

    private final Value[] args;

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

    public MapValue requireMapAt(int index) {
        checkAtLeast(index + 1);
        val value = args[index];
        if (value.type() != Types.MAP) {
            throw new TypeException(String.format("Map required at %d argument", index));
        }
        return (MapValue) value;
    }

    private static String pluralize(int count) {
        return (count == 1) ? "argument" : "arguments";
    }
}
