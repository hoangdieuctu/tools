package com.karrostech.tool.service;

import com.google.gson.Gson;
import com.karrostech.tool.model.BulkProduce;
import com.karrostech.tool.model.Environment;
import com.karrostech.tool.model.Role;
import com.karrostech.tool.model.SendingTask;
import com.karrostech.tool.util.KafkaSenderUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Timer;

@Slf4j
@Component
public class BulkQueueConsumerService extends Thread {

    @Autowired
    private BulkRequestQueueHolder queueHolder;

    @Autowired
    private KafkaService kafkaService;

    @Value("${distribute.role}")
    private String role;

    @Value("${distribute.topic}")
    private String topic;

    private Timer timer = new Timer();

    @PostConstruct
    public void init() {
        log.info("Starting thread consume bulk request queue");
        this.start();
    }

    @PreDestroy
    public void close() {
        log.info("Stop timer");
        timer.cancel();
    }

    @Override
    public void run() {
        while (true) {
            try {
                BulkProduce take = queueHolder.take();
                if (Role.slave.name().equalsIgnoreCase(role)) {
                    log.info("Process local item: {}", take.getName());
                    processOnLocal(take);
                } else {
                    int slaves = take.getSlaves();
                    log.info("Process item={}, slaves={}", take.getName(), slaves);

                    if (slaves == 0) {
                        processOnLocal(take);
                    } else {
                        processOnRemote(take);
                    }
                }
            } catch (InterruptedException e) {
                log.warn("Interrupted when take an object from queue. ", e);
            }
        }
    }

    private void processOnRemote(BulkProduce take) {
        String json = new Gson().toJson(take);
        kafkaService.send(Environment.NON_DEV, topic, json);

        processOnLocal(take);
    }

    private void processOnLocal(BulkProduce take) {
        long start = System.currentTimeMillis();


        int loopCount = take.getLoopCount();

        if (loopCount <= 0) {
            log.info("Loop count is non-positive ({}), skip this job", loopCount);
        } else if (loopCount == 1) {
            KafkaSenderUtil.send(kafkaService, take);
        } else {
            log.info("Scheduled sending task with timer (s): {}", take.getLoopSleep());
            SendingTask task = new SendingTask(kafkaService, take);
            timer.schedule(task, 0, take.getLoopSleep() * 1000);

            log.info("Waiting for timer is completed");
            while (!task.isCompleted()) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException ignore) {
                }
            }
        }

        long end = System.currentTimeMillis();
        queueHolder.executed(take);
        log.info("Executed task [{}], take: {} millis", take.getName(), end - start);
    }
}
