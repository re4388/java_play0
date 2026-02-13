package com.ben.visitorPattern2.kind;

import com.ben.visitorPattern2.Expression;
import com.ben.visitorPattern2.ExpressionVisitor;

/**
 * @param operator "+", "-", "*", "/"
 */ // 二元運算節點: +, -, *, /
public record BinaryExpression(Expression left, String operator, Expression right) implements Expression {

    @Override
    public void accept(ExpressionVisitor visitor) {
        visitor.visit(this);
    }
}
