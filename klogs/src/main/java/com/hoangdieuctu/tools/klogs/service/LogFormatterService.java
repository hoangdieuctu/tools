package com.hoangdieuctu.tools.klogs.service;

import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class LogFormatterService {

    private static final String SPACE = " ";
    private static final String LOG_PATTERN = "(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}.\\d{3})  (\\w+) (\\w+) (---) \\[(.+)\\](.+)::(.+)";

    private Pattern pattern = Pattern.compile(LOG_PATTERN);

    public String format(String log) {
        Matcher matcher = pattern.matcher(log);
        if (!matcher.find()) {
            return log;
        }

        String time = matcher.group(1);
        String level = matcher.group(2);
        String pid = matcher.group(3);
        String symbol = matcher.group(4);
        String thread = matcher.group(5);
        String _class = matcher.group(6);
        String rest = matcher.group(7);

        StringBuilder builder = new StringBuilder();
        builder.append(buildSpan(time, "time")).append(SPACE);
        builder.append(buildSpan(level, "level level_" + level)).append(SPACE);
        builder.append(buildSpan(pid, "pid")).append(SPACE);
        builder.append(buildSpan(symbol, "symbol")).append(SPACE);
        builder.append(buildSpan("[" + thread + "]", "thread")).append(SPACE);
        builder.append(buildSpan(_class, "_class"));
        builder.append(buildSpan("::", "determine"));
        builder.append(buildSpan(rest, "rest"));

        return builder.toString();
    }

    private String buildSpan(String text, String _class) {
        return "<span class='" + _class + "'>" + text + "</span>";
    }
}
