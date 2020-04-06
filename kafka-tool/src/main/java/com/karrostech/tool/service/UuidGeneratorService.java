package com.karrostech.tool.service;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UuidGeneratorService {

    public String build() {
        return UUID.randomUUID().toString();
    }

}
