package com.annimon.hotarufx.lexer;

import com.annimon.hotarufx.exceptions.LexerException;
import java.util.List;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

class HotaruLexerTest {

    static List<Token> t(String input) {
        return HotaruLexer.tokenize(input);
    }

    static List<Token> all(String input) {
        return new HotaruLexer(input).tokenize();
    }

    static Token single(String input) {
        List<Token> tokens = t(input);
        if (tokens.isEmpty()) {
            throw new AssertionError("Tokens list is empty");
        }
        return tokens.get(0);
    }

    @Test
    void testTokenizeNumbers() {
        assertThat(all("1 1.5 2"), contains(
                tokenId(HotaruTokenId.NUMBER),
                tokenId(HotaruTokenId.WS),
                tokenId(HotaruTokenId.NUMBER),
                tokenId(HotaruTokenId.WS),
                tokenId(HotaruTokenId.NUMBER)
        ));

        assertThrows(LexerException.class, () -> {
            all("1.2.3");
        });
    }

    @Test
    void testTokenizeWords() {
        assertThat(all("a b c"), contains(
                tokenId(HotaruTokenId.WORD),
                tokenId(HotaruTokenId.WS),
                tokenId(HotaruTokenId.WORD),
                tokenId(HotaruTokenId.WS),
                tokenId(HotaruTokenId.WORD)
        ));
    }

    @Test
    void testTokenizeText() {
        assertThat(t("1 \" 1\n2 3 '\""), contains(
                tokenId(HotaruTokenId.NUMBER),
                tokenId(HotaruTokenId.TEXT)
        ));
        assertThat(t("1 ' 1\n2 3 ' 2"), contains(
                tokenId(HotaruTokenId.NUMBER),
                tokenId(HotaruTokenId.TEXT),
                tokenId(HotaruTokenId.NUMBER)
        ));
        assertThrows(LexerException.class, () -> {
            all("' ... ");
        });
        assertThat(single("'\\\''").getText(), is("'"));
        assertThat(single("\'\\\"\'").getText(), is("\\\""));
        assertThat(single("\"\\\"\"").getText(), is("\""));
        assertThat(single("\"\\\'\"").getText(), is("\\\'"));
    }

    @Test
    void testTokenizeOperators() {
        assertThat(t("(){}=,."), contains(
                tokenId(HotaruTokenId.LPAREN),
                tokenId(HotaruTokenId.RPAREN),
                tokenId(HotaruTokenId.LBRACE),
                tokenId(HotaruTokenId.RBRACE),
                tokenId(HotaruTokenId.EQ),
                tokenId(HotaruTokenId.COMMA),
                tokenId(HotaruTokenId.DOT)
        ));
    }

    @Test
    void testTokenizeComments() {
        assertThat(all("1 # 2 3 4"), contains(
                tokenId(HotaruTokenId.NUMBER),
                tokenId(HotaruTokenId.WS),
                tokenId(HotaruTokenId.SINGLE_LINE_COMMENT)
        ));
        assertThat(t("1 # 2 3 4\n 2"), contains(
                tokenId(HotaruTokenId.NUMBER),
                tokenId(HotaruTokenId.NUMBER)
        ));
    }

    @Test
    void testTokenizeMultilineComments() {
        assertThat(all("1 /* 2\n3\n4 */"), contains(
                tokenId(HotaruTokenId.NUMBER),
                tokenId(HotaruTokenId.WS),
                tokenId(HotaruTokenId.MULTI_LINE_COMMENT)
        ));
        assertThat(t("1 /* 2 3 4 */ 2"), contains(
                tokenId(HotaruTokenId.NUMBER),
                tokenId(HotaruTokenId.NUMBER)
        ));
        assertThrows(LexerException.class, () -> {
            all("/* ... ");
        });
    }

    @Test
    void testStatements() {
        assertThat(t("A = node()"), contains(
                tokenId(HotaruTokenId.WORD),
                tokenId(HotaruTokenId.EQ),
                tokenId(HotaruTokenId.WORD),
                tokenId(HotaruTokenId.LPAREN),
                tokenId(HotaruTokenId.RPAREN)
        ));
        assertThat(t("B.x = 100"), contains(
                tokenId(HotaruTokenId.WORD),
                tokenId(HotaruTokenId.DOT),
                tokenId(HotaruTokenId.WORD),
                tokenId(HotaruTokenId.EQ),
                tokenId(HotaruTokenId.NUMBER)
        ));
        assertThat(t("G1 = group(A, B)"), contains(
                tokenId(HotaruTokenId.WORD),
                tokenId(HotaruTokenId.EQ),
                tokenId(HotaruTokenId.WORD),
                tokenId(HotaruTokenId.LPAREN),
                tokenId(HotaruTokenId.WORD),
                tokenId(HotaruTokenId.COMMA),
                tokenId(HotaruTokenId.WORD),
                tokenId(HotaruTokenId.RPAREN)
        ));
    }

    Matcher<Token> tokenId(HotaruTokenId tokenId) {
        return tokenId(is(tokenId));
    }

    Matcher<Token> tokenId(Matcher<HotaruTokenId> matcher) {
        return new FeatureMatcher<Token, HotaruTokenId>(matcher, "tokenId", "tokenId") {

            @Override
            protected HotaruTokenId featureValueOf(Token actual) {
                return actual.getType();
            }
        };
    }
}