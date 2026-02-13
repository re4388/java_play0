package com.ben;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Day11_2 {

    private static void testRun(String[][] grid) {


        int[] dr = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] dc = {-1, 0, 1, -1, 1, -1, 0, 1};
//
//        int r = 1;
//        int c = 1;
//        int r = 4;
//        int c = 3;
        int r = 3;
        int c = 3;

        List<List<Integer>> mark1 = new ArrayList<>(); // TODO: use set when algo is correct
        if (Objects.equals(grid[r][c], "L")) {
            for (int i = 0; i < 8; i++) {
                int nr = r + dr[i];
                int nc = c + dc[i];
                if (seeOccupied(new int[]{nr, nc}, grid, r, c)) {
                    mark1.add(List.of(r, c));
                }
            }
        }

        System.out.println(mark1);
    }

    public static void main(String[] args) throws IOException {
//        String[][] grid = getGridFromFile("day11_small.txt");
        String[][] grid = getGridFromFile("day11_big.txt");
        run(grid);
    }


    private static void run(String[][] grid) {
        List<List<Integer>> mark = new ArrayList<>();
        int rows = grid.length;
        int cols = grid[0].length;


//        for (int i = 0; i < 1  ; i++) {

        for (;; ) {

            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    updateMark1(mark, grid, r, c);
                }
            }
            applyMark(grid, mark, "#");
            if (mark.isEmpty()) {
                break;
            }
            mark.clear();


            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    markRule2(mark, grid, r, c);
                }
            }
            applyMark(grid, mark, "L");
            if (mark.isEmpty()) {
                break;
            }
            mark.clear();

        }


        int res = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (Objects.equals(grid[r][c], "#")) res++;
            }
        }

        System.out.println(res);
    }


    private static boolean seeOccupied(int[] dir, String[][] grid, int baseR, int baseC) {
        int rows = grid.length;
        int cols = grid[0].length;
        int nr = dir[0];
        int nc = dir[1];

        while (true) {

            if ((nr < 0 || nr >= rows || nc < 0 || nc >= cols)) {
                return false;
            }

            if (Objects.equals(grid[nr][nc], "L")) {
                return false;
            }


            if (Objects.equals(grid[nr][nc], "#")) {
                return true;
            }


            if (nr < baseR && nc == baseC) {
                nr--;
            }

            if (nr < baseR && nc < baseC) {
                nr--;
                nc--;
            }

            if (nr == baseR && nc < baseC) {
                nc--;
            }

            if (nr > baseR && nc < baseC) {
                nr++;
                nc--;
            }


            if (nr > baseR && nc == baseC) {
                nr++;
            }

            if (nr > baseR && nc > baseC) {
                nr++;
                nc++;
            }

            if (nr == baseR && nc > baseC) {
                nc++;
            }

            if (nr < baseR && nc > baseC) {
                nr--;
                nc++;
            }
        }
    }


    private static void applyMark(String[][] grid, List<List<Integer>> mark, String symbol) {
        for (List<Integer> integers : mark) {
            int r = integers.get(0);
            int c = integers.get(1);
            grid[r][c] = symbol;
        }
    }


    // If a seat is empty (L) and there are no occupied seats adjacent to it, the seat becomes occupied.
    private static void updateMark1(List<List<Integer>> mark, String[][] grid, int r, int c) {
        int[] dr = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] dc = {-1, 0, 1, -1, 1, -1, 0, 1};

        if (isEqual(grid[r][c], "#") || isEqual(grid[r][c], ".")) return;;

        int count = 0;
        if (Objects.equals(grid[r][c], "L")) {
            for (int i = 0; i < 8; i++) {
                int nr = r + dr[i];
                int nc = c + dc[i];
                if (seeOccupied(new int[]{nr, nc}, grid, r, c)) {
                    count++;
                }
            }
        }

        if(count == 0){
            mark.add(List.of(r, c));
        }
    }

//    Objects.equals(grid[r][c], "#")

    private static boolean isEqual(String a, String b) {
        return Objects.equals(a, b);
    }

    //    If a seat is occupied (#) and four or more seats adjacent to it are also occupied, the seat becomes empty.
    private static void markRule2(List<List<Integer>> mark, String[][] grid, int r, int c) {
        if (Objects.equals(grid[r][c], "#")) {
            if (countSeeOccupied(grid, r, c) >= 5) {
                mark.add(List.of(r, c));
            }
        }
    }

    private static int countSeeOccupied(String[][] grid, int r, int c) {
        int[] dr = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] dc = {-1, 0, 1, -1, 1, -1, 0, 1};

        int count = 0;
        for (int i = 0; i < 8; i++) {
            int nr = r + dr[i];
            int nc = c + dc[i];
            if (seeOccupied(new int[]{nr, nc}, grid, r, c)) count++;
        }

        return count;
    }


    private static String[][] getGridFromFile(String path) throws IOException {
        List<String> lines = FileUtils.readLines(new File(path),
                StandardCharsets.UTF_8);
        int rows = lines.size();
        String[][] grid = new String[rows][];
        for (int i = 0; i < rows; i++) {
            String line = lines.get(i);
            String[] split = line.split("");
            grid[i] = split;
        }
        return grid;
    }


    private static void mutateGridOneCell(String[][] grid, int row, int col, String input) {
        int rows = grid.length;
        int cols = grid[0].length;
        for (int r = 0; r < rows; r++) {
            System.out.println("row " + r + "");
            for (int c = 0; c < cols; c++) {
                if (r == row && c == col) {
                    grid[r][c] = input;
                }
            }
        }
    }


    private static void iterateGrid(String[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;
        for (int r = 0; r < rows; r++) {
            System.out.println("row " + r + "");
            for (int c = 0; c < cols; c++) {
                System.out.println(grid[r][c]);
            }
        }
    }
}
