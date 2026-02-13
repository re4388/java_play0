package com.ben;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Day18_part2 {

    public static void main(String[] args) throws IOException {
        String path = "day18_big.txt";
        List<String> lines = FileUtils.readLines(new File(path), StandardCharsets.UTF_8);


        ArrayList<Long> res = new ArrayList<>();

        for (String line : lines) {
            List<String> tokens = Arrays.stream(line.split("")).toList()
                    .stream()
                    .filter(v -> !v.isBlank())
                    .toList();
            System.out.println("input: " + tokens);

            Deque<String> stack = new ArrayDeque<>();
            List<String> tmp = new ArrayList<>();

            for (String token : tokens) {
                if (Objects.equals(token, ")")) {
                    while (!Objects.equals(stack.peek(), "(")) {
                        tmp.add(stack.pop());
                    }
                    stack.pop(); // pop the "("
                    System.out.println("tmp.reversed(): " + tmp.reversed());
                    long v = handleMultiPrecedence(tmp.reversed());
                    tmp.clear();
                    System.out.println("tmp result: " + v);
                    stack.push(String.valueOf(v));
                } else {
                    stack.push(token);
                }

            }

            System.out.println("after handle curly brace: " + stack);
//            long v = handleMultiPrecedence(stack.stream().toList().reversed());
            long v = handleMultiPrecedence(stack.stream().toList().reversed());
            System.out.println("FINAL ANS: " + v);
            res.add(v);
        }

        long sum = 0l;
        for (long re : res) {
            sum += re;
        }
        System.out.println(sum);
    }

    private static Long handleMultiPrecedence(List<String> tmp) {
//        List<String> tmp = tmp2.reversed();
        System.out.println("inside handleMultiPrecedence: " + tmp);
        Deque<String> s0 = new ArrayDeque<>();
        for (int i = 0; i < tmp.size(); i++) {
            String s = tmp.get(i);
            if (s.equals("+")) {
                String pop = s0.pop();
                Long l1 = Long.parseLong(pop) + Long.parseLong(tmp.get(i + 1));
                s0.push(l1.toString());
                i++;
            } else {
                s0.push(s);
            }
        }

        if(s0.size() == 1) {
            return Long.parseLong(s0.pop());
        }

        return runMutiOnly(s0.stream().toList());
    }

    private static Long runMutiOnly(List<String> tmp) {
        Long res = 0l;
        for (int i = 1; i < tmp.size(); i = i + 2) {
            if (i == 1) {
                res = (Long.parseLong(tmp.getFirst()) * Long.parseLong(tmp.get(i + 1)));
            } else {
                res = res * Long.parseLong(tmp.get(i + 1));
            }
        }

        return res;
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





