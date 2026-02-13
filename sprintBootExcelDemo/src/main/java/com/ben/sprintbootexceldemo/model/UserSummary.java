package com.ben.sprintbootexceldemo.model;

import com.ben.sprintbootexceldemo.annotation.ExcelColumn;

import java.time.LocalDateTime;

public class UserSummary {

    @ExcelColumn(header = "總人數", order = 1)
    private int totalUsers;

    @ExcelColumn(header = "啟用帳號數", order = 2)
    private int activeUsers;

    @ExcelColumn(header = "產生時間", order = 3, format = "yyyy-MM-dd HH:mm")
    private LocalDateTime generatedAt;

    // constructor / getter


    public int getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(int totalUsers) {
        this.totalUsers = totalUsers;
    }

    public int getActiveUsers() {
        return activeUsers;
    }

    public void setActiveUsers(int activeUsers) {
        this.activeUsers = activeUsers;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(LocalDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }

    public UserSummary(int totalUsers, int activeUsers, LocalDateTime generatedAt) {
        this.totalUsers = totalUsers;
        this.activeUsers = activeUsers;
        this.generatedAt = generatedAt;
    }
}

