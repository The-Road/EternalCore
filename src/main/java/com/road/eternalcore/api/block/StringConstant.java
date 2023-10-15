package com.road.eternalcore.api.block;

import java.util.HashMap;
import java.util.Map;

public class StringConstant implements Comparable<StringConstant>{
    // 因为Property在比较的时候用==，所以需要把字符串转化为常量进行存储
    private static final Map<String, StringConstant> strings = new HashMap<>();
    private final String value;
    private StringConstant(String value){
        this.value = value;
    }

    public static StringConstant of(String value){
        if (strings.containsKey(value)){
            return strings.get(value);
        } else {
            StringConstant newConstant = new StringConstant(value);
            strings.put(value, newConstant);
            return newConstant;
        }
    }
    public String value(){
        return value;
    }

    public int compareTo(StringConstant another) {
        return value.compareTo(another.value);
    }
}