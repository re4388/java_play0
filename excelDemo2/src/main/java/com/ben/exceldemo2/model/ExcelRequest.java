package com.ben.exceldemo2.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ExcelRequest {

    @NotNull(message = "setting 不可為空")
    @Valid
    private ExcelSetting setting;

    @NotNull(message = "data 不可為空")
    @NotEmpty(message = "data 不可為空")
    private List<Map<String, Object>> data;
}