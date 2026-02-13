package com.ben;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class Day4 {

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("day4.txt"));


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

        ///////////////
        List<Passport>totalPassport = new ArrayList<>();

        List<List<String>> allKeys = new ArrayList<>();
        for (String s : blocks) {
            List<String> keys = new ArrayList<>();
            String block = s;
            String[] split = block.split("\n");
            System.out.println(split);

            List<String> keyForPassword = new ArrayList<>();
            for (String ele : split) {
                String[] split1 = ele.split(" ");
                System.out.println(split1);
                for (String ele2 : split1) {
                    String[] split2 = ele2.split(":");
                    String key = split2[0];
                    String value = split2[1];
                    keys.add(key);
                    if (Objects.equals(key, "cid")) {
                    }
                    System.out.println(split2);
                }
                keyForPassword.addAll(Arrays.stream(split1).toList());
            }
            System.out.println(keyForPassword);
            assembleToPassword(keyForPassword, totalPassport);

//            System.out.println(split);
//            allKeys.add(keys);
        }

//        System.out.println(allKeys);


        int res = 0;
//        for (List<String> keys: allKeys){
//            if(checkIfValidForKey(keys)){
//                res++;
//            }
//        }

        System.out.println(totalPassport);

        for (Passport passport: totalPassport){
            if(
                    passport.isNeededPropertyExist() &&
            passport.validateHCL()&&
            passport.validateIYR()&&
            passport.validateEYR()&&
            passport.validateECL()&&
            passport.validatePID()&&
            passport.validateBYR()&&
            passport.validateHGT()
            ){
                res++;
            }
        }
        System.out.println(res);
    }

    private static void assembleToPassword(List<String> keyForPassword, List<Passport>totalPassword){
        Passport passport = new Passport();
        for (String ele: keyForPassword){
            String[] split = ele.split(":");
            String key = split[0];
            String value = split[1];
            switch (key) {
                case "hcl" -> passport.hcl(value);
                case "eyr" -> passport.eyr(value);
                case "pid" -> passport.pid(value);
                case "iyr" -> passport.iyr(value);
                case "ecl" -> passport.ecl(value);
                case "hgt" -> passport.hgt(value);
                case "byr" -> passport.byr(value);
                case "cid" -> passport.cid(value);
                case null, default -> System.out.println("invalid key " + key);
            }
        }
//        ecl:gry pid:860033327 eyr:2020 hcl:#fffffd
//        byr:1937 iyr:2017 cid:147 hgt:183cm
        totalPassword.add(passport);


    }

//    record Passport(String hcl, String iyr,String eyr,String ecl,String pid,String byr, String hgt, String cid){}

    private static boolean checkIfValidForKey(List<String> src){
        String[] a1 = {"hcl", "iyr", "eyr", "ecl", "pid", "byr", "hgt"};
        List<String> validKeys = Arrays.asList(a1);
        Set<String> srcSet = new HashSet<>(src);
        return srcSet.containsAll(validKeys);
    }
}

class Passport{
    String hcl;
    String iyr;
    String eyr;
    String ecl;
    String pid;
    String byr;
    String hgt;
    String cid;

    public Passport Passport() {
        return this;
    }

    public boolean isNeededPropertyExist(){
        return this.hcl != null && this.iyr != null && this.eyr != null && this.ecl != null && this.pid != null && this.byr != null && this.hgt != null;
    }


//    byr (Birth Year) - four digits; at least 1920 and at most 2002.
public boolean validateBYR(){
    return this.byr.length() ==4  &&
            Integer.parseInt(this.byr) >= 1920 &&
            Integer.parseInt(this.byr) <= 2002;
}


//    iyr (Issue Year) - four digits; at least 2010 and at most 2020.

    public boolean validateIYR(){
        return this.iyr.length() ==4  &&
                Integer.parseInt(this.iyr) >= 2010 &&
                Integer.parseInt(this.iyr) <= 2020;
    }

//    eyr (Expiration Year) - four digits; at least 2020 and at most 2030.
public boolean validateEYR(){
    return this.eyr.length() ==4  &&
            Integer.parseInt(this.eyr) >= 2020 &&
            Integer.parseInt(this.eyr) <= 2030;
}
// ex: hgt:162cm or 159in
//    hgt (Height) - a number followed by either cm or in:
//    If cm, the number must be at least 150 and at most 193.
//    If in, the number must be at least 59 and at most 76.
public boolean validateHGT() {

    if ((this.hgt.endsWith("cm") || this.hgt.endsWith("in"))) {
        if (this.hgt.endsWith("cm")) {
            String[] split = this.hgt.split("cm");
            int num = Integer.parseInt(split[0]);

            if (num >= 150 && num <= 193) return true;
            return false;
        }

        if (this.hgt.endsWith("in")) {
            String[] split = this.hgt.split("in");
            int num = Integer.parseInt(split[0]);
            if (num >= 59 && num <= 76) return true;
            return false;
        }

    }
    return false;
}





//    hcl (Hair Color) - a # followed by exactly six characters 0-9 or a-f.
    // hcl:#6c4ab1
    //hcl:#808e9e
    // hcl:#733820

    public boolean validateHCL() {
        HashSet<String> hexPossible = new HashSet<>(Arrays.asList("a", "b", "c", "d", "e", "f", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"));

        if(this.hcl.startsWith("#")){
            String[] split = this.hcl.split("#");
            String s = split[1];
            if(s.length() == 6){
                String[] split1 = s.split("");
                for (String singleS: split1){
                    if(!hexPossible.contains(singleS)){
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }



//            ecl (Eye Color) - exactly one of: amb blu brn gry grn hzl oth.
    public boolean validateECL() {
        HashSet<String> colorSet= new HashSet<>(Arrays.asList("amb", "blu", "brn", "gry", "grn", "hzl", "oth"));
        return colorSet.contains(this.ecl);
    }






//            pid (Passport ID) - a nine-digit number, including leading zeroes.
public boolean validatePID() {
    return this.pid.length() == 9;
}


//    cid (Country ID) - ignored, missing or not.



    //    Passport builder(){
//        return this;
//    }
    public Passport hcl(String hcl){
        this.hcl = hcl;
        return this;
    }

    public Passport iyr(String iyr){
        this.iyr = iyr;
        return this;
    }

    public Passport eyr(String eyr){
        this.eyr = eyr;
        return this;
    }

    public Passport ecl(String ecl){
        this.ecl = ecl;
        return this;
    }

    public Passport pid(String pid){
        this.pid = pid;
        return this;
    }

    public Passport byr(String byr){
        this.byr = byr;
        return this;
    }

    public Passport hgt(String hgt){
        this.hgt = hgt;
        return this;
    }

    public Passport cid(String cid){
        this.cid = cid;
        return this;
    }

//    cid
}