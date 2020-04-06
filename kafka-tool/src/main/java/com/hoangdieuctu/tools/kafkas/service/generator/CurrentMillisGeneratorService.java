package com.hoangdieuctu.tools.kafkas.service.generator;

import org.springframework.stereotype.Component;

@Component
public class CurrentMillisGeneratorService {

    public long build() {
        return System.currentTimeMillis();
    }

}
