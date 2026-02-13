package com.ben.visitorPattern2.funcs;

import com.ben.visitorPattern2.Expression;
import com.ben.visitorPattern2.ExpressionVisitor;
import com.ben.visitorPattern2.kind.BinaryExpression;
import com.ben.visitorPattern2.kind.FunctionCallExpression;
import com.ben.visitorPattern2.kind.NumberExpression;
import com.ben.visitorPattern2.kind.VariableExpression;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TypeCheckerVisitor implements ExpressionVisitor {
    private final Map<String, String> variableTypes;  // 變數名 -> 型別
    private String currentType;
    private List<String> errors = new ArrayList<>();

    public TypeCheckerVisitor(Map<String, String> variableTypes) {
        this.variableTypes = variableTypes;
    }

    @Override
    public void visit(NumberExpression expr) {
        currentType = "number";
    }

    @Override
    public void visit(BinaryExpression expr) {
        expr.left().accept(this);
        String leftType = currentType;

        expr.right().accept(this);
        String rightType = currentType;

        if (!leftType.equals("number") || !rightType.equals("number")) {
            errors.add(String.format(
                    "型別錯誤: %s 運算需要兩個 number,但得到 %s 和 %s",
                    expr.operator(), leftType, rightType
            ));
        }
        currentType = "number";
    }

    @Override
    public void visit(VariableExpression expr) {
        String varName = expr.name();
        if (!variableTypes.containsKey(varName)) {
            errors.add("未宣告的變數: " + varName);
            currentType = "unknown";
        } else {
            currentType = variableTypes.get(varName);
        }
    }

    @Override
    public void visit(FunctionCallExpression expr) {
        for (Expression arg : expr.arguments()) {
            arg.accept(this);
            if (!currentType.equals("number")) {
                errors.add(String.format(
                        "函數 %s 的參數必須是 number,但得到 %s",
                        expr.functionName(), currentType
                ));
            }
        }
        currentType = "number";
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    public List<String> getErrors() {
        return errors;
    }
}