package com.ben.visitorPattern2;

// 所有表達式的基類
public interface Expression {
    void accept(ExpressionVisitor visitor);
}
