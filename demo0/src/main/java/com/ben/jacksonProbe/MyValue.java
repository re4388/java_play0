package com.ben.jacksonProbe;

// Note: can use getters/setters as well; here we just use public fields directly:
public class MyValue {
    public String name;
    public int age;

    public MyValue(String name, int age) {
        this.name = name;
        this.age = age;
    }

    // NOTE: if using getters/setters, can keep fields `protected` or `private`
}