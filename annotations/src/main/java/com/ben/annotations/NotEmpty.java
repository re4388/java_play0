package com.ben.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD) // 用在欄位
@Retention(RetentionPolicy.SOURCE) // 編譯期即可被處理
public @interface NotEmpty {
}