package com.ben.annotation0;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class HelloInvocationHandler implements InvocationHandler {

    private final Object target;

    public HelloInvocationHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        MyLogger myLogger = method.getAnnotation(MyLogger.class);

        if(myLogger != null && myLogger.value() == LogPositionEnum.AT_BEGIN){
            System.out.println("AT_BEGIN" + method.getName());
        }

        Object res = method.invoke(target, args);

        if(myLogger != null && myLogger.value() == LogPositionEnum.AT_END){
            System.out.println("AT_END" + method.getName());
        }

        return res;
    }
}
