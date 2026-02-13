package com.ben;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Day5 {



//    FBFBBFFRLR
//    row 44, column 5.
//    Every seat also has a unique seat ID: multiply the row by 8, then add the column. In this example, the seat has ID 44 * 8 + 5 = 357.

//    BFFFBBFRRR: row 70, column 7, seat ID 567.
//    FFFBBBFRRR: row 14, column 7, seat ID 119.
//    BBFFBBFRLL: row 102, column 4, seat ID 820.


    public static void main(String[] args) throws IOException {
        int maxInt = 0;

        List<String> commandsList = FileUtils.readLines(new File("day5.txt"), StandardCharsets.UTF_8);
        for (String commands: commandsList){
            int res = binarySearch(commands);
            if(res > maxInt){
                maxInt = res;
            }
        }
//        System.out.println(maxInt);
    }


    public static int binarySearch(String commands){
        int mid;
        int left = 0;
        int right = 127;

        int left2 = 0;
        int right2 = 7;
        int mid2;

        int row = 0;
        int col = 0;

        String[] split = commands.split("");
        for (String command: split){

            if(command.equals("L") || command.equals("R")){
//                System.out.println("left: " + left + " right: " + right);
                assert (left == right);
                row = left;

                // begin handle col
                mid2 = (left2 + right2) / 2;
                if(command.equals("L")){
                    right2 = mid2;
                } else if(command.equals("R")){
                    left2 = mid2+1;
                }

            } else {

                mid = (left + right) / 2;
                if(command.equals("F")){
                    right = mid;
                } else if(command.equals("B")){
                    left = mid+1;
                }

            }

        }

        col = left2;


//    Start by considering the whole range, rows 0 through 127.
//    F means to take the lower half, keeping rows 0 through 63.
//    B means to take the upper half, keeping rows 32 through 63.
//    F means to take the lower half, keeping rows 32 through 47.
//    B means to take the upper half, keeping rows 40 through 47.
//    B keeps rows 44 through 47.
//    F keeps rows 44 through 45.
//    The final F keeps the lower of the two, row 44.
//        System.out.println("can't find it");
        System.out.println("row " + row + ", column " + col + ", seat ID " + (row * 8 + col));
        return row * 8 + col;
    }
}
