package com.ben.visitorPattern1;

import com.ben.visitorPattern1.funcs.ReportGenerator;
import com.ben.visitorPattern1.funcs.SalaryCalculator;
import com.ben.visitorPattern1.funcs.TaxCalculator;
import com.ben.visitorPattern1.employType.Contractor;
import com.ben.visitorPattern1.employType.FullTimeEmployee;
import com.ben.visitorPattern1.employType.PartTimeEmployee;

import java.util.Arrays;
import java.util.List;

public class HRSystem {
    public static void main(String[] args) {
        // 建立員工清單
        List<Employee> employees = Arrays.asList(
                new FullTimeEmployee("張三", 50000, 3),
                new PartTimeEmployee("李四", 200, 80),
                new Contractor("王五", 100000, 75),
                new FullTimeEmployee("趙六", 60000, 5)
        );

        System.out.println("===== 薪資計算 =====");
        SalaryCalculator salaryCalc = new SalaryCalculator();
        employees.forEach(emp -> emp.accept(salaryCalc));
        System.out.printf("總薪資支出: $%.2f%n%n", salaryCalc.getTotalSalary());

        System.out.println("===== 稅額計算 =====");
        TaxCalculator taxCalc = new TaxCalculator();
        employees.forEach(emp -> emp.accept(taxCalc));
        System.out.printf("總稅額: $%.2f%n%n", taxCalc.getTotalTax());

        System.out.println("===== 員工報表 =====");
        ReportGenerator reportGen = new ReportGenerator();
        employees.forEach(emp -> emp.accept(reportGen));
        System.out.println(reportGen.getReport());
    }
}
//```
//
//        ### 執行結果
//```
//        ===== 薪資計算 =====
//全職員工 張三: $53000.00 (底薪 + 年資獎金)
//兼職員工 李四: $16000.00 (時薪 × 工時)
//約聘人員 王五: $75000.00 (專案費 × 75%)
//全職員工 趙六: $65000.00 (底薪 + 年資獎金)
//總薪資支出: $209000.00
//
//        ===== 稅額計算 =====
//張三 稅額: $10600.00 (20%)
//李四 稅額: $1600.00 (10%)
//王五 稅額: $11250.00 (15%)
//趙六 稅額: $13000.00 (20%)
//總稅額: $36450.00
//
//        ===== 員工報表 =====
//        [全職] 張三 - 年資:3年 - 月薪:$50000.00
//        [兼職] 李四 - 工時:80h - 時薪:$200.00
//        [約聘] 王五 - 專案費:$100000.00 - 完成度:75%
//        [全職] 趙六 - 年資:5年 - 月薪:$60000.00
