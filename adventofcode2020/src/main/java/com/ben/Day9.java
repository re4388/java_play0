package com.ben;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;


public class Day9 {
    public static void main(String[] args) throws IOException {
        List<String> lines = FileUtils.readLines(new File("day9_big.txt"), StandardCharsets.UTF_8);

        Long goal = 144381670L;
        List<Long> tmp = new ArrayList<>();

        out:
        for (int i = 0; i < lines.size(); i++) {
            Long sum = 0L;
            for (int j = i; j < lines.size(); j++) {
                Long curNum = Long.parseLong(lines.get(j));
                tmp.add(curNum);
                sum = sum + curNum;
                if(sum.equals(goal)){
                    break out;
                }

                if (sum > goal) {
                    tmp.clear();
                    break;
                }
            }
        }
        System.out.println(tmp);

        Long min = Collections.min(tmp);
        Long max = Collections.max(tmp);
        System.out.println(min+max);

//        System.out.println("min: " + Collections.min(tmp));
//        System.out.println("max: " + Collections.max(tmp));
//        System.out.println("res: " + Long.parseLong(Collections.min(tmp)) +
//                Long.parseLong((Collections.max(tmp)));



//        int preamble = 5;
//
//        for (int i = preamble; i < lines.size(); i++) {
//            String line = lines.get(i);
//            long num = Long.parseLong(line);
//
//            ArrayList<Long> subList = new ArrayList<>();
//            for (int j = i - preamble; j < i; j++) {
//                subList.add(Long.parseLong(lines.get(j)));
//            }
//
//            boolean found = false;
//            outer:
//            for (int j = 0; j < subList.size(); j++) {
//                for (int k = j+1; k < subList.size(); k++) {
//                    if(subList.get(j) + subList.get(k) == num){
//                        found = true;
//                        break outer;
//                    }
//                }
//            }
//
//            if(!found){
//                System.out.println(num); // step1 ans: 127 and 144381670
//                break;
//            }
//        }


    }
}


