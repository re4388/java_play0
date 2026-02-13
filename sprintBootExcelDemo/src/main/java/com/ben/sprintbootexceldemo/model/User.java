package com.ben.sprintbootexceldemo.model;

import com.ben.sprintbootexceldemo.annotation.ExcelColumn;

import java.time.LocalDateTime;

public class User {

    @ExcelColumn(header = "ID", order = 1)
    private Long id;

    @ExcelColumn(header = "姓名", order = 2)
    private String name;

    @ExcelColumn(header = "Email", order = 3)
    private String email;

    @ExcelColumn(header = "建立時間", order = 4, format = "yyyy-MM-dd")
    private LocalDateTime createdAt;

    public User(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
