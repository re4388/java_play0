package com.ben.visitorPattern1.funcs;

import com.ben.visitorPattern1.employType.Contractor;
import com.ben.visitorPattern1.EmployeeVisitor;
import com.ben.visitorPattern1.employType.FullTimeEmployee;
import com.ben.visitorPattern1.employType.PartTimeEmployee;

public class ReportGenerator implements EmployeeVisitor {
    private StringBuilder report = new StringBuilder();

    @Override
    public void visit(FullTimeEmployee employee) {
        report.append(String.format(
                "[全職] %s - 年資:%d年 - 月薪:$%.2f%n",
                employee.getName(),
                employee.getYearsOfService(),
                employee.getMonthlySalary()
        ));
    }

    @Override
    public void visit(PartTimeEmployee employee) {
        report.append(String.format(
                "[兼職] %s - 工時:%dh - 時薪:$%.2f%n",
                employee.getName(),
                employee.getHoursWorked(),
                employee.getHourlyRate()
        ));
    }

    @Override
    public void visit(Contractor employee) {
        report.append(String.format(
                "[約聘] %s - 專案費:$%.2f - 完成度:%.0f%%%n",
                employee.getName(),
                employee.getProjectFee(),
                employee.getCompletionPercentage()
        ));
    }

    public String getReport() {
        return report.toString();
    }
}
