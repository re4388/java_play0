package com.ben;

import org.apache.commons.lang3.exception.ExceptionUtils;

import java.sql.SQLException;

public class ExceptionUtilsDemo {

    public static void main(String[] args) {
        try {
            controller();
        } catch (Exception ex) {

            System.out.println("========== printStackTrace ==========");
            ex.printStackTrace();

            System.out.println("\n========== ExceptionUtils ==========");

            // 1️⃣ 取得 root cause
            Throwable root = ExceptionUtils.getRootCause(ex);
            System.out.println("Root cause: " + root);

            // 2️⃣ 判斷是否含有特定例外
            boolean hasSqlEx =
                    ExceptionUtils.indexOfThrowable(ex, SQLException.class) != -1;
            System.out.println("Contains SQLException? " + hasSqlEx);

            // 3️⃣ 列出整個 cause chain（由外到內）
            System.out.println("\nCause chain:");
            ExceptionUtils.getThrowableList(ex)
                    .forEach(t -> System.out.println(" - " + t));

            // 4️⃣ stack trace 轉成 String（可寫入 log / 傳送）
            String stackTrace = ExceptionUtils.getStackTrace(ex);
            System.out.println("\nStackTrace as String:\n" + stackTrace);
        }
    }

    static void controller() {
        service();
    }

    static void service() {
        try {
            repository();
        } catch (SQLException e) {
            throw new IllegalStateException("Service failed", e);
        }
    }

    static void repository() throws SQLException {
        throw new SQLException("DB connection timeout");
    }
}