package com.ben.visitorPattern1.employType;

import com.ben.visitorPattern1.Employee;
import com.ben.visitorPattern1.EmployeeVisitor;

// 兼職員工
public class PartTimeEmployee implements Employee {
    private String name;
    private double hourlyRate;
    private int hoursWorked;

    public PartTimeEmployee(String name, double hourlyRate, int hoursWorked) {
        this.name = name;
        this.hourlyRate = hourlyRate;
        this.hoursWorked = hoursWorked;
    }

    @Override
    public void accept(EmployeeVisitor visitor) {
        visitor.visit(this);
    }

    public String getName() { return name; }
    public double getHourlyRate() { return hourlyRate; }
    public int getHoursWorked() { return hoursWorked; }
}