package com.ben.visitorPattern2.kind;

import com.ben.visitorPattern2.Expression;
import com.ben.visitorPattern2.ExpressionVisitor;

// 數字節點: 42, 3.14
public record NumberExpression(double value) implements Expression {

    @Override
    public void accept(ExpressionVisitor visitor) {
        visitor.visit(this);
    }
}




