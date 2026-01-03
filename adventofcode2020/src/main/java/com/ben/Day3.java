package com.ben;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Day3 {

    public static List<String> myAppend(int count, String[] s, List<String> orgList){
//        List<String> list = new ArrayList<>(Arrays.asList());
        for (int i = 0; i < count; i++) {
            orgList.addAll(Arrays.stream(s).toList());
        }
        return orgList;
    }

    public static void main(String[] args) throws IOException {
//        String strings = FileUtils.readFileToString(new File("day3.txt"), StandardCharsets.UTF_8);
//        System.out.println(strings);
//        List<String> list = Arrays.stream(strings.split("\n")).toList();
//        System.out.println(list.toString());


        List<String> strings = FileUtils.readLines(new File("day3.txt"), StandardCharsets.UTF_8);
        ArrayList<String[]> outerArr = new ArrayList<>();
        for (String s : strings) {
            String[] split = s.split("");
            outerArr.add(split);
        }
        System.out.println(outerArr);

        ArrayList<List<String>> outerArr2 = new ArrayList<>();
        for (String[] s : outerArr) {
            List<String> orglist = new ArrayList<>(Arrays.asList(s));
            List<String> newList = myAppend(100, s, orglist);
            outerArr2.add(newList);
        }



        System.out.println(outerArr2);

        int last_row = outerArr2.size();
        int last_col = outerArr2.get(0).size();
        int curR = 0;
        int curC = 0;

        ArrayList<Integer> endResultList = new ArrayList<>();

        int endResult = getEndResult(curR, last_row, curC, last_col, outerArr2, 1, 1);
        endResultList.add(endResult);
        int endResult1 = getEndResult(curR, last_row, curC, last_col, outerArr2, 3, 1);
        endResultList.add(endResult1);
        int endResult2 = getEndResult(curR, last_row, curC, last_col, outerArr2, 5, 1);
        endResultList.add(endResult2);
        int endResult3 = getEndResult(curR, last_row, curC, last_col, outerArr2, 7, 1);
        endResultList.add(endResult3);
        int endResult4 = getEndResult(curR, last_row, curC, last_col, outerArr2, 1, 2);
        endResultList.add(endResult4);

        long sumUp = 1L;
        for (int v: endResultList){
            sumUp = sumUp * v;
        }

        System.out.println(sumUp);



    }

    private static int getEndResult(int curR, int last_row, int curC, int last_col, ArrayList<List<String>> outerArr2
    ,int moveRight, int moveDown) {
        int endReuslt = 0;
        while (curR < last_row) {
            curC = curC + moveRight; // right 3
            curR = curR + moveDown; // down 1
            if (curR > last_row -1 || curC > last_col -1)  {
                break;
            }
            if (outerArr2.get(curR).get(curC).equals("#")) {
                endReuslt++;
            }

        }
        return endReuslt;
    }

}