package com.ben;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Day12_2 {
    public static void main(String[] args) throws IOException {
        List<String> lines = FileUtils.readLines(new File("day12_big.txt"), StandardCharsets.UTF_8);

        // The ship starts by facing east
        Navigator ship = new Navigator(0, 0, 0);
        Navigator waypoint = new Navigator(10, 1, 0);


        for (String line : lines) {
            String action = line.substring(0, 1);
            int step = Integer.parseInt(line.substring(1));

            /**
             * Action N means to move the waypoint north by the given value.
             * Action S means to move the waypoint south by the given value.
             * Action E means to move the waypoint east by the given value.
             * Action W means to move the waypoint west by the given value.
             * Action L means to rotate the waypoint around the ship left (counter-clockwise) the given number of degrees.
             * Action R means to rotate the waypoint around the ship right (clockwise) the given number of degrees.
             * Action F means to move forward to the waypoint a number of times equal to the given value.
             */

            if (action.equals("F")) {
                double x = waypoint.getX() * step;
                double y = waypoint.getY() * step;
                ship.moveByXandY(x, y);

                System.out.println("ship: " + ship);
                System.out.println("waypoint: " + waypoint);

            } else if (action.equals("L")) {
                waypoint.rotateLeft(step);
                System.out.println("ship: " + ship);
                System.out.println("waypoint: " + waypoint);
            } else if (action.equals("R")) {
                waypoint.rotateRight(step);
                System.out.println("ship: " + ship);
                System.out.println("waypoint: " + waypoint);
            } else if (action.equals("N")) {
                waypoint.moveNorth(step);
                System.out.println("ship: " + ship);
                System.out.println("waypoint: " + waypoint);
            } else if (action.equals("S")) {
                waypoint.moveSouth(step);
                System.out.println("ship: " + ship);
                System.out.println("waypoint: " + waypoint);
            } else if (action.equals("E")) {
                waypoint.moveEast(step);
                System.out.println("ship: " + ship);
                System.out.println("waypoint: " + waypoint);
            } else if (action.equals("W")) {
                waypoint.moveWest(step);
                System.out.println("ship: " + ship);
                System.out.println("waypoint: " + waypoint);
            } else {
                throw new Error("NO THIS ACTION");
            }
        }


        double manhattanDistFromOrigin = ship.getManhattanDistFromOrigin();
        System.out.println(manhattanDistFromOrigin);


    }
}


