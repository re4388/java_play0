package com.ben;

import com.ben.math.Calculator;

public class QuickPlay {

    public static void main(String[] args) {
        Calculator calculator = new Calculator();
        int res = calculator.add(1, 2);
        System.out.println(res);
    }
}
