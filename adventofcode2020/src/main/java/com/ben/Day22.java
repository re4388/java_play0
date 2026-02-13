package com.ben;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Day22 {

    public static void main(String[] args) throws IOException {
        String path = "day22_big.txt";
        List<String> lines = FileUtils.readLines(new File(path), StandardCharsets.UTF_8);
        System.out.println(lines);


        /**
         * Player 1:
         * 9
         * 2
         * 6
         * 3
         * 1
         *
         * Player 2:
         * 5
         * 8
         * 4
         * 7
         * 10
         *
         *
         */

        int spaceIdx = lines.indexOf("");
        Deque<String> player1 = new ArrayDeque<>(lines.subList(1, spaceIdx));
        Deque<String> player2 = new ArrayDeque<>(lines.subList(spaceIdx+2, lines.size()));
        System.out.println(player1);
        System.out.println(player2);


        long round = 1;
        /**
         * -- Round 1 --
         * Player 1's deck: 9, 2, 6, 3, 1
         * Player 2's deck: 5, 8, 4, 7, 10
         * Player 1 plays: 9
         * Player 2 plays: 5
         * Player 1 wins the round!
         */
        while(!player1.isEmpty() && !player2.isEmpty()){
            System.out.println("Round: " + round);
            System.out.println("Player 1's deck: " + player1);
            System.out.println("Player 2's deck: " + player2);
            String play1Play = player1.pop();
            String play2Play = player2.pop();
            System.out.println("Player 1 plays: " + play1Play);
            System.out.println("Player 2 plays: " + play1Play);

            if(Integer.parseInt(play1Play) > Integer.parseInt(play2Play)){
                System.out.println("Player 1 wins the round!");
                player1.addLast(play1Play);
                player1.addLast(play2Play);
            } else if(Integer.parseInt(play1Play) < Integer.parseInt(play2Play)){
                System.out.println("Player 2 wins the round!");
                player2.addLast(play2Play);
                player2.addLast(play1Play);
            } else {
                System.out.println("Draw!");
                System.exit(1); // todo: how to handle?
            }
            round++;
        }

        System.out.println("== Post-game results ==");
        System.out.println("Player 1's deck: " + player1);
        System.out.println("Player 2's deck: " + player2);

        if(player1.size() == 0){
            getScore(player2);
        } else {
            getScore(player1);
        }
    }

    private static void getScore(Deque<String> input){
        long score = 0;
        int size = input.size();
        for (String s : input) {
            int num = Integer.parseInt(s);
            score += ((long) num * size);
            size--;
        }

        System.out.println(score);
    }
}







