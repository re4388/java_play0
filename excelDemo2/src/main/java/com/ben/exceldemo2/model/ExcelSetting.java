package com.ben.exceldemo2.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ExcelSetting {

    private String sheetName = "Sheet1";

    @NotEmpty(message = "headers 不可為空")
    private List<String> headers;

    @NotEmpty(message = "columnKeys 不可為空")
    private List<String> columnKeys;  // 對應 data 中的 key

    private Map<String, String> columnTypes;  // 新增:指定每個欄位的型別
    private Integer headerRowHeight;
    private Integer dataRowHeight;
    private Boolean autoSizeColumns;
}