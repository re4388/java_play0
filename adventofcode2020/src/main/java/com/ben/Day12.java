package com.ben;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;


/**
 * F10
 * N3
 * F7
 * R90
 * F11
 */

public class Day12 {
    public static void main(String[] args) throws IOException {
        List<String> lines = FileUtils.readLines(new File("day12_big.txt"), StandardCharsets.UTF_8);
        System.out.println(lines);

        // The ship starts by facing east
        Navigator navigator = new Navigator(0, 0, 0);


        for (String line : lines) {
            String action = line.substring(0, 1);
            int step = Integer.parseInt(line.substring(1));
            System.out.println("action: " + action + " step: " + step);


            /**
             * Action N means to move north by the given value.
             * Action S means to move south by the given value.
             * Action E means to move east by the given value.
             * Action W means to move west by the given value.
             *
             * Action L means to turn left the given number of degrees.
             * Action R means to turn right the given number of degrees.
             *
             * Action F means to move forward by the given value in the direction the
             */

            if (action.equals("F")) {
                navigator.forward(step);
                System.out.println(navigator);
            } else if (action.equals("L")) {
                navigator.turnLeft(step);
                System.out.println(navigator);
            } else if (action.equals("R")) {
                navigator.turnRight(step);
                System.out.println(navigator);
            } else if (action.equals("N")) {
                navigator.moveNorth(step);
                System.out.println(navigator);
            } else if (action.equals("S")) {
                navigator.moveSouth(step);
                System.out.println(navigator);
            } else if (action.equals("E")) {
                navigator.moveEast(step);
                System.out.println(navigator);
            } else if (action.equals("W")) {
                navigator.moveWest(step);
                System.out.println(navigator);
            } else {
                throw new Error("NO THIS ACTION");
            }
        }


        double manhattanDistFromOrigin = navigator.getManhattanDistFromOrigin();
        System.out.println(manhattanDistFromOrigin);


    }
}

class Navigator {
    private double x;
    private double y;
    private double heading;

    public Navigator(double x, double y, double heading) {
        this.x = x;
        this.y = y;
        this.heading = heading;
    }

    public void moveByXandY(double x1, double y1) {
        x += x1;
        y += y1;
    }


    public void moveNorth(int step) {
        y += step;
    }

    public void moveSouth(int step) {
        y -= step;
    }

    public void moveWest(int step) {
        x -= step;
    }

    public void moveEast(int step) {
        x += step;
    }

    public double getManhattanDistFromOrigin() {
        double result = Math.abs(x) + Math.abs(y);
        return Math.round(result * 100.0) / 100.0;
    }


    public void forward(double step) {
        double radians = Math.toRadians(heading);
        x += step * Math.cos(radians);
        y += step * Math.sin(radians);
    }

    void rotateRight(int degree) {
        int times = degree / 90;
        for (int i = 0; i < times; i++) {
            double oldX = x;
            double oldY = y;
            x = oldY;
            y = -oldX;
        }
    }

    void rotateLeft(int degree) {
        int times = degree / 90;
        for (int i = 0; i < times; i++) {
            double oldX = x;
            double oldY = y;
            x = -oldY;
            y = oldX;
        }
    }

    public void turnLeft(double degree) {
        heading = (heading + degree) % 360;
    }

    public void turnRight(double degree) {
        this.heading = (heading - degree + 360) % 360;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getHeading() {
        return heading;
    }

    public void setHeading(double heading) {
        this.heading = heading;
    }

    @Override
    public String toString() {
        return String.format("x=%.2f, y=%.2f, heading=%.1f°", x, y, heading);
    }
}


