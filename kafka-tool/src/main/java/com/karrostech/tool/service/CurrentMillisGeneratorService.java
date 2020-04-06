package com.karrostech.tool.service;

import org.springframework.stereotype.Component;

@Component
public class CurrentMillisGeneratorService {

    public long build() {
        return System.currentTimeMillis();
    }

}
