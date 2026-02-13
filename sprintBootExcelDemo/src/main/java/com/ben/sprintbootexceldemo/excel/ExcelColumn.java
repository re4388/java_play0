package com.ben.sprintbootexceldemo.excel;

import java.util.function.Function;

public class ExcelColumn<T> {

    private final String header;
    private final Function<T, Object> valueExtractor;

    public ExcelColumn(String header, Function<T, Object> valueExtractor) {
        this.header = header;
        this.valueExtractor = valueExtractor;
    }

    public String getHeader() {
        return header;
    }

    public Object getValue(T data) {
        return valueExtractor.apply(data);
    }
}
