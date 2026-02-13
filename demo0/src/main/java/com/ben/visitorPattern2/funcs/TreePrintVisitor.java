package com.ben.visitorPattern2.funcs;

import com.ben.visitorPattern2.ExpressionVisitor;
import com.ben.visitorPattern2.kind.BinaryExpression;
import com.ben.visitorPattern2.kind.FunctionCallExpression;
import com.ben.visitorPattern2.kind.NumberExpression;
import com.ben.visitorPattern2.kind.VariableExpression;

public class TreePrintVisitor implements ExpressionVisitor {
    private final StringBuilder output = new StringBuilder();
    private int depth = 0;

    private void printIndent() {
        for (int i = 0; i < depth; i++) {
            output.append("  ");
        }
    }

    @Override
    public void visit(NumberExpression expr) {
        printIndent();
        output.append("Number: ").append(expr.value()).append("\n");
    }

    @Override
    public void visit(BinaryExpression expr) {
        printIndent();
        output.append("BinaryOp: ").append(expr.operator()).append("\n");

        depth++;
        printIndent();
        output.append("├─ Left:\n");
        depth++;
        expr.left().accept(this);
        depth--;

        printIndent();
        output.append("└─ Right:\n");
        depth++;
        expr.right().accept(this);
        depth -= 2;
    }

    @Override
    public void visit(VariableExpression expr) {
        printIndent();
        output.append("Variable: ").append(expr.name()).append("\n");
    }

    @Override
    public void visit(FunctionCallExpression expr) {
        printIndent();
        output.append("Function: ").append(expr.functionName()).append("\n");

        depth++;
        for (int i = 0; i < expr.arguments().size(); i++) {
            printIndent();
            output.append("├─ Arg ").append(i + 1).append(":\n");
            depth++;
            expr.arguments().get(i).accept(this);
            depth--;
        }
        depth--;
    }

    public String getOutput() {
        return output.toString();
    }
}