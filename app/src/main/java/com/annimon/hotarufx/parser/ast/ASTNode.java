package com.annimon.hotarufx.parser.ast;

import com.annimon.hotarufx.lexer.SourcePosition;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
public abstract class ASTNode implements Node {

    @Getter @Setter
    private SourcePosition start;
    @Getter @Setter
    private SourcePosition end;

    public String getSourceRange() {
        return start + " .. " + end;
    }
}
