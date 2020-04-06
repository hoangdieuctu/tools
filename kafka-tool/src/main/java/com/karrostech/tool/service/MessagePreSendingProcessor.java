package com.karrostech.tool.service;

import com.karrostech.tool.util.GeneratorBuilder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class MessagePreSendingProcessor {

    private static Logger logger = LoggerFactory.getLogger(MessagePreSendingProcessor.class);

    private static final String GENERATOR_PATTERN = "\\{\\{[\\w,\\d,(,),:,.,-]+}}";
    private static final String PATTERN_CHARACTER_START = "{{";
    private static final String PATTERN_CHARACTER_END = "}}";
    private static final String EMPTY_STRING = "";

    private Pattern pattern = Pattern.compile(GENERATOR_PATTERN);

    @Autowired
    private GeneratorBuilder generatorBuilder;

    public String process(String message) {
        Matcher matcher = pattern.matcher(message);
        List<String> matches = new ArrayList<>();
        while (matcher.find()) {
            matches.add(matcher.group(0));
        }

        if (!matches.isEmpty()) {
            logger.info("Found {} patterns", matches.size());
        }

        for (String m : matches) {
            String key = m.replace(PATTERN_CHARACTER_START, EMPTY_STRING).replace(PATTERN_CHARACTER_END, EMPTY_STRING);
            String value = String.valueOf(generatorBuilder.generator(key));
            message = StringUtils.replaceOnce(message, m, value);
        }

        return message;
    }

}
