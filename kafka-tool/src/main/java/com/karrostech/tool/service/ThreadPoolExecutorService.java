package com.karrostech.tool.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class ThreadPoolExecutorService {

    @Value("${executor.thread.pool:10}")
    private int pool;

    private ExecutorService executor;

    @PostConstruct
    public void initThreadPool() {
        log.info("Init thread pool: {}", pool);
        executor = Executors.newFixedThreadPool(pool);
    }

    @PreDestroy
    public void shutdownThreadPool() {
        log.info("Shutdown thread pool");
        executor.shutdown();
        try {
            executor.awaitTermination(pool, TimeUnit.SECONDS);
        } catch (InterruptedException ignore) {
        }
    }

    public void execute(Runnable runnable) {
        executor.execute(runnable);
    }
}
