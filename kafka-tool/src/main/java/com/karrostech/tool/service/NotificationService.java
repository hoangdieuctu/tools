package com.karrostech.tool.service;

import com.karrostech.tool.model.Environment;
import com.karrostech.tool.model.NotificationRule;
import com.karrostech.tool.model.PushChannel;
import com.karrostech.tool.model.PushMessage;
import com.omarsmak.kafka.consumer.lag.monitoring.client.data.Lag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class NotificationService {

    @Autowired
    private PushNotificationService pushNotificationService;

    @Autowired
    private ThreadPoolExecutorService executorService;

    @Autowired
    private NotificationRuleHolder ruleHolder;

    @Autowired
    private KafkaService kafkaService;

    public void executeActiveRules() {
        Set<NotificationRule> rules = ruleHolder.getRules();
        if (CollectionUtils.isEmpty(rules)) {
            return;
        }

        Set<NotificationRule> activeRules = rules.stream().filter(NotificationRule::isActive).collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(activeRules)) {
            return;
        }

        log.info("Found {} active notification rules", activeRules.size());
        activeRules.forEach(s -> executorService.execute(() -> executeRule(s)));
    }

    private void executeRule(NotificationRule rule) {
        Environment env = rule.getEnv();
        String topic = rule.getTopic();
        String groupId = rule.getConsumerGroup();
        long threshold = rule.getThreshold();

        try {
            List<Lag> lags = kafkaService.getConsumerLags(env, groupId);
            if (CollectionUtils.isEmpty(lags)) {
                return;
            }

            Optional<Lag> optLag = lags.stream().filter(l -> l.getTopicName().equalsIgnoreCase(topic)).findFirst();
            if (!optLag.isPresent()) {
                return;
            }

            Lag lag = optLag.get();
            long totalLag = lag.getTotalLag();
            if (totalLag > threshold) {
                log.info("Total lag({}) gt threshold({})", totalLag, threshold);

                String messageTitle = "Consumer lag too high";

                StringBuilder messageBody = new StringBuilder();
                messageBody.append("Total lag is higher than threshold. \n");
                messageBody.append("Consumer group: ").append(bold(groupId)).append("\n");
                messageBody.append("Topic: ").append(bold(topic)).append("\n");
                messageBody.append("Threshold: ").append(bold(threshold)).append("\n");
                messageBody.append("Total lag: ").append(bold(totalLag)).append("\n");

                PushMessage message = new PushMessage();
                message.setTitle(messageTitle);
                message.setContent(messageBody.toString());

                List<PushChannel> channels = Collections.singletonList(PushChannel.GOOGLE_CHAT);
                pushNotificationService.sendNotification(channels, message);
            }

            log.info("Validate negative lag rule for topic={}, group={}", topic, groupId);

            Map<Integer, Long> partitionLags = lag.getLagPerPartition();
            Set<Integer> partitions = partitionLags.keySet();

            Optional<Integer> negativeLag = partitions.stream().filter(p -> {
                Long partitionLag = partitionLags.get(p);
                return partitionLag < 0;
            }).findFirst();

            if (negativeLag.isPresent()) {
                Integer partition = negativeLag.get();

                log.info("Found negative lag");
                StringBuilder messageBody = new StringBuilder();
                messageBody.append("Found a negative lag. \n");
                messageBody.append("Consumer group: ").append(bold(groupId)).append("\n");
                messageBody.append("Topic: ").append(bold(topic)).append("\n");
                messageBody.append("Partition: ").append(bold(partition)).append("\n");
                messageBody.append("Lag: ").append(bold(partitionLags.get(partition))).append("\n");

                PushMessage message = new PushMessage();
                message.setTitle("Negative lag found");
                message.setContent(messageBody.toString());

                List<PushChannel> channels = Collections.singletonList(PushChannel.GOOGLE_CHAT);
                pushNotificationService.sendNotification(channels, message);
            }
        } catch (Exception ex) {
            log.error("Error while checking rule: {}", rule);
            log.error("Error: {}", ex);
        }
    }

    private String bold(Object value) {
        return "*" + value + "*";
    }

}
