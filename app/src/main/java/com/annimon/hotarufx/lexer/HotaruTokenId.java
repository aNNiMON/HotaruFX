package com.annimon.hotarufx.lexer;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PACKAGE)
public enum HotaruTokenId {

    NUMBER(Category.NUMBER),
    WORD(Category.IDENTIFIER),
    TEXT(Category.STRING),

    EQ(Category.OPERATOR),
    LPAREN(Category.OPERATOR),
    RPAREN(Category.OPERATOR),
    LBRACE(Category.OPERATOR),
    RBRACE(Category.OPERATOR),
    COMMA(Category.OPERATOR),
    DOT(Category.OPERATOR),

    SINGLE_LINE_COMMENT(Category.COMMENT),
    MULTI_LINE_COMMENT(Category.COMMENT),

    WS(Category.WHITESPACE),
    EOF(Category.WHITESPACE);

    private enum Category {
        NUMBER, IDENTIFIER, STRING, OPERATOR, COMMENT, WHITESPACE
    }

    private final Category category;

    public String getPrimaryCategory() {
        return category.name().toLowerCase();
    }
}
