package com.karrostech.tool.model;

import com.karrostech.tool.service.KafkaService;
import com.karrostech.tool.util.KafkaSenderUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.TimerTask;

@Slf4j
public class SendingTask extends TimerTask {

    private boolean completed = false;
    private int runTimes = 0;

    private BulkProduce take;
    private KafkaService kafkaService;

    public SendingTask(KafkaService kafkaService, BulkProduce take) {
        this.kafkaService = kafkaService;
        this.take = take;
    }

    @Override
    public void run() {
        if (take.isStopped()) {
            this.done();
            return;
        }

        KafkaSenderUtil.send(kafkaService, take);

        if (++runTimes == take.getLoopCount()) {
            this.done();
        } else {
            this.take.setPercent(runTimes * 100 / take.getLoopCount());
        }
    }

    private void done() {
        this.cancel();
        this.complete();
    }

    private void complete() {
        this.completed = true;
    }

    public boolean isCompleted() {
        return completed || take.isStopped();
    }
}
