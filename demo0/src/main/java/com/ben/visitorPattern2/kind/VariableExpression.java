package com.ben.visitorPattern2.kind;

import com.ben.visitorPattern2.Expression;
import com.ben.visitorPattern2.ExpressionVisitor;

// 變數節點: x, count, userName
public record VariableExpression(String name) implements Expression {

    @Override
    public void accept(ExpressionVisitor visitor) {
        visitor.visit(this);
    }
}
