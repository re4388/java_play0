package com.ben.visitorPattern2.funcs;

import com.ben.visitorPattern2.Expression;
import com.ben.visitorPattern2.ExpressionVisitor;
import com.ben.visitorPattern2.kind.BinaryExpression;
import com.ben.visitorPattern2.kind.FunctionCallExpression;
import com.ben.visitorPattern2.kind.NumberExpression;
import com.ben.visitorPattern2.kind.VariableExpression;

import java.util.List;

public class PrettyPrintVisitor implements ExpressionVisitor {
    private final StringBuilder output = new StringBuilder();
    private int indent = 0;

    @Override
    public void visit(NumberExpression expr) {
        output.append(expr.value());
    }

    @Override
    public void visit(BinaryExpression expr) {
        output.append("(");
        expr.left().accept(this);
        output.append(" ").append(expr.operator()).append(" ");
        expr.right().accept(this);
        output.append(")");
    }

    @Override
    public void visit(VariableExpression expr) {
        output.append(expr.name());
    }

    @Override
    public void visit(FunctionCallExpression expr) {
        output.append(expr.functionName()).append("(");
        List<Expression> args = expr.arguments();
        for (int i = 0; i < args.size(); i++) {
            args.get(i).accept(this);
            if (i < args.size() - 1) {
                output.append(", ");
            }
        }
        output.append(")");
    }

    public String getOutput() {
        return output.toString();
    }
}
