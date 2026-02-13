package com.ben.sprintbootexceldemo.template;

import com.ben.sprintbootexceldemo.util.ExcelMetaResolver;
import com.ben.sprintbootexceldemo.metadata.ExcelColumnMeta;
import com.ben.sprintbootexceldemo.metadata.ExcelSheetMeta;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class ExcelExportTemplate {

    public Workbook export(List<ExcelSheetMeta<?>> sheets) {

        Workbook wb = new SXSSFWorkbook(100);
        CreationHelper helper = wb.getCreationHelper();

        for (ExcelSheetMeta<?> sheetMeta : sheets) {
            createSheet(wb, helper, sheetMeta);
        }

        return wb;
    }

    private <T> void createSheet(Workbook wb, CreationHelper helper, ExcelSheetMeta<T> sheetMeta) {
        Sheet sheet = wb.createSheet(sheetMeta.getSheetName());
        List<ExcelColumnMeta> columns = ExcelMetaResolver.resolveColumns(
                        sheetMeta.getType(),
                        sheetMeta.getEnabledFields()
                );

        // Header
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < columns.size(); i++) {
            headerRow.createCell(i).setCellValue(columns.get(i).getHeader());
        }

        // Data
        for (int i = 0; i < sheetMeta.getData().size(); i++) {
            Row row = sheet.createRow(i + 1);
            T data = sheetMeta.getData().get(i);

            for (int j = 0; j < columns.size(); j++) {
                writeCell(wb,
                        helper,
                        row.createCell(j),
                        columns.get(j),
                        data);
            }
        }
    }

    private <T> void writeCell(
            Workbook wb,
            CreationHelper helper,
            Cell cell,
            ExcelColumnMeta meta,
            T data) {

        try {
            Object value = meta.getField().get(data);

            if (value == null) return;

            if (value instanceof Number n) {
                cell.setCellValue(n.doubleValue());
            } else if (value instanceof LocalDateTime dt) {
                CellStyle style = wb.createCellStyle();
                style.setDataFormat(
                        helper.createDataFormat()
                                .getFormat(meta.getFormat())
                );
                cell.setCellValue(
                        Date.from(dt.atZone(ZoneId.systemDefault()).toInstant())
                );
                cell.setCellStyle(style);
            } else {
                cell.setCellValue(value.toString());
            }

        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}

