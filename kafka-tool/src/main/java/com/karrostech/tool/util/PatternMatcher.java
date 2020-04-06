package com.karrostech.tool.util;

public class PatternMatcher {

    public static final boolean match(String pattern, String data) {
        return data.matches(pattern.replace("?", ".?").replace("*", ".*?"));
    }

}
