package com.ben.annotation0;


public class Test0 {

    public Test0(String name, int count) {
        this.name = name;
        this.count = count;
    }

    private String name; // OK

    private int count; // ❌ 會編譯錯誤：只能用在 String


    public static void main(String[] args) {

        Test0 ben = new Test0("ben", 1);
        System.out.println(ben.count);
        System.out.println(ben.name);

//        MyLogger annotation = Test0.class.getAnnotation(MyLogger.class);
//        System.out.println(annotation.value());
//


    }

    @MyLogger(value = LogPositionEnum.AT_BEGIN)
    public void helloWorld(){
        System.out.println("hello");
    }
}





