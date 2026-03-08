package com.ben.excel.core.util;


import com.ben.excel.core.annotation.ExcelColumn;
import com.ben.excel.core.metadata.ExcelColumnMeta;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class ExcelMetaResolver {
    public static List<ExcelColumnMeta> resolve(Class<?> clazz, Set<String> enabledFields) {
        List<ExcelColumnMeta> columns = new ArrayList<>();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(ExcelColumn.class)) {
                ExcelColumn ann = field.getAnnotation(ExcelColumn.class);
                if (enabledFields == null || enabledFields.contains(field.getName())) {
                    columns.add(new ExcelColumnMeta(ann.header(), ann.order(), field.getName(), ann.format()));
                }
            }
        }
        columns.sort(Comparator.comparingInt(ExcelColumnMeta::getOrder));
        return columns;
    }
}
