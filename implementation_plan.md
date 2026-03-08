# Revised Implementation Plan: Modularization & New Service

[Overview]
將 `sprintBootExcelDemo` 的 Excel 核心邏輯抽離為獨立 Library (`excel-core`)，移除 `demo0` 中的暫時性實作，並建立一個全新的 Spring Boot Module (`excel-service-app`) 作為正式的 Backend Service。

[Architecture]
1.  **excel-core (Library)**: 純淨的 Excel 工具包 (Annotation, Template, Exporter)，不相依於 Spring Web。
2.  **excel-service-app (New Service)**: 獨立的 Spring Boot 應用程式，引用 `excel-core` 並提供 HTTP 接口。
3.  **demo0**: 回歸原始狀態，移除 Excel 相關實作。

[Steps]
1.  **拆分 Library**:
    *   建立 `sprintBootExcelDemo/excel-core` 目錄。
    *   搬移核心邏輯：`ExcelColumn`, `ExcelSheetMeta`, `ExcelExportTemplate`, `ExcelMetaResolver`。
    *   調整 `ExcelExportTemplate` 使其不相依於 `HttpServletResponse` (改用 `OutputStream`)。
2.  **清理舊實作**:
    *   刪除 `demo0/src/main/java/com/ben/UseExcelLibraryDemo` 資料夾。
    *   還原 `demo0/pom.xml` 與 `application.properties` (選擇性，或移除 Bean Override)。
3.  **建立新 Module (`excel-service-app`)**:
    *   在根目錄下建立 `excel-service-app` 資料夾與 Maven 結構。
    *   配置 `pom.xml` 引用 `excel-core`。
    *   建立 Spring Boot 啟動類、Controller 與 DTO。
4.  **驗證**:
    *   安裝 `excel-core` 到本地 Maven 倉庫。
    *   啟動 `excel-service-app` 並測試 Excel 下載。

[Technical Notes]
*   **Decoupling**: `excel-core` 應避免掃描 Spring Component，改由應用層進行 Bean 初始化。
*   **Port Management**: `excel-service-app` 預設使用不同於 `demo0` 的埠號 (如 8082)。
