# ExcelDemo2 — Excel 匯出工具

> 支援兩種使用模式：**獨立 REST 服務** 或 **嵌入式 Library**

---

## 目錄

- [功能特色](#功能特色)
- [技術架構](#技術架構)
- [模式一：獨立 REST 服務](#模式一獨立-rest-服務)
- [模式二：引入為 Library](#模式二引入為-library)
- [API 格式說明](#api-格式說明)
- [欄位型別對照表](#欄位型別對照表)
- [執行測試](#執行測試)

---

## 功能特色

- ✅ 支援多種欄位型別：文字、數字、貨幣、百分比、日期、布林
- ✅ 自訂表頭、欄位、行高
- ✅ 自動調整欄寬 (`autoSizeColumns`)
- ✅ **HTTP Streaming**：使用 `StreamingResponseBody` 邊產生邊傳輸，不佔記憶體緩衝
- ✅ **POI Streaming**：使用 `SXSSFWorkbook` 滑動視窗模式，大資料量不 OOM
- ✅ 檔名支援中文（RFC 5987 UTF-8 編碼）

---

## 技術架構

```
POST /api/excel/export
        │
        ▼
ExcelController          ← StreamingResponseBody（HTTP Streaming）
        │
        ▼
ExcelService             ← SXSSFWorkbook(100)（POI Streaming，記憶體只保留 100 筆 row）
        │
        ▼
HTTP Response            ← Transfer-Encoding: chunked（邊產生邊傳輸）
```

**建置產物（`mvn package`）：**

| JAR | 大小 | 說明 |
|-----|------|------|
| `excelDemo2-x.x.x.jar` | ~16 KB | **Library 用** — plain jar，可被其他服務引入 |
| `excelDemo2-x.x.x-exec.jar` | ~36 MB | **Service 用** — fat jar，包含所有依賴，可直接執行 |

---

## 模式一：獨立 REST 服務

### 1. 建置與啟動

```bash
# 建置
./mvnw clean package -DskipTests

# 啟動（使用 fat jar）
java -jar target/excelDemo2-0.0.1-SNAPSHOT-exec.jar
```

服務預設啟動於 `http://localhost:8080`。

### 2. 呼叫 API

```bash
curl -X POST http://localhost:8080/api/excel/export \
  -H "Content-Type: application/json" \
  -d '{
    "setting": {
      "sheetName": "銷售報表",
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
      {"name":"李四","age":35,"salary":72000,"joinDate":"2019-07-01","active":false}
    ]
  }' \
  --output report.xlsx
```

### 3. 回應說明

```
HTTP/1.1 200
Content-Type: application/vnd.openxmlformats-officedocument.spreadsheetml.sheet
Transfer-Encoding: chunked          ← Streaming 傳輸，無需等待完整產生
Content-Disposition: attachment; filename="銷售報表_20260101_120000.xlsx"; filename*=UTF-8''...
```

---

## 模式二：引入為 Library

> 其他 Spring Boot 後端服務可直接引入此模組，透過 **Spring Boot Auto-configuration** 自動獲得 `ExcelService` bean，**無需任何額外設定**。

### 1. 安裝到本地 Maven Repository

```bash
cd excelDemo2
./mvnw install -DskipTests
```

### 2. 消費方加入依賴

在消費方的 `pom.xml` 加入（**不要加 classifier**，取得 plain jar 而非 fat jar）：

```xml
<dependencies>
    <!-- Excel 匯出 Library -->
    <dependency>
        <groupId>com.ben</groupId>
        <artifactId>excelDemo2</artifactId>
        <version>0.0.1-SNAPSHOT</version>
        <!-- ✅ 不加 classifier → 取得 16KB plain jar（Library 用）  -->
        <!-- ❌ 不要加 <classifier>exec</classifier> → 那是 fat jar   -->
    </dependency>
</dependencies>
```

### 3. 消費方直接注入使用

Auto-configuration 會自動在 Spring Context 中提供 `ExcelService` bean，消費方只需直接 `@Autowired`：

```java
import com.ben.exceldemo2.service.ExcelService;
import com.ben.exceldemo2.model.ExcelRequest;
import com.ben.exceldemo2.model.ExcelSetting;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ExcelService excelService;  // ← 自動注入，無需任何設定

    public void exportToFile(String filePath) throws IOException {
        // 建立設定
        ExcelSetting setting = new ExcelSetting();
        setting.setSheetName("月報");
        setting.setHeaders(List.of("產品", "數量", "金額"));
        setting.setColumnKeys(List.of("product", "qty", "amount"));
        setting.setColumnTypes(Map.of("qty", "integer", "amount", "currency"));
        setting.setAutoSizeColumns(true);

        // 準備資料
        List<Map<String, Object>> data = List.of(
            Map.of("product", "商品A", "qty", 100, "amount", 50000),
            Map.of("product", "商品B", "qty", 200, "amount", 80000)
        );

        ExcelRequest request = new ExcelRequest();
        request.setSetting(setting);
        request.setData(data);

        // 直接寫入 OutputStream（可以是 FileOutputStream、HttpServletResponse.getOutputStream() 等）
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            excelService.generateExcel(request, fos);
        }
    }
}
```

### 4. Auto-configuration 運作原理

```
消費方 Spring Boot 啟動
        │
        ▼
掃描所有 JAR 內的
META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports
        │
        ▼
發現 ExcelAutoConfiguration
        │
        ├─ @ConditionalOnMissingBean(ExcelService.class)
        │   └─ 若消費方沒有自己的 ExcelService bean → 自動建立
        │   └─ 若消費方有自己的 ExcelService bean   → 跳過（不重複建立）
        ▼
ExcelService bean 準備完成 ✅
```

---

## API 格式說明

### Request Body

```json
{
  "setting": {
    "sheetName":       "Sheet 名稱（字串）",
    "headers":         ["欄位標題1", "欄位標題2"],
    "columnKeys":      ["data_key1", "data_key2"],
    "columnTypes": {
      "data_key1":     "欄位型別（見下表）",
      "data_key2":     "currency"
    },
    "headerRowHeight": 25,
    "dataRowHeight":   20,
    "autoSizeColumns": true
  },
  "data": [
    { "data_key1": "值", "data_key2": 12345 }
  ]
}
```

### 欄位型別對照表

| `columnTypes` 值 | Excel 格式 | 範例輸入 | Excel 顯示 |
|-----------------|-----------|---------|-----------|
| `text` / `string` | 左對齊文字 | `"hello"` | `hello` |
| `number` / `integer` / `double` | `#,##0.00` | `1234` | `1,234.00` |
| `currency` / `money` | `$#,##0.00` | `55000` | `$55,000.00` |
| `percent` / `percentage` | `0.00%` | `75`（輸入 75 代表 75%）| `75.00%` |
| `date` | `yyyy-mm-dd` | `"2024-01-01"` | `2024-01-01` |
| `datetime` | `yyyy-mm-dd hh:mm:ss` | `"2024-01-01 12:00:00"` | `2024-01-01 12:00:00` |
| `boolean` | 左對齊文字 | `true` / `false` | `是` / `否` |
| （未指定）| 自動判斷 | 依 Java 型別推斷 | — |

---

## 執行測試

```bash
# 執行所有測試（含 E2E 整合測試，需能啟動 Spring Boot）
./mvnw test

# 只執行 E2E 測試
./mvnw test -Dtest=ExcelExportE2ETest
```

### E2E 測試涵蓋項目

| # | 測試名稱 | 驗證內容 |
|---|---------|---------|
| 1 | `shouldReturn200` | HTTP 狀態碼 200 |
| 2 | `shouldReturnXlsxContentType` | Content-Type 為 xlsx MIME type |
| 3 | `shouldUseChunkedTransferEncoding` | `Transfer-Encoding: chunked`（HTTP Streaming） |
| 4 | `shouldHaveCorrectContentDisposition` | RFC 5987 UTF-8 編碼的 Content-Disposition |
| 5 | `shouldReturnValidXlsxFile` | 回應 body 可被 POI 解析為合法 xlsx |
| 6 | `shouldHaveCorrectSheetName` | Sheet 名稱正確 |
| 7 | `shouldHaveCorrectHeaders` | Header row 欄位名稱正確 |
| 8 | `shouldHaveCorrectRowCount` | 資料筆數正確 |
| 9 | `shouldHaveCorrectData` | 資料值與型別轉換正確 |
