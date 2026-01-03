package com.ben;

import com.google.common.collect.Sets;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toSet;


//     1-3 a: abcde
//
//    {
//        minToMax:[1, 3]
//        charToCheck:"a"
//        word: "abcde"
//    }

record input(List<Long> minToMax, String charToCheck, String word) {
}

public class Day2 {

    public static void main(String[] args) throws IOException {
        long endResult = 0;


        List<String> strings = FileUtils.readLines(new File("day2.txt"), StandardCharsets.UTF_8);

        for (String s : strings) {
            String[] split = s.split(" ");
            input input = new input(Arrays.stream(split[0].split("-")).map(Long::parseLong).toList(),
                    split[1].substring(0, 1),
                    split[2]
            );


//            day1
//            long countTimeResult = countTime(input.word(), input.charToCheck());
//            if(withInRange(countTimeResult, input.minToMax())){
//                endResult++;
//            }

            // day2
            List<String> getCheckedCharResult = getCheckedChar(input.word(), input.minToMax());
            if (checkTimes(input.charToCheck(), getCheckedCharResult) == 1) {
                endResult++;
            }


        }

        System.out.println(endResult);
    }

    //    record input(List<Long>minToMax, String charToCheck, String word){}
    public static List<String> getCheckedChar(String word, List<Long> minToMax) {
        List<String> worldlist = Arrays.stream(word.split("")).toList();

        // -1 to covert to 0 based idx
        Long idx1 = minToMax.get(0) - 1;
        Long idx2 = minToMax.get(1) - 1;

        return List.of(worldlist.get(idx1.intValue()), worldlist.get(idx2.intValue()));
    }

    public static Long checkTimes(String charToCheck, List<String> getCheckedCharResult) {
        Long times = 0L;
        for (String s : getCheckedCharResult) {
            if (s.equals(charToCheck)) {
                times++;
            }
        }

        return times;
    }


//    abcde
//    idx 1, 3 -> a, c
//
//    getCheckedChar -> List<String>
//
//    看a在List<String>裡面出現過幾次 -> Long
//
//    只有 剛好一次才是 valid


//     1-3 a: abcde
//
//    {
//        minToMax:[1, 3]
//        charToCheck:"a"
//        word: "abcde"
//    }


    public static long countTime(String word, String charToCheck) {
        long countResult = 0L;
        String[] wordSplit = word.split("");
        for (String s : wordSplit) {
            if (s.equals(charToCheck)) {
                countResult++;
            }
        }

        return countResult;
    }

    public static Boolean withInRange(Long countResult, List<Long> minToMax) {
        return countResult >= minToMax.get(0) && countResult <= minToMax.get(1);
    }
}