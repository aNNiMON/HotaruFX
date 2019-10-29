package com.annimon.hotarufx.lexer;

import com.annimon.hotarufx.exceptions.LexerException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("WeakerAccess")
public abstract class Lexer {

    private final String input;
    private final int length;

    private final List<Token> tokens;
    private final StringBuilder buffer;

    private int pos;
    private int row, col;

    public Lexer(String input) {
        this.input = input;
        length = input.length();
        tokens = new ArrayList<>();
        buffer = new StringBuilder();
        pos = 0;
        row = col = 1;
    }

    public List<Token> getTokens() {
        return tokens;
    }

    protected StringBuilder getBuffer() {
        return buffer;
    }

    protected int getPos() {
        return pos;
    }

    protected void setPos(int pos) {
        this.pos = pos;
    }

    public boolean isEOF() {
        return pos >= length;
    }

    public List<Token> tokenize() {
        final List<Token> allTokens = new ArrayList<>();
        while (!isEOF()) {
            allTokens.add(nextToken());
        }
        return allTokens;
    }

    public abstract Token nextToken();

    protected void clearBuffer() {
        buffer.setLength(0);
    }

    protected char next() {
        pos++;
        final char result = peek(0);
        if (result == '\n') {
            row++;
            col = 1;
        } else col++;
        return result;
    }

    protected char peek(int relativePosition) {
        final int position = pos + relativePosition;
        if (position >= length) return '\0';
        return input.charAt(position);
    }

    protected SourcePosition currentPosition() {
        return new SourcePosition(pos, row, col);
    }

    protected Token addToken(HotaruTokenId tokenId) {
        return addToken(tokenId, buffer.toString());
    }

    protected Token addToken(HotaruTokenId tokenId, String text) {
        return addToken(tokenId, text, text.length());
    }

    protected Token addToken(HotaruTokenId tokenId, String text, int length) {
        final var token = createToken(tokenId, text, length);
        tokens.add(token);
        return token;
    }

    protected Token createToken(HotaruTokenId tokenId) {
        return createToken(tokenId, buffer.toString(), buffer.length());
    }

    protected Token createToken(HotaruTokenId tokenId, String text, int length) {
        return new Token(tokenId, text, length, currentPosition());
    }

    protected LexerException error(String message) {
        return new LexerException(currentPosition(), message);
    }

    protected boolean isHexNumber(char current) {
        return Character.isDigit(current)
                || ('a' <= current && current <= 'f')
                || ('A' <= current && current <= 'F');
    }
}
