package com.annimon.hotarufx.parser.ast;

import com.annimon.hotarufx.lexer.SourcePosition;

public abstract class ASTNode implements Node {

    private SourcePosition start;
    private SourcePosition end;

    public SourcePosition start() {
        return start;
    }

    public SourcePosition end() {
        return end;
    }

    public ASTNode start(SourcePosition start) {
        this.start = start;
        return this;
    }

    public ASTNode end(SourcePosition end) {
        this.end = end;
        return this;
    }

    public String getSourceRange() {
        return start + " .. " + end;
    }
}
