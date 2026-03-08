package com.ben.excel.core.template;

import com.ben.excel.core.util.ExcelMetaResolver;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.ben.excel.core.metadata.ExcelColumnMeta;
import com.ben.excel.core.metadata.ExcelSheetMeta;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ExcelExportTemplate {

    public Workbook export(List<ExcelSheetMeta<?>> sheets) {
        Workbook workbook = new XSSFWorkbook();
        for (ExcelSheetMeta<?> sheetMeta : sheets) {
            createSheet(workbook, sheetMeta);
        }
        return workbook;
    }

    private void createSheet(Workbook workbook, ExcelSheetMeta<?> meta) {
        Sheet sheet = workbook.createSheet(meta.getSheetName());
        List<ExcelColumnMeta> columns = ExcelMetaResolver.resolve(meta.getType(), meta.getEnabledFields());

        // Header
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < columns.size(); i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns.get(i).getHeader());
        }

        // Data
        int rowIdx = 1;
        for (Object item : meta.getData()) {
            Row row = sheet.createRow(rowIdx++);
            for (int i = 0; i < columns.size(); i++) {
                Cell cell = row.createCell(i);
                setCellValue(cell, item, columns.get(i));
            }
        }
    }

    private void setCellValue(Cell cell, Object item, ExcelColumnMeta colMeta) {
        try {
            Field field = item.getClass().getDeclaredField(colMeta.getField());
            field.setAccessible(true);
            Object value = field.get(item);

            if (value == null) return;

            if (value instanceof LocalDateTime ldt) {
                String fmt = colMeta.getFormat().isEmpty() ? "yyyy-MM-dd HH:mm:ss" : colMeta.getFormat();
                cell.setCellValue(ldt.format(DateTimeFormatter.ofPattern(fmt)));
            } else {
                cell.setCellValue(value.toString());
            }
        } catch (Exception e) {
            cell.setCellValue("Error");
        }
    }
}
