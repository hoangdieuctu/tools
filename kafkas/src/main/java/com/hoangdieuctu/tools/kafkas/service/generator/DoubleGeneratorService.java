package com.hoangdieuctu.tools.kafkas.service.generator;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component
public class DoubleGeneratorService {

    private Random r = new Random();

    public double build(List<String> params) {
        double min = Double.parseDouble(params.get(0));
        double max = Double.parseDouble(params.get(1));

        return min + (max - min) * r.nextDouble();
    }

}
