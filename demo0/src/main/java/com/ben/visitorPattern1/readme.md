it's like 2 categories use 2 interface to bridge together
in this case, the first category is funcs
and the another case is employType

both category are impl from in their own interface

and these two interface are bridge together


Visitor Pattern 的優勢
✅ 新增操作超簡單
java// 想加「績效評估」功能?只要加一個 Visitor
public class PerformanceEvaluator implements EmployeeVisitor {
// 不用改任何 Employee 類別!
}
✅ 邏輯分離清楚

薪資計算邏輯 → SalaryCalculator
稅務邏輯 → TaxCalculator
報表邏輯 → ReportGenerator

✅ 符合開閉原則

對擴展開放:加新 Visitor 不影響既有程式碼
對修改封閉:Employee 類別不需要改動


什麼時候用 Visitor Pattern?

✅ 物件結構穩定,但操作經常變化
✅ 需要對不同類型物件做不同處理
✅ 想把演算法邏輯從資料結構中分離