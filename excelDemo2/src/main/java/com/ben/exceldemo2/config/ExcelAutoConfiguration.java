package com.ben.exceldemo2.config;

import com.ben.exceldemo2.service.ExcelService;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * Excel Library 的 Spring Boot Auto-configuration。
 *
 * 當其他後端服務將此模組作為 library 引入時，
 * 此類別會自動向 Spring Context 提供 ExcelService bean，
 * 消費方無需額外設定或 @ComponentScan。
 *
 * 運作原理（Spring Boot 3.x+）：
 *   Spring Boot 啟動時會掃描所有 JAR 內的：
 *   META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports
 *   並自動套用其中列出的 @AutoConfiguration 類別。
 *
 * 使用方式（消費方）：
 *   1. 在 pom.xml 加入依賴（使用不含 -exec classifier 的 plain jar）
 *   2. 直接 @Autowired ExcelService 即可使用
 *
 * 本服務自身的 Service 模式：
 *   @SpringBootApplication 的 component scan 會先掃描到 @Service ExcelService，
 *   @ConditionalOnMissingBean 確保不會重複建立 bean。
 */
@AutoConfiguration
public class ExcelAutoConfiguration {

    /**
     * 提供 ExcelService bean。
     * 若 Spring Context 中已存在 ExcelService（例如本服務自身透過 @Service 建立），
     * 則此 @Bean 方法不會執行（@ConditionalOnMissingBean 保護）。
     */
    @Bean
    @ConditionalOnMissingBean
    public ExcelService excelService() {
        return new ExcelService();
    }
}
