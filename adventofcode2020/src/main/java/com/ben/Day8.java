package com.ben;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


enum CODE_TYPE {
    NOP,
    ACC,
    JMP
}

record Opcode(CODE_TYPE code, int value){}

public class Day8 {
    public static void main(String[] args) throws IOException {
        List<String> lines = FileUtils.readLines(new File("day8_big.txt"), StandardCharsets.UTF_8);

        List<Opcode> opcodes = new ArrayList<>();
        for (String line: lines){
            String[] split = line.split(" ");
            if(split[0].equals("acc")){
                opcodes.add(new Opcode(CODE_TYPE.ACC, Integer.parseInt(split[1])));
            } else if(split[0].equals("jmp")){
                opcodes.add(new Opcode(CODE_TYPE.JMP, Integer.parseInt(split[1])));
            } else if(split[0].equals("nop")){
                opcodes.add(new Opcode(CODE_TYPE.NOP, Integer.parseInt(split[1])));
            } else {
                throw  new RuntimeException("not recognized opcode + " + split[0] + "");
            }
        }

//        System.out.println(opcodes);
        List<Integer> idxList = new ArrayList<>();
        for (Opcode opcode : opcodes) {
            if(opcode.code().equals(CODE_TYPE.NOP) || opcode.code().equals(CODE_TYPE.JMP)
            ){
                idxList.add(opcodes.indexOf(opcode));
            }
        }

//        System.out.println(idxList); [0, 2, 4, 7]

        for (Integer i : idxList) {
//            System.out.println(i);

            // update
            if(opcodes.get(i).code().equals(CODE_TYPE.NOP)){
                opcodes.set(i, new Opcode(CODE_TYPE.JMP, opcodes.get(i).value()));
            } else if(opcodes.get(i).code().equals(CODE_TYPE.JMP)){
                opcodes.set(i, new Opcode(CODE_TYPE.NOP, opcodes.get(i).value()));
            }
//            System.out.println(opcodes);
            AccumulatorRes accumulator = getAccumulator(opcodes);
            if(!accumulator.isCycle()){
                System.out.println(accumulator.accumulator);
                break;
            }

            // update back
            if(opcodes.get(i).code().equals(CODE_TYPE.NOP)){
                opcodes.set(i, new Opcode(CODE_TYPE.JMP, opcodes.get(i).value()));
            } else if(opcodes.get(i).code().equals(CODE_TYPE.JMP)){
                opcodes.set(i, new Opcode(CODE_TYPE.NOP, opcodes.get(i).value()));
            }
        }
    }

    record AccumulatorRes(boolean isCycle, int accumulator){}

    private static AccumulatorRes getAccumulator(List<Opcode> opcodes) {
        Set<Integer> set0 = new HashSet<>();
        boolean isCycle = false;

        int accumulator = 0;
        for (int i = 0; i < opcodes.size(); i++) {
            if(set0.contains(i)){
                System.out.println("CYCLE!");
                isCycle = true;
                break;
            }

            Opcode opcode = opcodes.get(i);
            if(opcode.code().equals(CODE_TYPE.NOP)){
                set0.add(i);
                continue;
            } else if(opcode.code().equals(CODE_TYPE.ACC)){
                accumulator = accumulator + opcode.value();
                set0.add(i);
            } else if(opcode.code().equals(CODE_TYPE.JMP)){
                set0.add(i);
                i = i + opcode.value() - 1;

            } else {
                throw  new RuntimeException("SHALL NOT HAPPEN");
            }
        }
        return new AccumulatorRes(isCycle, accumulator);
    }
}
