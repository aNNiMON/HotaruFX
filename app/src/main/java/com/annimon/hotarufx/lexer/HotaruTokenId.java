package com.annimon.hotarufx.lexer;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PACKAGE)
public enum HotaruTokenId {

    NUMBER(Category.NUMBER),
    WORD(Category.IDENTIFIER),
    TEXT(Category.STRING),

    TRUE(Category.KEYWORD),
    FALSE(Category.KEYWORD),

    EQ(Category.OPERATOR),
    PLUS(Category.OPERATOR),
    MINUS(Category.OPERATOR),
    LPAREN(Category.OPERATOR),
    RPAREN(Category.OPERATOR),
    LBRACE(Category.OPERATOR),
    RBRACE(Category.OPERATOR),
    LBRACKET(Category.OPERATOR),
    RBRACKET(Category.OPERATOR),
    COLON(Category.OPERATOR),
    COMMA(Category.OPERATOR),
    DOT(Category.OPERATOR),
    AT(Category.OPERATOR),

    SINGLE_LINE_COMMENT(Category.COMMENT),
    MULTI_LINE_COMMENT(Category.COMMENT),

    WS(Category.WHITESPACE),
    EOF(Category.WHITESPACE);

    private enum Category {
        NUMBER, IDENTIFIER, STRING, KEYWORD, OPERATOR, COMMENT, WHITESPACE
    }

    private final Category category;

    public String getPrimaryCategory() {
        return category.name().toLowerCase();
    }
}
