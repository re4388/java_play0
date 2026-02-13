package com.ben.visitorPattern1.funcs;

import com.ben.visitorPattern1.employType.Contractor;
import com.ben.visitorPattern1.EmployeeVisitor;
import com.ben.visitorPattern1.employType.FullTimeEmployee;
import com.ben.visitorPattern1.employType.PartTimeEmployee;

public class SalaryCalculator implements EmployeeVisitor {
    private double totalSalary = 0;

    @Override
    public void visit(FullTimeEmployee employee) {
        // 全職:月薪 + 年資獎金
        double bonus = employee.getYearsOfService() * 1000;
        double salary = employee.getMonthlySalary() + bonus;
        totalSalary += salary;
        System.out.printf("全職員工 %s: $%.2f (底薪 + 年資獎金)%n",
                employee.getName(), salary);
    }

    @Override
    public void visit(PartTimeEmployee employee) {
        // 兼職:時薪 × 工時
        double salary = employee.getHourlyRate() * employee.getHoursWorked();
        totalSalary += salary;
        System.out.printf("兼職員工 %s: $%.2f (時薪 × 工時)%n",
                employee.getName(), salary);
    }

    @Override
    public void visit(Contractor employee) {
        // 約聘:專案費 × 完成度
        double salary = employee.getProjectFee() * (employee.getCompletionPercentage() / 100);
        totalSalary += salary;
        System.out.printf("約聘人員 %s: $%.2f (專案費 × %.0f%%)%n",
                employee.getName(), salary, employee.getCompletionPercentage());
    }

    public double getTotalSalary() {
        return totalSalary;
    }
}
