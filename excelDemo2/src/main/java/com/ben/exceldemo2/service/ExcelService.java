package com.ben.exceldemo2.service;


import com.ben.exceldemo2.model.ExcelRequest;
import com.ben.exceldemo2.model.ExcelSetting;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ExcelService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 產生 Excel 並直接寫入傳入的 OutputStream。
     * 使用 SXSSFWorkbook（POI Streaming API）：記憶體中只保留固定筆數的 row，
     * 超出的 row 會自動 flush 到磁碟暫存檔，大幅降低大資料量時的記憶體消耗。
     *
     * @param request      Excel 設定與資料
     * @param outputStream 目標輸出串流（由 Spring StreamingResponseBody 提供）
     */
    public void generateExcel(ExcelRequest request, OutputStream outputStream) throws IOException {
        // rowAccessWindowSize = 100：記憶體中最多保留 100 筆 row，超出自動寫入磁碟暫存
        SXSSFWorkbook workbook = new SXSSFWorkbook(100);
        // 壓縮磁碟暫存的 XML，進一步節省磁碟空間（適合大資料量）
        workbook.setCompressTempFiles(true);

        try {
            ExcelSetting setting = request.getSetting();

            // 建立 sheet（SXSSFSheet 才有 trackAllColumnsForAutoSizing）
            SXSSFSheet sheet = workbook.createSheet(
                    setting.getSheetName() != null ? setting.getSheetName() : "Sheet1"
            );

            // ⚠️ 若需要 autoSizeColumn，必須在寫入任何資料「之前」先呼叫 trackAllColumnsForAutoSizing，
            //    這樣 SXSSF 才會在 sliding window 中保留欄寬計算所需的資訊。
            if (Boolean.TRUE.equals(setting.getAutoSizeColumns())) {
                sheet.trackAllColumnsForAutoSizing();
            }

            // 建立各種樣式（必須在寫入 row 前完成，SXSSFWorkbook 不支援回頭讀取已 flush 的 row）
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle textStyle = createTextStyle(workbook);
            CellStyle numberStyle = createNumberStyle(workbook);
            CellStyle currencyStyle = createCurrencyStyle(workbook);
            CellStyle percentStyle = createPercentStyle(workbook);
            CellStyle dateStyle = createDateStyle(workbook);
            CellStyle datetimeStyle = createDateTimeStyle(workbook);

            // 寫入表頭
            Row headerRow = sheet.createRow(0);
            if (setting.getHeaderRowHeight() != null) {
                headerRow.setHeightInPoints(setting.getHeaderRowHeight());
            }

            List<String> headers = setting.getHeaders();
            for (int i = 0; i < headers.size(); i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers.get(i));
                cell.setCellStyle(headerStyle);
            }

            // 寫入資料
            List<Map<String, Object>> dataList = request.getData();
            List<String> columnKeys = setting.getColumnKeys();
            Map<String, String> columnTypes = setting.getColumnTypes();

            for (int rowIndex = 0; rowIndex < dataList.size(); rowIndex++) {
                Row row = sheet.createRow(rowIndex + 1);
                if (setting.getDataRowHeight() != null) {
                    row.setHeightInPoints(setting.getDataRowHeight());
                }

                Map<String, Object> rowData = dataList.get(rowIndex);

                for (int colIndex = 0; colIndex < columnKeys.size(); colIndex++) {
                    Cell cell = row.createCell(colIndex);
                    String columnKey = columnKeys.get(colIndex);
                    Object value = rowData.get(columnKey);

                    String specifiedType = columnTypes != null ? columnTypes.get(columnKey) : null;
                    setCellValueWithType(cell, value, specifiedType,
                            textStyle, numberStyle, currencyStyle, percentStyle,
                            dateStyle, datetimeStyle);
                }
            }

            // 自動調整欄寬（只有在 trackAllColumnsForAutoSizing 之後才有效）
            if (Boolean.TRUE.equals(setting.getAutoSizeColumns())) {
                for (int i = 0; i < headers.size(); i++) {
                    sheet.autoSizeColumn(i);
                    sheet.setColumnWidth(i, sheet.getColumnWidth(i) + 512);
                }
            }

            // 直接寫入 HTTP response OutputStream
            workbook.write(outputStream);

        } finally {
            // ⚠️ 必須呼叫 dispose() 清除磁碟暫存檔，否則會造成磁碟空間洩漏
            workbook.dispose();
            workbook.close();
        }
    }

    /**
     * 設定 Cell 值和樣式
     */
    private void setCellValueWithType(Cell cell,
                                      Object value,
                                      String specifiedType,
                                      CellStyle textStyle,
                                      CellStyle numberStyle,
                                      CellStyle currencyStyle,
                                      CellStyle percentStyle,
                                      CellStyle dateStyle,
                                      CellStyle datetimeStyle) {
        if (value == null) {
            cell.setCellValue("");
            cell.setCellStyle(textStyle);
            return;
        }

        // 如果有指定型別，優先使用指定型別
        if (specifiedType != null) {
            setValueBySpecifiedType(cell, value, specifiedType,
                    textStyle, numberStyle, currencyStyle, percentStyle, dateStyle, datetimeStyle);
            return;
        }

        // 自動判斷型別
        if (value instanceof Number) {
            cell.setCellValue(((Number) value).doubleValue());
            cell.setCellStyle(numberStyle);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value ? "是" : "否");
            cell.setCellStyle(textStyle);
        } else if (value instanceof Date) {
            cell.setCellValue((Date) value);
            cell.setCellStyle(datetimeStyle);
        } else if (value instanceof LocalDate) {
            String dateStr = ((LocalDate) value).format(DATE_FORMATTER);
            cell.setCellValue(dateStr);
            cell.setCellStyle(dateStyle);
        } else if (value instanceof LocalDateTime) {
            String dateStr = ((LocalDateTime) value).format(DATETIME_FORMATTER);
            cell.setCellValue(dateStr);
            cell.setCellStyle(datetimeStyle);
        } else {
            String strValue = value.toString();
            if (tryParseAsNumber(cell, strValue, numberStyle)) {
                return;
            }
            if (tryParseAsDate(cell, strValue, dateStyle, datetimeStyle)) {
                return;
            }
            cell.setCellValue(strValue);
            cell.setCellStyle(textStyle);
        }
    }

    /**
     * 按指定型別設定值
     */
    private void setValueBySpecifiedType(Cell cell,
                                         Object value,
                                         String type,
                                         CellStyle textStyle,
                                         CellStyle numberStyle,
                                         CellStyle currencyStyle,
                                         CellStyle percentStyle,
                                         CellStyle dateStyle,
                                         CellStyle datetimeStyle) {
        switch (type.toLowerCase()) {
            case "number":
            case "integer":
            case "double":
                cell.setCellValue(parseAsNumber(value));
                cell.setCellStyle(numberStyle);
                break;
            case "currency":
            case "money":
                cell.setCellValue(parseAsNumber(value));
                cell.setCellStyle(currencyStyle);
                break;
            case "percent":
            case "percentage":
                cell.setCellValue(parseAsNumber(value) / 100);
                cell.setCellStyle(percentStyle);
                break;
            case "date":
                cell.setCellValue(value.toString());
                cell.setCellStyle(dateStyle);
                break;
            case "datetime":
                cell.setCellValue(value.toString());
                cell.setCellStyle(datetimeStyle);
                break;
            case "boolean":
                cell.setCellValue(parseAsBoolean(value) ? "是" : "否");
                cell.setCellStyle(textStyle);
                break;
            case "text":
            case "string":
            default:
                cell.setCellValue(value.toString());
                cell.setCellStyle(textStyle);
                break;
        }
    }

    private boolean tryParseAsNumber(Cell cell, String value, CellStyle numberStyle) {
        try {
            double num = Double.parseDouble(value.replace(",", ""));
            cell.setCellValue(num);
            cell.setCellStyle(numberStyle);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean tryParseAsDate(Cell cell, String value, CellStyle dateStyle, CellStyle datetimeStyle) {
        try {
            if (value.contains(":")) {
                cell.setCellValue(value);
                cell.setCellStyle(datetimeStyle);
                return true;
            } else if (value.matches("\\d{4}-\\d{2}-\\d{2}")) {
                cell.setCellValue(value);
                cell.setCellStyle(dateStyle);
                return true;
            }
        } catch (Exception e) {
            // 解析失敗，忽略
        }
        return false;
    }

    private double parseAsNumber(Object value) {
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        try {
            return Double.parseDouble(value.toString().replace(",", ""));
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private boolean parseAsBoolean(Object value) {
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        String str = value.toString().toLowerCase();
        return str.equals("true") || str.equals("1") || str.equals("yes") || str.equals("是");
    }


    // ========== 樣式建立 ==========

    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 12);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        setBorder(style);
        return style;
    }

    private CellStyle createTextStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        setBorder(style);
        return style;
    }

    private CellStyle createNumberStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setDataFormat(workbook.createDataFormat().getFormat("#,##0.00"));
        style.setAlignment(HorizontalAlignment.RIGHT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        setBorder(style);
        return style;
    }

    private CellStyle createCurrencyStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setDataFormat(workbook.createDataFormat().getFormat("$#,##0.00"));
        style.setAlignment(HorizontalAlignment.RIGHT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        setBorder(style);
        return style;
    }

    private CellStyle createPercentStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setDataFormat(workbook.createDataFormat().getFormat("0.00%"));
        style.setAlignment(HorizontalAlignment.RIGHT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        setBorder(style);
        return style;
    }

    private CellStyle createDateStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setDataFormat(workbook.createDataFormat().getFormat("yyyy-mm-dd"));
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        setBorder(style);
        return style;
    }

    private CellStyle createDateTimeStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setDataFormat(workbook.createDataFormat().getFormat("yyyy-mm-dd hh:mm:ss"));
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        setBorder(style);
        return style;
    }

    private void setBorder(CellStyle style) {
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
    }
}
