package com.ben.sprintbootexceldemo.service;

import com.ben.sprintbootexceldemo.metadata.ExcelSheetMeta;
import com.ben.sprintbootexceldemo.template.ExcelExportTemplate;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@Service
public class ExcelExportService {

    public void export(
            String fileName,
            List<ExcelSheetMeta<?>> sheets,
            HttpServletResponse response) throws IOException {

        ExcelExportTemplate template = new ExcelExportTemplate();
        Workbook wb = template.export(sheets);

        response.setContentType(
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader(
                "Content-Disposition",
                "attachment; filename=\"" + fileName + "\"");

        try (wb; OutputStream os = response.getOutputStream()) {
            wb.write(os);
        }
    }
}


