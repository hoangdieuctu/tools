package com.karrostech.tool.util;

import com.karrostech.tool.model.BulkProduce;
import com.karrostech.tool.service.KafkaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class KafkaSenderUtil {

    public static void send(KafkaService kafkaService, BulkProduce take) {
        ExecutorService executor = Executors.newFixedThreadPool(take.getThreadCount());

        int workers = take.getSlaves() + 1; // 1 from master
        for (int i = 0; i < take.getElementCount() / workers; i++) {
            Runnable worker = () -> {
                List<String> messages = take.getBody();
                for (int j = 0; j < messages.size(); j++) {
                    String body = messages.get(j);

                    List<String> keys = take.getKeys();

                    String key = null;
                    try {
                        key = CollectionUtils.isEmpty(keys) ? null : keys.get(j);
                    } catch (Exception ignore) {
                        log.warn("Error while getting key");
                    }

                    kafkaService.send(take.getEnv(), take.getTopic(), null, StringUtils.isEmpty(key) ? null : key, body);
                }

                Long sleep = take.getSleep();
                if (sleep > 0L) {
                    try {
                        Thread.sleep(sleep);
                    } catch (InterruptedException ignore) {
                        log.warn("Exception when sleeping in executing job");
                    }
                }
            };
            executor.execute(worker);
        }
        executor.shutdown();

        log.info("Waiting for the threads is terminated!");
        while (!executor.isTerminated()) {
        }
    }

}
