package com.ben.annotation0;

//import jdk.internal.reflect.CallerSensitive;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class Main {



    public static void main(String[] args) {
        HelloService service = (HelloService)Proxy.newProxyInstance(
                HelloService.class.getClassLoader(),
                new Class[]{HelloService.class},
                new HelloInvocationHandler(new HelloServiceImpl())
        );

        System.out.println(service.helloWorld());


    }
}
