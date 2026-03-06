package com.ben.exceldemo2;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.io.ByteArrayInputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * E2E 整合測試：啟動完整 Spring Boot Server，透過 JDK HttpClient 發出真實 HTTP 呼叫。
 *
 * 驗證項目：
 *  1. HTTP 狀態碼 200
 *  2. Content-Type 為 xlsx MIME type
 *  3. Transfer-Encoding: chunked（確認 StreamingResponseBody 生效）
 *  4. Content-Disposition 含正確 filename
 *  5. 回應 body 是有效的 xlsx 檔案（可被 POI 解析）
 *  6. Sheet 名稱正確
 *  7. Header row 內容正確
 *  8. 資料 row 數量正確
 *  9. 資料內容正確（抽樣驗證）
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ExcelExportE2ETest {

    @LocalServerPort
    private int port;

    private HttpClient httpClient;

    private static final String REQUEST_BODY = """
            {
              "setting": {
                "sheetName": "E2E測試",
                "headers": ["姓名", "年齡", "薪資", "入職日期", "是否在職"],
                "columnKeys": ["name", "age", "salary", "joinDate", "active"],
                "columnTypes": {
                  "age":     "integer",
                  "salary":  "currency",
                  "joinDate":"date",
                  "active":  "boolean"
                },
                "headerRowHeight": 25,
                "dataRowHeight":   20,
                "autoSizeColumns": true
              },
              "data": [
                {"name":"張三","age":28,"salary":55000,"joinDate":"2022-03-15","active":true},
                {"name":"李四","age":35,"salary":72000,"joinDate":"2019-07-01","active":true},
                {"name":"王五","age":25,"salary":48000,"joinDate":"2023-11-20","active":false}
              ]
            }
            """;

    @BeforeEach
    void setUp() {
        httpClient = HttpClient.newHttpClient();
    }

    private HttpResponse<byte[]> callExportApi() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + "/api/excel/export"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(REQUEST_BODY))
                .build();

        return httpClient.send(request, HttpResponse.BodyHandlers.ofByteArray());
    }

    // ===== 測試案例 =====

    @Test
    @DisplayName("1. HTTP 狀態碼應為 200")
    void shouldReturn200() throws Exception {
        assertThat(callExportApi().statusCode()).isEqualTo(200);
    }

    @Test
    @DisplayName("2. Content-Type 應為 xlsx MIME type")
    void shouldReturnXlsxContentType() throws Exception {
        Optional<String> contentType = callExportApi().headers()
                .firstValue("content-type");

        assertThat(contentType).isPresent();
        assertThat(contentType.get())
                .contains("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    }

    @Test
    @DisplayName("3. Transfer-Encoding 應為 chunked（StreamingResponseBody 生效）")
    void shouldUseChunkedTransferEncoding() throws Exception {
        HttpResponse<byte[]> response = callExportApi();

        List<String> transferEncoding = response.headers().allValues("transfer-encoding");
        assertThat(transferEncoding)
                .as("Transfer-Encoding 應為 chunked，代表 HTTP Streaming 有效")
                .isNotEmpty()
                .contains("chunked");

        // Streaming 模式下 Server 不知道最終大小，不會回傳 Content-Length
        Optional<String> contentLength = response.headers().firstValue("content-length");
        assertThat(contentLength)
                .as("Streaming 模式下不應有 Content-Length")
                .isEmpty();
    }

    @Test
    @DisplayName("4. Content-Disposition 應使用 RFC 5987 UTF-8 編碼格式")
    void shouldHaveCorrectContentDisposition() throws Exception {
        Optional<String> disposition = callExportApi().headers()
                .firstValue("content-disposition");

        assertThat(disposition)
                .as("Content-Disposition header 應存在")
                .isPresent();

        String value = disposition.get();
        // Spring ContentDisposition.attachment().filename(name, UTF_8) 產生格式：
        // attachment; filename="..."; filename*=UTF-8''...
        assertThat(value)
                .as("應為 attachment 類型")
                .startsWith("attachment;");
        assertThat(value)
                .as("應包含 RFC 5987 UTF-8 編碼的 filename*")
                .contains("filename*=UTF-8''");
        assertThat(value)
                .as("應以 .xlsx 結尾")
                .endsWith(".xlsx");
    }

    @Test
    @DisplayName("5. 回應 body 應為有效的 xlsx 檔案（可被 POI 解析）")
    void shouldReturnValidXlsxFile() throws Exception {
        byte[] body = callExportApi().body();

        assertThat(body).isNotNull().isNotEmpty();

        try (Workbook workbook = new XSSFWorkbook(new ByteArrayInputStream(body))) {
            assertThat(workbook.getNumberOfSheets()).isGreaterThanOrEqualTo(1);
        }
    }

    @Test
    @DisplayName("6. Sheet 名稱應為 'E2E測試'")
    void shouldHaveCorrectSheetName() throws Exception {
        byte[] body = callExportApi().body();
        try (Workbook workbook = new XSSFWorkbook(new ByteArrayInputStream(body))) {
            assertThat(workbook.getSheetAt(0).getSheetName()).isEqualTo("E2E測試");
        }
    }

    @Test
    @DisplayName("7. Header row 應包含正確的欄位名稱")
    void shouldHaveCorrectHeaders() throws Exception {
        byte[] body = callExportApi().body();
        try (Workbook workbook = new XSSFWorkbook(new ByteArrayInputStream(body))) {
            Sheet sheet = workbook.getSheetAt(0);
            Row headerRow = sheet.getRow(0);

            assertThat(headerRow.getCell(0).getStringCellValue()).isEqualTo("姓名");
            assertThat(headerRow.getCell(1).getStringCellValue()).isEqualTo("年齡");
            assertThat(headerRow.getCell(2).getStringCellValue()).isEqualTo("薪資");
            assertThat(headerRow.getCell(3).getStringCellValue()).isEqualTo("入職日期");
            assertThat(headerRow.getCell(4).getStringCellValue()).isEqualTo("是否在職");
        }
    }

    @Test
    @DisplayName("8. 資料 row 數量應為 3 筆（不含 header）")
    void shouldHaveCorrectRowCount() throws Exception {
        byte[] body = callExportApi().body();
        try (Workbook workbook = new XSSFWorkbook(new ByteArrayInputStream(body))) {
            Sheet sheet = workbook.getSheetAt(0);
            // lastRowNum 是 0-indexed：header(row 0) + 3 data rows(1,2,3) → lastRowNum = 3
            assertThat(sheet.getLastRowNum()).isEqualTo(3);
        }
    }

    @Test
    @DisplayName("9. 資料內容應正確（抽樣驗證第一筆與最後一筆）")
    void shouldHaveCorrectData() throws Exception {
        byte[] body = callExportApi().body();
        try (Workbook workbook = new XSSFWorkbook(new ByteArrayInputStream(body))) {
            Sheet sheet = workbook.getSheetAt(0);

            // 第一筆：張三
            Row row1 = sheet.getRow(1);
            assertThat(row1.getCell(0).getStringCellValue()).isEqualTo("張三");
            assertThat(row1.getCell(1).getNumericCellValue()).isEqualTo(28.0);
            assertThat(row1.getCell(2).getNumericCellValue()).isEqualTo(55000.0);
            assertThat(row1.getCell(3).getStringCellValue()).isEqualTo("2022-03-15");
            assertThat(row1.getCell(4).getStringCellValue()).isEqualTo("是");

            // 最後一筆：王五（active = false → "否"）
            Row row3 = sheet.getRow(3);
            assertThat(row3.getCell(0).getStringCellValue()).isEqualTo("王五");
            assertThat(row3.getCell(4).getStringCellValue()).isEqualTo("否");
        }
    }
}
