package com.hoangdieuctu.tools.kafkas.util;

public class PatternMatcher {

    public static boolean match(String pattern, String data) {
        return data.matches(pattern.replace("?", ".?").replace("*", ".*?"));
    }

}
