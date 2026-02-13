package com.ben.visitorPattern2;

import com.ben.visitorPattern2.funcs.*;
import com.ben.visitorPattern2.kind.BinaryExpression;
import com.ben.visitorPattern2.kind.FunctionCallExpression;
import com.ben.visitorPattern2.kind.NumberExpression;
import com.ben.visitorPattern2.kind.VariableExpression;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParserDemo {
    public static void main(String[] args) {
        // 建立 AST: (10 + x) * sqrt(16) - max(5, 3)
        Expression ast = getAst();

        // ===== 1. Pretty Print =====
        System.out.println("===== Pretty Print =====");
        PrettyPrintVisitor printer = new PrettyPrintVisitor();
        ast.accept(printer);
        System.out.println(printer.getOutput());
        System.out.println();

        // ===== 2. Tree Structure =====
        System.out.println("===== AST Tree =====");
        TreePrintVisitor treePrinter = new TreePrintVisitor();
        ast.accept(treePrinter);
        System.out.println(treePrinter.getOutput());

        // ===== 3. Type Checking =====
        System.out.println("===== Type Checking =====");
        Map<String, String> types = new HashMap<>();
        types.put("x", "number");
        TypeCheckerVisitor typeChecker = new TypeCheckerVisitor(types);
        ast.accept(typeChecker);
        if (typeChecker.hasErrors()) {
            System.out.println("型別錯誤:");
            typeChecker.getErrors().forEach(System.out::println);
        } else {
            System.out.println("✓ 型別檢查通過");
        }
        System.out.println();

        // ===== 4. Evaluation =====
        System.out.println("===== Evaluation =====");
        Map<String, Double> variables = new HashMap<>();
        variables.put("x", 5.0);
        EvaluatorVisitor evaluator = new EvaluatorVisitor(variables);
        ast.accept(evaluator);
        System.out.printf("結果 (x=5): %.2f%n", evaluator.getResult());
        System.out.println();

        // ===== 5. Code Generation =====
        System.out.println("===== JavaScript Code =====");
        JavaScriptCodeGenerator jsGen = new JavaScriptCodeGenerator();
        ast.accept(jsGen);
        System.out.println(jsGen.getCode());
    }

    // 建立 AST: (10 + x) * sqrt(16) - max(5, 3)
    @NotNull
    private static Expression getAst() {
        Expression ast = new BinaryExpression(
                new BinaryExpression(
                        new NumberExpression(10),
                        "+",
                        new VariableExpression("x")
                ),
                "*",
                new FunctionCallExpression("sqrt",
                        List.of(new NumberExpression(16))
                )
        );

        ast = new BinaryExpression(
                ast,
                "-",
                new FunctionCallExpression("max",
                        List.of(
                                new NumberExpression(5),
                                new NumberExpression(3)
                        )
                )
        );
        return ast;
    }
}


/**
 * ```
 *
 * ### 執行結果
 * ```
 * ===== Pretty Print =====
 * (((10.0 + x) * sqrt(16.0)) - max(5.0, 3.0))
 *
 * ===== AST Tree =====
 * BinaryOp: -
 *   ├─ Left:
 *     BinaryOp: *
 *       ├─ Left:
 *         BinaryOp: +
 *           ├─ Left:
 *             Number: 10.0
 *           └─ Right:
 *             Variable: x
 *       └─ Right:
 *         Function: sqrt
 *           ├─ Arg 1:
 *             Number: 16.0
 *   └─ Right:
 *     Function: max
 *       ├─ Arg 1:
 *         Number: 5.0
 *       ├─ Arg 2:
 *         Number: 3.0
 *
 * ===== Type Checking =====
 * ✓ 型別檢查通過
 *
 * ===== Evaluation =====
 * 結果 (x=5): 55.00
 *
 * ===== JavaScript Code =====
 * (((10.0 + x) * Math.sqrt(16.0)) - Math.max(5.0, 3.0))
 */