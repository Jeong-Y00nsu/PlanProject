package com.jRyun.demo.planProject.util;

public class Validation {

    public static boolean isContainChars(String str, String[] chars){
        for(String c : chars){
            if(str.contains(c)) return true;
        }
        return false;
    }

}
