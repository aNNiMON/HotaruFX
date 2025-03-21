package com.annimon.hotarufx.parser;

import com.annimon.hotarufx.exceptions.ParseException;
import com.annimon.hotarufx.lexer.HotaruTokenId;
import com.annimon.hotarufx.lexer.SourcePosition;
import com.annimon.hotarufx.lexer.Token;
import com.annimon.hotarufx.parser.ast.BlockNode;
import com.annimon.hotarufx.parser.ast.Node;
import java.util.List;

public abstract class Parser {

    private static final Token EOF = new Token(HotaruTokenId.EOF, "",
            0, new SourcePosition(-1, -1, -1));

    private final List<Token> tokens;
    private final int size;
    private final ParseErrors parseErrors;
    private Node parsedNode;

    protected int pos;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.size = tokens.size();
        parseErrors = new ParseErrors();
        pos = 0;
    }

    public Node getParsedNode() {
        return parsedNode;
    }

    public ParseErrors getParseErrors() {
        return parseErrors;
    }

    public Node parse() {
        parseErrors.clear();
        final var result = new BlockNode();
        result.start(getSourcePosition());
        while (!match(HotaruTokenId.EOF)) {
            try {
                result.add(statement());
            } catch (Exception ex) {
                parseErrors.add(ex, getSourcePosition());
                recover();
            }
        }
        result.end(getSourcePosition());
        parsedNode = result;
        return result;
    }

    protected abstract Node statement();

    protected SourcePosition getSourcePosition() {
        if (size == 0) return new SourcePosition(0, 0, 0);
        if (pos >= size) return tokens.get(size - 1).position();
        return tokens.get(pos).position();
    }

    private void recover() {
        int preRecoverPosition = pos;
        for (int i = preRecoverPosition; i <= size; i++) {
            pos = i;
            try {
                statement();
                // successfully parsed,
                pos = i; // restore position
                return;
            } catch (Exception ex) {
                // fail
            }
        }
    }

    protected Token consume(HotaruTokenId type) {
        final var current = get(0);
        if (type != current.type()) {
            throw new ParseException("Token " + current + " doesn't match " + type, current.position());
        }
        pos++;
        return current;
    }

    protected boolean match(HotaruTokenId type) {
        final var current = get(0);
        if (type != current.type()) return false;
        pos++;
        return true;
    }

    protected boolean lookMatch(int pos, HotaruTokenId type) {
        return get(pos).type() == type;
    }

    protected Token get(int relativePosition) {
        final var position = pos + relativePosition;
        if (position >= size) return EOF;
        return tokens.get(position);
    }

}
