package com.ben;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class Day18 {
    public static void main(String[] args) throws IOException {
        String path = "day18_big.txt";
        List<String> lines = FileUtils.readLines(new File(path), StandardCharsets.UTF_8);


        ArrayList<Long> res = new ArrayList<>();

        for (String line : lines) {
            List<String> tokens = Arrays.stream(line.split("")).toList()
                    .stream()
                    .filter(v -> !v.isBlank())
                    .toList();
            System.out.println(tokens);

            Deque<String> stack = new ArrayDeque<>();
            List<String> tmp = new ArrayList<>();

            for (String token : tokens) {
                if (Objects.equals(token, ")")) {
                    while (!Objects.equals(stack.peek(), "(")) {
                        tmp.add(stack.pop());
                    }
                    stack.pop(); // pop the "("
                    System.out.println("tmp.reversed(): " + tmp.reversed());
                    long v = runTmpv2(tmp.reversed());
                    tmp.clear();
                    System.out.println("tmp result: " + v);
                    stack.push(String.valueOf(v));
                } else {
                    stack.push(token);
                }

            }

            System.out.println(stack);
            long v = runTmpv2(stack.stream().toList().reversed());
            System.out.println("FINAL ANS: " + v);
            res.add(v);
        }

        long sum = 0l;
        for (long re : res) {
            sum += re;
        }
        System.out.println(sum);
    }


    private static Long runTmpv2(List<String> tmp) {
        Long res = 0l;
        for (int i = 1; i < tmp.size(); i = i + 2) {
            String token = tmp.get(i);

            if (token.equals("+")) {
                if (i == 1) {
                    res = (Long.parseLong(tmp.getFirst()) + Long.parseLong(tmp.get(i + 1)));
                } else {
                    res = res + Long.parseLong(tmp.get(i + 1));
                }
            } else {
                if (i == 1) {
                    res = (Long.parseLong(tmp.getFirst()) * Long.parseLong(tmp.get(i + 1)));
                } else {
                    res = res * Long.parseLong(tmp.get(i + 1));
                }
            }
        }

        return res;
    }


    private static double runTmp(List<String> tmp) {
        Set<String> operators = new HashSet<>(Set.of("+", "*"));

        double res = 0;
        for (int i = 0; i < tmp.size(); i++) {
            String token = tmp.get(i);

            if (operators.contains(token)) {
                double s1;
                if (i == 1) {
                    s1 = Double.parseDouble(tmp.get(i - 1));
                } else {
                    s1 = res;
                }

                String s2 = tmp.get(i + 1);
                if (token.equals("+")) {
                    res = s1 + Double.parseDouble(s2);
                } else {
                    res = s1 * Double.parseDouble(s2);
                }
            }
        }

        return res;
    }


}





