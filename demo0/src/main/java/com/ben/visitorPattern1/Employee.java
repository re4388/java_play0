package com.ben.visitorPattern1;

// 員工基類
public interface Employee {
    void accept(EmployeeVisitor visitor);
    String getName();
}
