package com.ben.poi_test.poc;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jetbrains.annotations.NotNull;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class PoiTest {
    public static void main(String[] args) throws IOException {
//        Workbook workbook = createSingleCell();
        Workbook workbook = createUser();

        FileOutputStream fos = new FileOutputStream("test2.xlsx");
        workbook.write(fos);
        fos.close();
        workbook.close();

    }

    private record User(String id, String name, String email){}


    private static Workbook createUser() {

        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("Users");

        Row headerRow = sheet.createRow(0);
        String[] headers = {"ID", "姓名", "Email"};
        for (int i = 0; i < headers.length; i++) {
            headerRow.createCell(i).setCellValue(headers[i]);
        }

        // Data
        List<User> users = List.of(
                new User("1", "ben", "xx@gmail"),
                new User("2", "ben2", "xx2@gmail")
        );

        // i begin from 1
        for (int i = 1; i <= users.size(); i++) {
            Row row = sheet.createRow(i);
            User user = users.get(i - 1);
            row.createCell(0).setCellValue(user.id());
            row.createCell(1).setCellValue(user.name());
            row.createCell(2).setCellValue(user.email());
        }

        return wb;
    }

    @NotNull
    private static Workbook createSingleCell() {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet1");

        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue("Hello POI");
        return workbook;
    }

}
