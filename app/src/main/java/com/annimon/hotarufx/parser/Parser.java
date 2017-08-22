package com.annimon.hotarufx.parser;

import com.annimon.hotarufx.exceptions.ParseException;
import com.annimon.hotarufx.lexer.HotaruTokenId;
import com.annimon.hotarufx.lexer.SourcePosition;
import com.annimon.hotarufx.lexer.Token;
import com.annimon.hotarufx.parser.ast.BlockNode;
import com.annimon.hotarufx.parser.ast.Node;
import java.util.List;
import lombok.val;

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
        val result = new BlockNode();
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
        if (pos >= size) return tokens.get(size - 1).getPosition();
        return tokens.get(pos).getPosition();
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
        val current = get(0);
        if (type != current.getType()) {
            throw new ParseException("Token " + current + " doesn't match " + type, current.getPosition());
        }
        pos++;
        return current;
    }

    protected boolean match(HotaruTokenId type) {
        val current = get(0);
        if (type != current.getType()) return false;
        pos++;
        return true;
    }

    protected boolean lookMatch(int pos, HotaruTokenId type) {
        return get(pos).getType() == type;
    }

    protected Token get(int relativePosition) {
        val position = pos + relativePosition;
        if (position >= size) return EOF;
        return tokens.get(position);
    }

}
