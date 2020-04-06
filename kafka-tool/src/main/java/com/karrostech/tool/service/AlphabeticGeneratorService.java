package com.karrostech.tool.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AlphabeticGeneratorService {

    public String build(List<String> lengths) {
        int length = Integer.parseInt(lengths.get(0));
        return RandomStringUtils.randomAlphabetic(length).toLowerCase();
    }

}
