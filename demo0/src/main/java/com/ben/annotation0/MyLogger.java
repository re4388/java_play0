package com.ben.annotation0;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD})
public @interface MyLogger {
    LogPositionEnum value();

}


//@Target({ ElementType.ANNOTATION_TYPE, ElementType.METHOD })