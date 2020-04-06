package com.hoangdieuctu.tools.kafkas.service.generator;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AlphanumericGeneratorService {

    public String build(List<String> lengths) {
        int length = Integer.parseInt(lengths.get(0));
        return RandomStringUtils.randomAlphanumeric(length).toLowerCase();
    }

}
