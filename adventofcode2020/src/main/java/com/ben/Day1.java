package com.ben;
import com.google.common.collect.Sets;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;

public class Day1 {

    public static void main(String[] args) throws IOException {
        List<String> strings = FileUtils.readLines(new File("day1.txt"), StandardCharsets.UTF_8);
        Set<Long> lines = strings.stream().map(Long::parseLong).collect(toSet());

        int k = 3;
        long res = 0;

        Set<Set<Long>> combinations = Sets.combinations(lines, k);
        for (Set<Long> combination : combinations){
            System.out.println(combination);
            List<Long> list = combination.stream().toList();
            if(list.get(0) + list.get(1)  + list.get(2)== 2020){
                res = list.get(0) * list.get(1) * list.get(2);
            }
        }

        System.out.println(res);



    }
}