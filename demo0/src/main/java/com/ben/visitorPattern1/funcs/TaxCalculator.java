package com.ben.visitorPattern1.funcs;

import com.ben.visitorPattern1.employType.Contractor;
import com.ben.visitorPattern1.EmployeeVisitor;
import com.ben.visitorPattern1.employType.FullTimeEmployee;
import com.ben.visitorPattern1.employType.PartTimeEmployee;

public class TaxCalculator implements EmployeeVisitor {
    private double totalTax = 0;

    @Override
    public void visit(FullTimeEmployee employee) {
        // 全職:稅率 20%
        double salary = employee.getMonthlySalary() + employee.getYearsOfService() * 1000;
        double tax = salary * 0.20;
        totalTax += tax;
        System.out.printf("%s 稅額: $%.2f (20%%)%n", employee.getName(), tax);
    }

    @Override
    public void visit(PartTimeEmployee employee) {
        // 兼職:稅率 10%
        double salary = employee.getHourlyRate() * employee.getHoursWorked();
        double tax = salary * 0.10;
        totalTax += tax;
        System.out.printf("%s 稅額: $%.2f (10%%)%n", employee.getName(), tax);
    }

    @Override
    public void visit(Contractor employee) {
        // 約聘:稅率 15%
        double salary = employee.getProjectFee() * (employee.getCompletionPercentage() / 100);
        double tax = salary * 0.15;
        totalTax += tax;
        System.out.printf("%s 稅額: $%.2f (15%%)%n", employee.getName(), tax);
    }

    public double getTotalTax() {
        return totalTax;
    }
}
