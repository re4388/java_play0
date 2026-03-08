package com.ben.excel.service;

import com.ben.excel.core.annotation.ExcelColumn;
import com.ben.excel.core.metadata.ExcelSheetMeta;
import com.ben.excel.core.template.ExcelExportTemplate;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/excel")
public class ExcelServiceController {

    @GetMapping("/download")
    public void download(HttpServletResponse response) throws IOException {
        List<UserDTO> data = createMockData();

        ExcelSheetMeta<UserDTO> meta = new ExcelSheetMeta<>(
                "Service Users",
                data,
                UserDTO.class,
                Set.of("id", "fullName", "email")
        );
        ExcelExportTemplate template = new ExcelExportTemplate();
        Workbook wb = template.export(List.of(meta));
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=service_export.xlsx");

        try (OutputStream os = response.getOutputStream()) {
            wb.write(os);
            wb.close();
        }
    }

    @NonNull
    private static List<UserDTO> createMockData() {
        List<UserDTO> data = Arrays.asList(
                new UserDTO(101L, "System Admin", "admin@service.com"),
                new UserDTO(102L, "Excel Bot", "bot@service.com")
        );
        return data;
    }

    public static class UserDTO {
        @ExcelColumn(header = "UID", order = 1)
        private Long id;
        @ExcelColumn(header = "Full Name", order = 2)
        private String fullName;
        @ExcelColumn(header = "Email Address", order = 3)
        private String email;

        public UserDTO(Long id, String fullName, String email) {
            this.id = id;
            this.fullName = fullName;
            this.email = email;
        }
        // Getters needed for reflection if not using setAccessible(true) but I used it in template
        public Long getId() { return id; }
        public String getFullName() { return fullName; }
        public String getEmail() { return email; }
    }
}
