package com.karrostech.tool.util;

import com.karrostech.tool.constant.GeneratorType;
import com.karrostech.tool.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class GeneratorBuilder {

    private static final String START_PARAMS = "(";
    private static final String END_PARAMS = ")";
    private static final String SEPARATE_PARAMS = ",";

    @Autowired
    private IntegerGeneratorService integerGeneratorService;

    @Autowired
    private DoubleGeneratorService doubleGeneratorService;

    @Autowired
    private UuidGeneratorService uuidGeneratorService;

    @Autowired
    private CurrentMillisGeneratorService currentMillisGeneratorService;

    @Autowired
    private AlphabeticGeneratorService alphabeticGeneratorService;

    @Autowired
    private AlphanumericGeneratorService alphanumericGeneratorService;

    public Object generator(String pattern) {
        GeneratorType type = extractKey(pattern);

        switch (type) {
            case random_int:
                return integerGeneratorService.build(extractParams(pattern));
            case random_double:
                return doubleGeneratorService.build(extractParams(pattern));
            case random_uuid:
                return uuidGeneratorService.build();
            case current_millis:
                return currentMillisGeneratorService.build();
            case random_alphabetic:
                return alphabeticGeneratorService.build(extractParams(pattern));
            case random_alphanumeric:
                return alphanumericGeneratorService.build(extractParams(pattern));
            default:
                return null;
        }
    }

    public List<String> extractParams(String pattern) {
        int start = pattern.indexOf(START_PARAMS);
        int end = pattern.indexOf(END_PARAMS);

        String params = pattern.substring(start + 1, end);
        return Arrays.asList(params.split(SEPARATE_PARAMS));
    }

    public GeneratorType extractKey(String pattern) {
        if (!pattern.contains(START_PARAMS) && !pattern.contains(END_PARAMS)) {
            return GeneratorType.from(pattern);
        }

        int start = pattern.indexOf(START_PARAMS);
        String sub = pattern.substring(0, start);
        return GeneratorType.from(sub);
    }
}
