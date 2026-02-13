package com.ben.visitorPattern2;

import com.ben.visitorPattern2.kind.BinaryExpression;
import com.ben.visitorPattern2.kind.FunctionCallExpression;
import com.ben.visitorPattern2.kind.NumberExpression;
import com.ben.visitorPattern2.kind.VariableExpression;

public interface ExpressionVisitor {
    void visit(NumberExpression expr);
    void visit(BinaryExpression expr);
    void visit(VariableExpression expr);
    void visit(FunctionCallExpression expr);
}
