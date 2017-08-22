package com.annimon.hotarufx.parser;

import com.annimon.hotarufx.lexer.SourcePosition;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public class ParseErrors implements Iterable<ParseError> {

    private final List<ParseError> errors;

    public ParseErrors() {
        errors = new ArrayList<>();
    }

    public void clear() {
        errors.clear();
    }

    public void add(Exception ex, SourcePosition pos) {
        errors.add(new ParseError(ex, pos));
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    @Override
    public Iterator<ParseError> iterator() {
        return errors.iterator();
    }

    public Stream<ParseError> errorsStream() {
        return errors.stream();
    }

    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        for (ParseError error : errors) {
            result.append(error).append(System.lineSeparator());
        }
        return result.toString();
    }
}
