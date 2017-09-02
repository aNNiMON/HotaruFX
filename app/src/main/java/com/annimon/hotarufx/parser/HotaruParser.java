package com.annimon.hotarufx.parser;

import com.annimon.hotarufx.exceptions.ParseException;
import com.annimon.hotarufx.lexer.HotaruTokenId;
import com.annimon.hotarufx.lexer.Token;
import com.annimon.hotarufx.parser.ast.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.val;

public class HotaruParser extends Parser {

    public static Node parse(List<Token> tokens) {
        val parser = new HotaruParser(tokens);
        val program = parser.parse();
        if (parser.getParseErrors().hasErrors()) {
            throw new ParseException(parser.getParseErrors().toString());
        }
        return program;
    }

    public HotaruParser(List<Token> tokens) {
        super(tokens);
    }

    private Node block() {
        val block = new BlockNode();
        block.start(getSourcePosition());
        consume(HotaruTokenId.LBRACE);
        while (!match(HotaruTokenId.RBRACE)) {
            block.add(statement());
        }
        block.end(getSourcePosition());
        return block;
    }

    private Node statementOrBlock() {
        if (lookMatch(0, HotaruTokenId.LBRACE)) {
            return block();
        }
        return statement();
    }

    @Override
    protected Node statement() {
        return assignmentStatement();
    }

    private Node assignmentStatement() {
        return expression();
    }

    private Node functionChain(Node qualifiedNameExpr) {
        // f1()()() || f1().f2().f3() || f1().key
        val expr = function(qualifiedNameExpr);
        if (lookMatch(0, HotaruTokenId.LPAREN)) {
            return functionChain(expr);
        }
        return objectAccess(expr);
    }

    private Node objectAccess(Node expr) {
        if (lookMatch(0, HotaruTokenId.DOT)) {
            val indices = variableSuffix();
            if (indices == null || indices.isEmpty())
                return expr;

            if (lookMatch(0, HotaruTokenId.LPAREN)) {
                // next function call
                return functionChain(new AccessNode(expr, indices));
            }
            // container access
            return new AccessNode(expr, indices);
        }
        return expr;
    }

    private FunctionNode function(Node qualifiedNameExpr) {
        // function(arg1, arg2, ...)
        consume(HotaruTokenId.LPAREN);
        val function = new FunctionNode(qualifiedNameExpr);
        while (!match(HotaruTokenId.RPAREN)) {
            function.addArgument(expression());
            match(HotaruTokenId.COMMA);
        }
        return function;
    }

    private Node array() {
        // [value1, value2, ...]
        consume(HotaruTokenId.LBRACKET);
        val elements = new ArrayList<Node>();
        while (!match(HotaruTokenId.RBRACKET)) {
            elements.add(expression());
            match(HotaruTokenId.COMMA);
        }
        return new ArrayNode(elements);
    }

    private Node map() {
        // {key1 : value1, key2 : value2, ...}
        consume(HotaruTokenId.LBRACE);
        final Map<String, Node> elements = new HashMap<>();
        while (!match(HotaruTokenId.RBRACE)) {
            val key = consume(HotaruTokenId.WORD).getText();
            consume(HotaruTokenId.COLON);
            val value = expression();
            elements.put(key, value);
            match(HotaruTokenId.COMMA);
        }
        return new MapNode(elements);
    }


    private Node expression() {
        return assignment();
    }

    private Node assignment() {
        val assignment = assignmentStrict();
        if (assignment != null) {
            return assignment;
        }
        return unary();
    }

    private Node assignmentStrict() {
        final int position = pos;
        val startSourcePosition = getSourcePosition();
        val targetExpr = qualifiedName();
        if ((targetExpr == null) || !(targetExpr instanceof Accessible)) {
            pos = position;
            return null;
        }

        if (!match(HotaruTokenId.EQ)) {
            pos = position;
            return null;
        }
        return new AssignNode((Accessible) targetExpr, expression())
                .start(startSourcePosition)
                .end(getSourcePosition());
    }

    private Node unary() {
        if (match(HotaruTokenId.MINUS)) {
            return new UnaryNode(UnaryNode.Operator.NEGATE, primary());
        }
        if (match(HotaruTokenId.PLUS)) {
            return primary();
        }
        return primary();
    }

    private Node primary() {
        if (match(HotaruTokenId.LPAREN)) {
            val result = expression();
            match(HotaruTokenId.RPAREN);
            return result;
        }
        return variable();
    }

    private Node variable() {
        // function(...
        if (lookMatch(0, HotaruTokenId.WORD) && lookMatch(1, HotaruTokenId.LPAREN)) {
            return functionChain(new ValueNode(consume(HotaruTokenId.WORD).getText()));
        }

        final Node qualifiedNameExpr = qualifiedName();
        if (qualifiedNameExpr != null) {
            // variable(args) || arr["key"](args) || obj.key(args)
            if (lookMatch(0, HotaruTokenId.LPAREN)) {
                return functionChain(qualifiedNameExpr);
            }
            // node@prop || map.node@prop
            if (match(HotaruTokenId.AT)) {
                val propName = consume(HotaruTokenId.WORD).getText();
                val expr = new PropertyNode(qualifiedNameExpr, propName);
                return objectAccess(expr);
            }
            return qualifiedNameExpr;
        }

        if (lookMatch(0, HotaruTokenId.LBRACE)) {
            return map();
        }
        if (lookMatch(0, HotaruTokenId.LBRACKET)) {
            return array();
        }
        return value();
    }

    private Node qualifiedName() {
        // var || var.key[index].key2
        final Token current = get(0);
        if (!match(HotaruTokenId.WORD)) return null;

        final List<Node> indices = variableSuffix();
        if ((indices == null) || indices.isEmpty()) {
            return new VariableNode(current.getText());
        }
        return new AccessNode(current.getText(), indices);
    }

    private List<Node> variableSuffix() {
        final List<Node> indices = new ArrayList<>();
        while (lookMatch(0, HotaruTokenId.DOT)) {
            if (match(HotaruTokenId.DOT)) {
                val fieldName = consume(HotaruTokenId.WORD).getText();
                val key = new ValueNode(fieldName);
                indices.add(key);
            }
        }
        return indices;
    }

    private Node value() {
        val current = get(0);
        if (match(HotaruTokenId.NUMBER)) {
            return new ValueNode(createNumber(current.getText(), 10));
        }
        if (match(HotaruTokenId.TEXT)) {
            return new ValueNode(current.getText());
        }
        throw new ParseException("Unknown expression: " + current, getSourcePosition());
    }

    private Number createNumber(String text, int radix) {
        // Double
        if (text.contains(".")) {
            return Double.parseDouble(text);
        }
        // Integer
        try {
            return Integer.parseInt(text, radix);
        } catch (NumberFormatException nfe) {
            return Long.parseLong(text, radix);
        }
    }
}
