package com.ben.sprintbootexceldemo.controller;

import com.ben.sprintbootexceldemo.excel.ExcelColumn;
import com.ben.sprintbootexceldemo.metadata.ExcelSheetMeta;
import com.ben.sprintbootexceldemo.model.User;
import com.ben.sprintbootexceldemo.model.UserSummary;
import com.ben.sprintbootexceldemo.service.ExcelExportService;
import com.ben.sprintbootexceldemo.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/users")
public class UserExportController {

    private final UserService userService;
    private final ExcelExportService excelExportService;

    public UserExportController(UserService userService,
                                ExcelExportService excelExportService) {
        this.userService = userService;
        this.excelExportService = excelExportService;
    }

    @GetMapping("/export")
    public void exportReport(HttpServletResponse response) throws IOException {

        // Sheet 1：使用者清單
        List<User> users = userService.findAllUsers();

        ExcelSheetMeta<User> userSheet =
                new ExcelSheetMeta<>(
                        "Users",
                        users,
                        User.class,
                        Set.of("id", "name", "email") // 動態欄位
                );

        // Sheet 2：統計資料（只有一筆）
        UserSummary summary =
                new UserSummary(
                        users.size(),
                        users.size() - 2,
                        LocalDateTime.now()
                );

        ExcelSheetMeta<UserSummary> summarySheet =
                new ExcelSheetMeta<>(
                        "Summary",
                        List.of(summary),
                        UserSummary.class,
                        null // 全欄位
                );

        excelExportService.export(
                "report.xlsx",
                List.of(userSheet, summarySheet),
                response
        );
    }
}
