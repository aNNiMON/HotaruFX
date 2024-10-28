package com.annimon.hotarufx.lexer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HotaruLexer extends Lexer {

    public static List<Token> tokenize(String input) {
        final var lexer = new HotaruLexer(input);
        lexer.tokenize();
        return lexer.getTokens();
    }

    private static final String TEXT_CHARS = "'\"";
    private static final String OPERATOR_CHARS = "(){}[]:=+-.,@";

    private static final Map<String, HotaruTokenId> OPERATORS;
    static {
        OPERATORS = new HashMap<>(OPERATOR_CHARS.length());
        OPERATORS.put("(", HotaruTokenId.LPAREN);
        OPERATORS.put(")", HotaruTokenId.RPAREN);
        OPERATORS.put("{", HotaruTokenId.LBRACE);
        OPERATORS.put("}", HotaruTokenId.RBRACE);
        OPERATORS.put("[", HotaruTokenId.LBRACKET);
        OPERATORS.put("]", HotaruTokenId.RBRACKET);
        OPERATORS.put(":", HotaruTokenId.COLON);
        OPERATORS.put("=", HotaruTokenId.EQ);
        OPERATORS.put("+", HotaruTokenId.PLUS);
        OPERATORS.put("-", HotaruTokenId.MINUS);
        OPERATORS.put(".", HotaruTokenId.DOT);
        OPERATORS.put(",", HotaruTokenId.COMMA);
        OPERATORS.put("@", HotaruTokenId.AT);
    }

    private static final Map<String, HotaruTokenId> KEYWORDS;
    static {
        KEYWORDS = Map.ofEntries(
                Map.entry("true", HotaruTokenId.TRUE),
                Map.entry("false", HotaruTokenId.FALSE),
                Map.entry("ms", HotaruTokenId.MS),
                Map.entry("sec", HotaruTokenId.SEC)
        );
    }

    public HotaruLexer(String input) {
        super(input);
    }

    public Token nextToken() {
        final var current = peek(0);
        if (isNumber(current)) return tokenizeNumber();
        else if (Character.isJavaIdentifierStart(current)) return tokenizeWord();
        else if (current == '#') return tokenizeComment();
        else if (current == '/' && peek(1) == '*') {
            return tokenizeMultilineComment();
        }
        else if (TEXT_CHARS.indexOf(current) != -1) {
            return tokenizeText(current);
        }
        else if (OPERATOR_CHARS.indexOf(current) != -1) {
            return tokenizeOperator();
        }
        else if (Character.isWhitespace(current)) {
            return tokenizeWhitespaces();
        }
        else {
            // other
            next();
        }
        return createToken(HotaruTokenId.WS, "", 1);
    }

    private Token tokenizeNumber() {
        clearBuffer();
        char current = peek(0);
        while (true) {
            if (current == '.') {
                if (getBuffer().indexOf(".") != -1)
                    throw error("Invalid float number");
            } else if (!isNumber(current)) {
                break;
            }
            getBuffer().append(current);
            current = next();
        }
        return addToken(HotaruTokenId.NUMBER);
    }

    private Token tokenizeWord() {
        clearBuffer();
        getBuffer().append(peek(0));
        char current = next();
        while (!isEOF()) {
            if (!Character.isJavaIdentifierPart(current)) {
                break;
            }
            getBuffer().append(current);
            current = next();
        }

        final var word = getBuffer().toString();
        return addToken(KEYWORDS.getOrDefault(word, HotaruTokenId.WORD));
    }

    private Token tokenizeText(char openChar) {
        next();// "
        clearBuffer();
        int startPos = getPos() - 1;
        char current = peek(0);
        while (true) {
            if (current == '\\') {
                final var buffer = getBuffer();
                current = next();
                if ("\r\n\0".indexOf(current) != -1) {
                    throw error("Reached end of line while parsing text");
                }
                if (current == openChar) {
                    current = next();
                    buffer.append(openChar);
                    continue;
                }
                int idx = "\\0bfnrt".indexOf(current);
                if (idx != -1) {
                    current = next();
                    buffer.append("\\\0\b\f\n\r\t".charAt(idx));
                    continue;
                }
                if (current == 'u') {
                    current = tokenizeUnicodeString(current, buffer);
                    continue;
                }
                buffer.append('\\');
                continue;
            }
            if (current == openChar) break;
            if (current == '\0') {
                throw error("Reached end of file while parsing text");
            }
            getBuffer().append(current);
            current = next();
        }
        next(); // "
        int actualLength = getPos() - startPos;
        return addToken(HotaruTokenId.TEXT, getBuffer().toString(), actualLength);
    }

    private char tokenizeUnicodeString(char current, StringBuilder buffer) {
        // http://docs.oracle.com/javase/specs/jls/se8/html/jls-3.html#jls-3.3
        int rollbackPosition = getPos();
        while (current == 'u') {
            current = next();
        }
        int escapedValue = 0;
        for (int i = 12; i >= 0 && escapedValue != -1; i -= 4) {
            if (isHexNumber(current)) {
                escapedValue |= (Character.digit(current, 16) << i);
            } else {
                escapedValue = -1;
            }
            current = next();
        }
        if (escapedValue >= 0) {
            buffer.append((char) escapedValue);
        } else {
            // rollback
            buffer.append("\\u");
            setPos(rollbackPosition);
        }
        return current;
    }

    private Token tokenizeOperator() {
        char current = peek(0);
        clearBuffer();
        while (true) {
            final var text = getBuffer().toString();
            if (!text.isEmpty() && !OPERATORS.containsKey(text + current)) {
                return addToken(OPERATORS.get(text), "", text.length());
            }
            getBuffer().append(current);
            current = next();
        }
    }

    private Token tokenizeComment() {
        next(); // #
        clearBuffer();
        getBuffer().append("#");
        char current = peek(0);
        while ("\r\n\0".indexOf(current) == -1) {
            getBuffer().append(current);
            current = next();
        }
        return createToken(HotaruTokenId.SINGLE_LINE_COMMENT);
    }

    private Token tokenizeMultilineComment() {
        next(); // /
        next(); // *
        clearBuffer();
        getBuffer().append("/*");
        char current = peek(0);
        while (current != '*' || peek(1) != '/') {
            if (current == '\0') {
                throw error("Reached end of file while parsing multiline comment");
            }
            getBuffer().append(current);
            current = next();
        }
        next(); // *
        next(); // /
        getBuffer().append("*/");
        return createToken(HotaruTokenId.MULTI_LINE_COMMENT);
    }

    private Token tokenizeWhitespaces() {
        clearBuffer();
        char current = peek(0);
        while (Character.isWhitespace(current)) {
            getBuffer().append(current);
            current = next();
        }
        return createToken(HotaruTokenId.WS);
    }
}
