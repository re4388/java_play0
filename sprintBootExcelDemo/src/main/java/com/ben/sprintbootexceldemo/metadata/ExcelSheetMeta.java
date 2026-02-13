package com.ben.sprintbootexceldemo.metadata;

import java.util.List;
import java.util.Set;

public class ExcelSheetMeta<T> {

    private String sheetName;
    private List<T> data;
    private Class<T> type;
    private Set<String> enabledFields; // 動態欄位

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public Class<T> getType() {
        return type;
    }

    public void setType(Class<T> type) {
        this.type = type;
    }

    public Set<String> getEnabledFields() {
        return enabledFields;
    }

    public void setEnabledFields(Set<String> enabledFields) {
        this.enabledFields = enabledFields;
    }

    public ExcelSheetMeta(String sheetName, List<T> data, Class<T> type, Set<String> enabledFields) {
        this.sheetName = sheetName;
        this.data = data;
        this.type = type;
        this.enabledFields = enabledFields;
    }
}
