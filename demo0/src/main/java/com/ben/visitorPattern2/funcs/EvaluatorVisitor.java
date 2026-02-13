package com.ben.visitorPattern2.funcs;

import com.ben.visitorPattern2.*;
import com.ben.visitorPattern2.kind.BinaryExpression;
import com.ben.visitorPattern2.kind.FunctionCallExpression;
import com.ben.visitorPattern2.kind.NumberExpression;
import com.ben.visitorPattern2.kind.VariableExpression;

import java.util.List;
import java.util.Map;

public class EvaluatorVisitor implements ExpressionVisitor {
    private final Map<String, Double> variables;
    private double result;

    public EvaluatorVisitor(Map<String, Double> variables) {
        this.variables = variables;
    }

    @Override
    public void visit(NumberExpression expr) {
        result = expr.value();
    }

    @Override
    public void visit(BinaryExpression expr) {
        // 遞迴計算左邊
        expr.left().accept(this);
        double left = result;

        // 遞迴計算右邊
        expr.right().accept(this);
        double right = result;

        // 執行運算
        switch (expr.operator()) {
            case "+": result = left + right; break;
            case "-": result = left - right; break;
            case "*": result = left * right; break;
            case "/":
                if (right == 0) throw new ArithmeticException("除以零");
                result = left / right;
                break;
            default:
                throw new UnsupportedOperationException("未知運算子: " + expr.operator());
        }
    }

    @Override
    public void visit(VariableExpression expr) {
        if (!variables.containsKey(expr.name())) {
            throw new RuntimeException("未定義的變數: " + expr.name());
        }
        result = variables.get(expr.name());
    }

    @Override
    public void visit(FunctionCallExpression expr) {
        String funcName = expr.functionName();
        List<Expression> args = expr.arguments();

        switch (funcName) {
            case "sqrt":
                if (args.size() != 1) throw new RuntimeException("sqrt 需要 1 個參數");
                args.getFirst().accept(this);
                result = Math.sqrt(result);
                break;

            case "max":
                if (args.size() != 2) throw new RuntimeException("max 需要 2 個參數");
                args.get(0).accept(this);
                double a = result;
                args.get(1).accept(this);
                double b = result;
                result = Math.max(a, b);
                break;

            case "min":
                if (args.size() != 2) throw new RuntimeException("min 需要 2 個參數");
                args.get(0).accept(this);
                double x = result;
                args.get(1).accept(this);
                double y = result;
                result = Math.min(x, y);
                break;

            default:
                throw new UnsupportedOperationException("未知函數: " + funcName);
        }
    }

    public double getResult() {
        return result;
    }
}
