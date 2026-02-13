package com.ben.sprintbootexceldemo.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.util.List;

public class ExcelExporter<T> {

    public Workbook export(String sheetName,
                           List<ExcelColumn<T>> columns,
                           List<T> dataList) {

        Workbook wb = new SXSSFWorkbook(100);
        Sheet sheet = wb.createSheet(sheetName);

        // Header
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < columns.size(); i++) {
            headerRow.createCell(i).setCellValue(columns.get(i).getHeader());
        }

        // Data
        for (int i = 0; i < dataList.size(); i++) {
            Row row = sheet.createRow(i + 1);
            T data = dataList.get(i);

            for (int j = 0; j < columns.size(); j++) {
                Object value = columns.get(j).getValue(data);
                Cell cell = row.createCell(j);

                if (value instanceof Number) {
                    cell.setCellValue(((Number) value).doubleValue());
                } else {
                    cell.setCellValue(value == null ? "" : value.toString());
                }
            }
        }

        return wb;
    }
}

