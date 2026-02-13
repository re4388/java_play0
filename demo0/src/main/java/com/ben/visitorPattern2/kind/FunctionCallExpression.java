package com.ben.visitorPattern2.kind;


import com.ben.visitorPattern2.Expression;
import com.ben.visitorPattern2.ExpressionVisitor;

import java.util.List;

// 函數呼叫節點: sqrt(16), max(a, b)
public record FunctionCallExpression(String functionName, List<Expression> arguments) implements Expression {

    @Override
    public void accept(ExpressionVisitor visitor) {
        visitor.visit(this);
    }
}