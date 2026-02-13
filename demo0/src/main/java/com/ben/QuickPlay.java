package com.ben;

import java.util.Arrays;

public class QuickPlay {

    public static void main(String[] args) {

        String a1 = "13in";
        String[] split = a1.split("in");
        for (String s: split){
            System.out.println(s);
        }
    }
}
