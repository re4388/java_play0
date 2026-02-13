package com.ben;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Day11 {

    public static void main(String[] args) throws IOException {
        String[][] grid = getGridFromFile("day11_big.txt");

//      mutateGridOneCell(grid, 2, 3, "X");
//        System.out.println(grid);


        run(grid);
//        System.out.println(grid);


    }



    private static boolean seeOccupied(int[] dir, String[][] grid){

        int rows = grid.length;
        int cols = grid[0].length;
        int r = dir[0];
        int c = dir[1];

        while(true){

            if ((r < 0 || r >= rows  || c < 0 || c >= cols )) break;

            if(Objects.equals(grid[r][c], "#")){
                return true;
            }


            if(r < 0 && c == 0){
                r--;
            }

            if(r < 0 && c < 0){
                r--;
                c--;
            }

            if(r == 0 && c < 0){
                c--;
            }

            if(r > 0 && c < 0){
                r++;
                c--;
            }


            if(r > 0 && c == 0){
                r++;
            }

            if(r > 0 && c > 0){
                r++;
                c++;
            }

            if(r == 0 && c > 0){
                c++;
            }

            if(r < 0 && c > 0){
                r--;
                c++;
            }
        }

        return false;
    }


    private static void run(String[][] grid) {
        List<List<Integer>> mark = new ArrayList<>();
        int rows = grid.length;
        int cols = grid[0].length;

        for (;;) {


            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    markRule1(mark, grid, r, c);
                }
            }
            applyMark(grid, mark, "#");
            if(mark.size()==0) {
                break;
            }
            mark.clear();



            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    markRule2(mark, grid, r, c);
                }
            }
            applyMark(grid, mark, "L");
            if(mark.size()==0) {
                break;
            }
            mark.clear();

//            System.out.println(grid);

        }



        int res = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if(grid[r][c]=="#") res++;
            }
        }

        System.out.println(res);
    }

    private static void applyMark(String[][] grid, List<List<Integer>> mark, String symbol) {
        for (List<Integer> integers : mark) {
            int r = integers.get(0);
            int c = integers.get(1);
            grid[r][c] = symbol;
        }
    }


    // If a seat is empty (L) and there are no occupied seats adjacent to it, the seat becomes occupied.
    private static void markRule1(List<List<Integer>> mark, String[][] grid, int r, int c) {
        int rows = grid.length;
        int cols = grid[0].length;
        int[] dr = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] dc = {-1, 0, 1, -1, 1, -1, 0, 1};

        if (Objects.equals(grid[r][c], "L")) {
            boolean allEmpty = true;
            for (int i = 0; i < 8; i++) {
                int nr = r + dr[i];
                int nc = c + dc[i];



                if ((nr < 0 || nr >= rows  || nc < 0 || nc >= cols )) continue;
                if (Objects.equals(grid[nr][nc], "#")) {
                    allEmpty = false;
                    break;
                }
            }
            if (allEmpty) {
                mark.add(List.of(r, c));
            }
        }
    }

    //    If a seat is occupied (#) and four or more seats adjacent to it are also occupied, the seat becomes empty.
    private static void markRule2(List<List<Integer>> mark,String[][] grid, int r, int c) {
        if (Objects.equals(grid[r][c], "#")) {
            if (countAdjacentOccupied(grid, r, c) >= 4) {
                mark.add(List.of(r, c));
            }
        }
    }

    private static int countAdjacentOccupied(String[][] grid, int r, int c) {
        int[] dr = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] dc = {-1, 0, 1, -1, 1, -1, 0, 1};

        int rows = grid.length;
        int cols = grid[0].length;
        int count = 0;

        for (int i = 0; i < 8; i++) {
            int nr = r + dr[i];
            int nc = c + dc[i];
            if ((nr < 0 || nr >= rows || nc < 0 || nc >= cols)) continue;
            if (Objects.equals(grid[nr][nc], "#")) count++;
        }

        return count;
    }

    private static void applyRule3(String[][] grid, int r, int c) {

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
