package com.hoangdieuctu.tools.kafkas.service.generator;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component
public class IntegerGeneratorService {

    private Random r = new Random();

    public int build(List<String> params) {
        int min = Integer.parseInt(params.get(0));
        int max = Integer.parseInt(params.get(1));
        return r.nextInt((max - min) + 1) + min;
    }

}
