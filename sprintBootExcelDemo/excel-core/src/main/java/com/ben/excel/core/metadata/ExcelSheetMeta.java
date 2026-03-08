package com.ben.excel.core.metadata;

import java.util.List;
import java.util.Set;

public class ExcelSheetMeta<T> {
    private String sheetName;
    private List<T> data;
    private Class<T> type;
    private Set<String> enabledFields;

    public ExcelSheetMeta(String sheetName, List<T> data, Class<T> type, Set<String> enabledFields) {
        this.sheetName = sheetName;
        this.data = data;
        this.type = type;
        this.enabledFields = enabledFields;
    }

    public String getSheetName() { return sheetName; }
    public List<T> getData() { return data; }
    public Class<T> getType() { return type; }
    public Set<String> getEnabledFields() { return enabledFields; }
}
