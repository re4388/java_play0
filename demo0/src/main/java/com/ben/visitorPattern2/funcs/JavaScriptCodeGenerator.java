package com.ben.visitorPattern2.funcs;

import com.ben.visitorPattern2.Expression;
import com.ben.visitorPattern2.ExpressionVisitor;
import com.ben.visitorPattern2.kind.BinaryExpression;
import com.ben.visitorPattern2.kind.FunctionCallExpression;
import com.ben.visitorPattern2.kind.NumberExpression;
import com.ben.visitorPattern2.kind.VariableExpression;

import java.util.List;

public class JavaScriptCodeGenerator implements ExpressionVisitor {
    private final StringBuilder code = new StringBuilder();

    @Override
    public void visit(NumberExpression expr) {
        code.append(expr.value());
    }

    @Override
    public void visit(BinaryExpression expr) {
        code.append("(");
        expr.left().accept(this);
        code.append(" ").append(expr.operator()).append(" ");
        expr.right().accept(this);
        code.append(")");
    }

    @Override
    public void visit(VariableExpression expr) {
        code.append(expr.name());
    }

    @Override
    public void visit(FunctionCallExpression expr) {
        code.append("Math.").append(expr.functionName()).append("(");
        List<Expression> args = expr.arguments();
        for (int i = 0; i < args.size(); i++) {
            args.get(i).accept(this);
            if (i < args.size() - 1) {
                code.append(", ");
            }
        }
        code.append(")");
    }

    public String getCode() {
        return code.toString();
    }
}
