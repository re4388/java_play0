package com.ben.visitorPattern1.employType;

import com.ben.visitorPattern1.Employee;
import com.ben.visitorPattern1.EmployeeVisitor;

// 約聘人員
public class Contractor implements Employee {
    private String name;
    private double projectFee;
    private double completionPercentage;

    public Contractor(String name, double projectFee, double completionPercentage) {
        this.name = name;
        this.projectFee = projectFee;
        this.completionPercentage = completionPercentage;
    }

    @Override
    public void accept(EmployeeVisitor visitor) {
        visitor.visit(this);
    }

    public String getName() { return name; }
    public double getProjectFee() { return projectFee; }
    public double getCompletionPercentage() { return completionPercentage; }
}