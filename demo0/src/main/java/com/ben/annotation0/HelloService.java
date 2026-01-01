package com.ben.annotation0;

public interface HelloService {
    @MyLogger(LogPositionEnum.AT_BEGIN)
    String helloWorld();
}
