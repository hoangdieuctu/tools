package com.hoangdieuctu.tools.klogs.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class LogFormatterService {

    private static final String SPACE = " ";
    private static final String NEW_LINE = "\n";

    // time, space, level, pid, symbol, thread, space, class, label, method, space, label, content
    private static final String LOG_PATTERN = "(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}.\\d{3})( )+([A-Z]+) (\\d+) (---) (\\[.+\\])( )+(.+)(::)([a-zA-Z0-9_$]+)( )+(:) (.+)";

    private Pattern pattern = Pattern.compile(LOG_PATTERN);

    public List<String> format(String log) {
        String[] lines = log.split(NEW_LINE);

        List<String> results = new ArrayList<>();

        StringBuilder builder = new StringBuilder();
        for (String line : lines) {
            if (line.isEmpty()) {
                continue;
            }

            Matcher matcher = pattern.matcher(line);
            if (!matcher.find()) {
                builder.append(buildSpan(line, "log_line"));
            } else {
                if (builder.length() != 0) {
                    results.add(builder.toString());
                    builder = new StringBuilder();
                }
                builder.append(format(matcher));
            }
        }

        if (builder.length() != 0) {
            results.add(builder.toString());
        }

        return results;
    }

    private String format(Matcher matcher) {
        String time = matcher.group(1);
        String level = matcher.group(3);
        String pid = matcher.group(4);
        String symbol = matcher.group(5);
        String thread = matcher.group(6);
        String _class = matcher.group(8);
        String label = matcher.group(9);
        String method = matcher.group(10);
        String secondLabel = matcher.group(12);
        String content = matcher.group(13);

        StringBuilder builder = new StringBuilder();
        builder.append(buildSpan(time, "time")).append(SPACE);
        builder.append(buildSpan(level, "level level_" + level)).append(SPACE);
        builder.append(buildSpan(pid, "pid")).append(SPACE);
        builder.append(buildSpan(symbol, "symbol")).append(SPACE);
        builder.append(buildSpan(thread, "thread")).append(SPACE);
        builder.append(buildSpan(_class, "_class"));
        builder.append(buildSpan(label, "label"));
        builder.append(buildSpan(method, "method")).append(SPACE);
        builder.append(buildSpan(secondLabel, "symbol")).append(SPACE);
        builder.append(buildSpan(content, "content"));

        return builder.toString();
    }

    private String buildSpan(String text, String _class) {
        return "<span class='" + _class + "'>" + text + "</span>";
    }
}