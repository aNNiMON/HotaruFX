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
        KEYWORDS = new HashMap<>();
        KEYWORDS.put("true", HotaruTokenId.TRUE);
        KEYWORDS.put("false", HotaruTokenId.FALSE);
        KEYWORDS.put("ms", HotaruTokenId.MS);
        KEYWORDS.put("sec", HotaruTokenId.SEC);
    }

    public HotaruLexer(String input) {
        super(input);
    }

    public Token nextToken() {
        final var current = peek(0);
        if (Character.isDigit(current)) return tokenizeNumber();
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
            } else if (!Character.isDigit(current)) {
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
                if (current == openChar) {
                    current = next();
                    buffer.append(openChar);
                    continue;
                }
                switch (current) {
                    case '0': current = next(); buffer.append('\0'); continue;
                    case 'b': current = next(); buffer.append('\b'); continue;
                    case 'f': current = next(); buffer.append('\f'); continue;
                    case 'n': current = next(); buffer.append('\n'); continue;
                    case 'r': current = next(); buffer.append('\r'); continue;
                    case 't': current = next(); buffer.append('\t'); continue;
                    case 'u': // http://docs.oracle.com/javase/specs/jls/se8/html/jls-3.html#jls-3.3
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
        while (true) {
            if (current == '*' && peek(1) == '/') break;
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
