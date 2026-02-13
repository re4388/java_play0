package com.ben.sprintbootexceldemo.metadata;

import java.lang.reflect.Field;

public class ExcelColumnMeta {

    private String header;
    private int order;
    private Field field;
    private String format;

    public ExcelColumnMeta(String header, int order, Field field, String format) {
        this.header = header;
        this.order = order;
        this.field = field;
        this.format = format;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    // constructor / getter
}
