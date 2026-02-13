Visitor Pattern 在語言處理的優勢
✅ 關注點分離

Parser: 只負責建立 AST
Evaluator: 只負責執行
Type Checker: 只負責型別檢查
Code Generator: 只負責生成程式碼

✅ 易於擴充
想加新功能?加個 Visitor 就好:

死碼消除 (Dead Code Elimination)
變數重命名 (Variable Renaming)
複雜度分析 (Complexity Analysis)

✅ 多階段處理
javaast.accept(typeChecker);    // 第一階段:檢查型別
ast.accept(optimizer);       // 第二階段:優化
ast.accept(codeGenerator);   // 第三階段:生成程式碼

真實世界的例子

Java Compiler (javac): AST → Bytecode
TypeScript Compiler: TS AST → Type Check → JS Code
Babel: ES6+ AST → Transform → ES5
ESLint: AST → Lint Rules → Warnings