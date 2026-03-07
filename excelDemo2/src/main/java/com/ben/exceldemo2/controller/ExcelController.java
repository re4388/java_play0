package com.ben.exceldemo2.controller;

import com.ben.exceldemo2.model.ExcelRequest;
import com.ben.exceldemo2.service.ExcelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@RestController
@RequestMapping("/api/excel")
@RequiredArgsConstructor
public class ExcelController {

    private static final MediaType XLSX_MEDIA_TYPE = MediaType.parseMediaType(
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

    private final ExcelService excelService;

    @PostMapping("/export")
    public ResponseEntity<StreamingResponseBody> exportExcel(@Valid @RequestBody ExcelRequest request) {
        String filename = generateFilename(request.getSetting().getSheetName());

        log.info("Excel export requested, sheetName={}, rowCount={}",
                request.getSetting().getSheetName(),
                request.getData() != null ? request.getData().size() : 0);

        // StreamingResponseBody：Spring 會在獨立執行緒中執行 lambda，
        // 邊產生 Excel 邊把資料寫入 HTTP response outputStream，
        // 不需要先把整個檔案載入記憶體。
        // 這裡不包 try-catch 後再轉成 RuntimeException，因為 response header/status
        // 很可能已經送出，重新拋出也無法再轉成正常 JSON error response。
        StreamingResponseBody body = outputStream -> excelService.generateExcel(request, outputStream);

        // 使用 ContentDisposition builder 做 RFC 5987 UTF-8 編碼，
        // 確保含中文等非 ASCII 字元的檔名能正確傳遞。
        // 產生格式：attachment; filename="xxx.xlsx"; filename*=UTF-8''xxx.xlsx
        String contentDisposition = ContentDisposition.attachment()
                .filename(filename, StandardCharsets.UTF_8)
                .build()
                .toString();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .contentType(XLSX_MEDIA_TYPE)
                .body(body);
    }

    private String generateFilename(String sheetName) {
        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        return (sheetName != null ? sheetName : "export") + "_" + timestamp + ".xlsx";
    }
}
