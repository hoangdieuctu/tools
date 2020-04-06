package com.hoangdieuctu.tools.kafkas.service.generator;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UuidGeneratorService {

    public String build() {
        return UUID.randomUUID().toString();
    }

}
