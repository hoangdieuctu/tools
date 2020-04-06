package com.karrostech.tool.service;

import com.karrostech.tool.repository.FileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LogService {

    private static final String LOG_FILE_NAME = "kafka-tool.log";

    @Autowired
    private FileRepository fileRepository;

    public String getLogs() {
        String content = fileRepository.read(LOG_FILE_NAME);
        return content;
    }
}
