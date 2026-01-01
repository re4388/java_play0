package com.ben.bytebuddy;

import com.ben.annotation0.LogPositionEnum;
import com.ben.annotation0.MyLogger;
import net.bytebuddy.asm.Advice;

import java.lang.reflect.Method;

public class LogAdvice {

    @Advice.OnMethodEnter
    static void enter(@Advice.Origin Method method) {
        MyLogger myLogger = method.getAnnotation(MyLogger.class);

        if (myLogger != null && myLogger.value() == LogPositionEnum.AT_BEGIN) {
            System.out.println("[LOG] Begin: " + method.getName());
        }

    }

    @Advice.OnMethodExit
    static void exit(@Advice.Origin Method method) {
        MyLogger myLogger = method.getAnnotation(MyLogger.class);

        if (myLogger != null && myLogger.value() == LogPositionEnum.AT_END) {
            System.out.println("[LOG] End: " + method.getName());
        }
    }
}