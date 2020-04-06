package com.karrostech.tool.service;

import com.karrostech.tool.model.BulkProduce;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

@Slf4j
@Component
public class BulkRequestQueueHolder {

    private final BlockingDeque<BulkProduce> queue = new LinkedBlockingDeque<>(10);

    private final List<BulkProduce> scheduled = new Vector<>();
    private final List<BulkProduce> running = new Vector<>();

    public void add(BulkProduce bulkProduce) {
        scheduled.add(bulkProduce);
        queue.add(bulkProduce);
        log.info("Added job to queue, queue size: {}", queue.size());
    }

    public BulkProduce take() throws InterruptedException {
        BulkProduce take = queue.take();
        scheduled.remove(take);
        running.add(take);
        return take;
    }

    public void executed(BulkProduce bulkProduce) {
        running.remove(bulkProduce);
        log.info("Running task: {}, scheduled task: {}, queue: {}", running.size(), scheduled.size(), queue.size());
    }

    public List<BulkProduce> getScheduled() {
        return scheduled;
    }

    public List<BulkProduce> getRunning() {
        return running;
    }
}
