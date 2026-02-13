package com.ben.Day7;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Day7 {
    static int totalBag = 0;

    private record BagNode(String parent, Map<String, Integer> children) {
    }

    public static void main(String[] args) throws IOException {
        int V = 0;
        int E = 0;



        //        map.put("apple", 1);
//        map.put("banana", 2);

        List<String> lines = FileUtils.readLines(new File("day7_big.txt"), StandardCharsets.UTF_8);
        Set<String> bagSet = new LinkedHashSet<>();
        List<BagNode> allNode = new ArrayList<>();

        for (String line : lines) {
            Pattern p = Pattern.compile("(\\d\\s)?(\\w+\\s\\w+)\\sbags?");
            Matcher m = p.matcher(line);

            // light red bags contain 1 bright white bag, 2 muted yellow bags
            String parent = null;
            Map<String, Integer> m1 = new HashMap<>();
            int iter = 0;
            while (m.find()) {
                iter++;

                if (m.group(2).equals("no other")) {
                    continue;
                }

                String count = Optional.ofNullable(m.group(1)).orElse("0").trim();
                String bagKind = m.group(2).replace(" ", "_");
//                System.out.println(count);
//                System.out.println(bagKind);
                if (iter == 1) {
                    parent = bagKind;
                    bagSet.add(bagKind);
                } else {
                    m1.put(bagKind, Integer.parseInt(count));
                    bagSet.add(bagKind);
                }
                E++;
            }
            E--; // for each loop, 減去一個 parent那一個

            assert parent != null;
            allNode.add(new BagNode(parent, m1));
        }

//        for (String bag : bagSet) {
//            System.out.println(bag);
//        }


        Map<String, BagNode> map0 = new LinkedHashMap<>();
        for (BagNode bagNode : allNode) {
            String parent = bagNode.parent();
            Map<String, Integer> children = bagNode.children();
            map0.put(parent, new BagNode(parent, children));
        }

        String source = "shiny_gold";
        getCountRecur(map0, source);
        System.out.println(totalBag);





        /// /////////// below is part1 code to gen `output0.txt`
        // and with this output, we run DepthFirstDirectedPaths and get the result
//        V = bagSet.size();
//        System.out.println(V);
//        System.out.println(E);
//        System.out.println(allNode);
//        List<String> outputLines = new ArrayList<>();
//        outputLines.add(String.valueOf(V));
//        outputLines.add(String.valueOf(E));
//
//        for (String s : bagSet) {
//            outputLines.add(s);
//        }
//
//
//        for (BagNode bagNode : allNode) {
//            String parent = bagNode.parent();
//            Map<String, Integer> children = bagNode.children();
//
//            for (Map.Entry<String, Integer> entry : children.entrySet()) {
//                String child = entry.getKey();
//                Integer count = entry.getValue();
//                System.out.println(parent + " " + child + " " + count);
//                outputLines.add(parent + " " + child);
//            }
//        }

//        writeLines(new File("output0.txt"), outputLines);


    }

    private static void getCountRecur(Map<String, BagNode> map0, String source){
        BagNode bagNode = map0.get(source);
        Map<String, Integer> children = bagNode.children();
        if(children == null || children.isEmpty()){
            return;
        }
        for (Map.Entry<String, Integer> stringIntegerEntry : children.entrySet()) {
            String key = stringIntegerEntry.getKey();
            Integer value = stringIntegerEntry.getValue();
            totalBag = totalBag + value;
            for (int i = 0; i < value; i++) {
                getCountRecur(map0, key);
            }
        }

    }
}
