package com.ben.sprintbootexceldemo.util;

import com.ben.sprintbootexceldemo.annotation.ExcelColumn;
import com.ben.sprintbootexceldemo.metadata.ExcelColumnMeta;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class ExcelMetaResolver {

    public static <T> List<ExcelColumnMeta> resolveColumns(
            Class<T> clazz,
            Set<String> enabledFields) {


        List<ExcelColumnMeta> excelColumnMetaList = Arrays.stream(clazz.getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(ExcelColumn.class))
                .map(f -> {
                    ExcelColumn annotation = f.getAnnotation(ExcelColumn.class);
                    f.setAccessible(true);
                    return new ExcelColumnMeta(
                            annotation.header(),
                            annotation.order(),
                            f,
                            annotation.format()
                    );
                })
                .filter(m -> enabledFields == null ||
                        enabledFields.contains(m.getField().getName()))
                .sorted(Comparator.comparingInt(ExcelColumnMeta::getOrder))
                .toList();

        return excelColumnMetaList;
    }
}
