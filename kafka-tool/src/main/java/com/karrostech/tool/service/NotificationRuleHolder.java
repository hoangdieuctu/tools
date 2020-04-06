package com.karrostech.tool.service;

import com.karrostech.tool.model.Environment;
import com.karrostech.tool.model.NotificationRule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Component
public class NotificationRuleHolder {

    private final Set<NotificationRule> rules = new HashSet<>();

    @Value("${notification.rules}")
    private String configureRules;

    @PostConstruct
    public void convertRules() {
        log.info("Convert rules: {}", configureRules);

        String[] confRules = configureRules.split(";");
        for (String confRule : confRules) {
            String[] parts = confRule.split(",");

            NotificationRule rule = new NotificationRule();
            rule.setEnv(Environment.forName(parts[0]));
            rule.setConsumerGroup(parts[1]);
            rule.setTopic(parts[2]);
            rule.setThreshold(Long.parseLong(parts[3]));
            rule.setActive(Boolean.parseBoolean(parts[4]));

            rules.add(rule);
        }
        log.info("Found {} rules", rules.size());
    }

    public Set<NotificationRule> getRules() {
        return rules;
    }
}
