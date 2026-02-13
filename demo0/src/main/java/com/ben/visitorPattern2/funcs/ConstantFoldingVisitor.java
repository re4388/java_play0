package com.ben.visitorPattern2.funcs;

import com.ben.visitorPattern2.*;
import com.ben.visitorPattern2.kind.BinaryExpression;
import com.ben.visitorPattern2.kind.FunctionCallExpression;
import com.ben.visitorPattern2.kind.NumberExpression;
import com.ben.visitorPattern2.kind.VariableExpression;

// 常數折疊優化: (2 + 3) * 4 → 5 * 4 → 20
public class ConstantFoldingVisitor implements ExpressionVisitor {
    private Expression result;

    @Override
    public void visit(NumberExpression expr) {
        result = expr;
    }

    @Override
    public void visit(BinaryExpression expr) {
        // 遞迴優化左右子樹
        expr.left().accept(this);
        Expression left = result;

        expr.right().accept(this);
        Expression right = result;

        // 如果兩邊都是常數,直接計算
        if (left instanceof NumberExpression && right instanceof NumberExpression) {
            double leftVal = ((NumberExpression) left).value();
            double rightVal = ((NumberExpression) right).value();
            double computed = switch (expr.operator()) {
                case "+" -> leftVal + rightVal;
                case "-" -> leftVal - rightVal;
                case "*" -> leftVal * rightVal;
                case "/" -> leftVal / rightVal;
                default -> 0;
            };

            result = new NumberExpression(computed);
        } else {
            result = new BinaryExpression(left, expr.operator(), right);
        }
    }

    @Override
    public void visit(VariableExpression expr) {
        result = expr;
    }

    @Override
    public void visit(FunctionCallExpression expr) {
        result = expr;  // 函數不優化
    }

    public Expression getOptimizedExpression() {
        return result;
    }
}