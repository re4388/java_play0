package com.ben;

import org.jspecify.annotations.NonNull;




import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Day6 {
    public static void main(String[] args) throws IOException {
//        System.out.println();
        List<String> blocks = readFileAndSeparateByEmptyLine("day6.txt");

        List<List<String>> res0 = new ArrayList<>();
        for (String block: blocks){

            // b
            if(!hasMuti_YES(block) && !hasMulti_Ppl(block)){
                res0.add(List.of(block));
            }

            // abc
            if(hasMuti_YES(block) && !hasMulti_Ppl(block)){
                String[] split = block.split("");
                res0.add(Arrays.stream(split).toList());
            }


            if(hasMulti_Ppl(block)){


                ////////////////////  for the second part ////////////////////////
                List<List<String>> tmp = new ArrayList<>();
                String[] splitByNewLine = block.split("\n");
                // ["ab", "ac"]

//                [["a", "b"], ["a", "c"]]
                for (String s: splitByNewLine){
                    String[] split = s.split("");
                    tmp.add(Arrays.stream(split).toList());
                }

                Set<String> common = findCommon(tmp);
                List<String> list = common.stream().toList();
                res0.add(list);
                ////////////////////  for the second part ////////////////////////


////////////////////  for the first part ////////////////////////
//                ArrayList<String> tmp = new ArrayList<>();
//                for (String s: splitByNewLine){
//                    // ac
//                    // b
//                    if(hasMuti_YES(block)){
//                        String[] split = s.split("");
//                        tmp.addAll(Arrays.stream(split).toList());
//                    // a
//                    // b
//                    // c
//                    } else {
//                        tmp.add(block);
//                    }
//
//                }
//                res0.add(tmp);
////////////////////  for the first part ////////////////////////////////////
            }

        }

        System.out.println(res0);

        List<HashSet<String>> hashSetList = res0.stream().map(HashSet::new).toList();
        int total =0;
        for (HashSet<String> v: hashSetList){
            total += v.size();
        }
        System.out.println(total);

    }

    private static Set<String> findCommon(List<List<String>> data){
        // 先把第一個 list 轉成 set
        Set<String> common = new HashSet<>(data.getFirst());

        // 對後面的 list 做交集
        for (int i = 1; i < data.size(); i++) {
            common.retainAll(data.get(i));
        }

        return common;
    }

    private static boolean hasMulti_Ppl(String input){
        for (char c: input.toCharArray()){
            if(c == '\n'){
                return true;
            }
        }
        return false;
    }

    private static boolean hasMuti_YES(String input){
        char[] charArray = input.toCharArray();
        return charArray.length != 1;
    }

    private static @NonNull List<String> readFileAndSeparateByEmptyLine(String fileName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));


        List<String> blocks = new ArrayList<>();
        StringBuilder currentBlock = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            if (line.trim().isEmpty()) {
                // 遇到空行且目前段落有東西，就把目前段落存下
                if (!currentBlock.isEmpty()) {
                    blocks.add(currentBlock.toString().trim());
                    currentBlock.setLength(0); // 清空 StringBuilder
                }
            } else {
                // 加入段落內容
                currentBlock.append(line).append("\n");
            }
        }

        // 如果檔案沒以空行結尾, 且段落有東西，加入 blocks
        if (!currentBlock.isEmpty()) {
            blocks.add(currentBlock.toString().trim());
        }

        reader.close();
        return blocks;
    }


}
