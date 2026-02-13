package com.ben;
import processing.core.PApplet;

// https://processing.org/tutorials/

public class Main extends PApplet {

    // 視窗大小
    public void settings() {
        size(400, 400);
    }

    // 初始化
    public void setup() {
        background(0);
    }

    // 每幀更新
    public void draw() {
        fill(255);
        ellipse(mouseX, mouseY, 50, 50); // 小球跟隨滑鼠
    }

    // main 函式
    public static void main(String[] args) {
        PApplet.main("com.ben.Main"); // 注意這裡是 "完整類名"，含 package
    }
}

