package com.ben.excel.core.metadata;

public class ExcelColumnMeta {
    private String header;
    private int order;
    private String field;
    private String format;

    public ExcelColumnMeta(String header, int order, String field, String format) {
        this.header = header;
        this.order = order;
        this.field = field;
        this.format = format;
    }

    public String getHeader() { return header; }
    public int getOrder() { return order; }
    public String getField() { return field; }
    public String getFormat() { return format; }
}
