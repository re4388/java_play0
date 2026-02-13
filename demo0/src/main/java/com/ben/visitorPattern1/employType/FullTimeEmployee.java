package com.ben.visitorPattern1.employType;

import com.ben.visitorPattern1.Employee;
import com.ben.visitorPattern1.EmployeeVisitor;

// 全職員工
public class FullTimeEmployee implements Employee {
    private String name;
    private double monthlySalary;
    private int yearsOfService;

    public FullTimeEmployee(String name, double monthlySalary, int yearsOfService) {
        this.name = name;
        this.monthlySalary = monthlySalary;
        this.yearsOfService = yearsOfService;
    }

    @Override
    public void accept(EmployeeVisitor visitor) {
        visitor.visit(this);
    }

    public String getName() { return name; }
    public double getMonthlySalary() { return monthlySalary; }
    public int getYearsOfService() { return yearsOfService; }
}
