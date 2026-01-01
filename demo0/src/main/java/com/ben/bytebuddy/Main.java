package com.ben.bytebuddy;

import com.ben.annotation0.HelloService;
import com.ben.annotation0.HelloServiceImpl;
import com.ben.annotation0.MyLogger;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.reflect.InvocationTargetException;

public class Main {
    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        HelloService service = new ByteBuddy()
                .subclass(HelloServiceImpl.class)
                .method(ElementMatchers.isAnnotatedWith(MyLogger.class))
                .intercept(net.bytebuddy.implementation.MethodDelegation.to(LogAdvice.class))
                .make()
                .load(HelloService.class.getClassLoader())
                .getLoaded()
                .getDeclaredConstructor()
                .newInstance();

        service.helloWorld();
    }
}
