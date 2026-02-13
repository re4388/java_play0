package com.ben.visitorPattern1;

import com.ben.visitorPattern1.employType.Contractor;
import com.ben.visitorPattern1.employType.FullTimeEmployee;
import com.ben.visitorPattern1.employType.PartTimeEmployee;

// 訪問者介面
public interface EmployeeVisitor {
    void visit(FullTimeEmployee employee);
    void visit(PartTimeEmployee employee);
    void visit(Contractor employee);
}