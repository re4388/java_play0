package com.ben.exceldemo2.model;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class ExcelRequest {
    private ExcelSetting setting;
    private List<Map<String, Object>> data;
}