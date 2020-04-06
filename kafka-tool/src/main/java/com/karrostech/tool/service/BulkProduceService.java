package com.karrostech.tool.service;

import com.google.gson.Gson;
import com.karrostech.tool.constant.Constants;
import com.karrostech.tool.model.BulkJobQueue;
import com.karrostech.tool.model.BulkProduce;
import com.karrostech.tool.repository.FileRepository;
import com.karrostech.tool.util.FileNameUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class BulkProduceService {

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private BulkRequestQueueHolder queueHolder;

    public void storeRequest(BulkProduce bulkProduce) {
        StringBuilder builder = new StringBuilder();
        builder.append(FileNameUtil.getBulkProduceFolder());
        builder.append(bulkProduce.getName()).append(".").append(Constants.FILE_EXTENSION);

        fileRepository.save(builder.toString(), new Gson().toJson(bulkProduce));
    }

    public List<String> getRequestedItems() {
        String[] extensions = {Constants.FILE_EXTENSION};
        String folder = FileNameUtil.getBulkProduceFolder();
        List<String> fileNames = fileRepository.list(folder, extensions);

        return fileNames.stream().map(f -> f.replace("." + Constants.FILE_EXTENSION, ""))
                .collect(Collectors.toList());
    }

    public BulkProduce getRequestedItem(String name) {
        StringBuilder builder = new StringBuilder();
        builder.append(FileNameUtil.getBulkProduceFolder());
        builder.append(name).append(".").append(Constants.FILE_EXTENSION);

        String json = fileRepository.read(builder.toString());
        if (StringUtils.isEmpty(json)) {
            return null;
        }

        return new Gson().fromJson(json, BulkProduce.class);
    }

    public void deleteRequestedItem(String name) {
        StringBuilder builder = new StringBuilder();
        builder.append(FileNameUtil.getBulkProduceFolder());
        builder.append(name).append(".").append(Constants.FILE_EXTENSION);

        fileRepository.delete(builder.toString());
    }

    public void executeRequestedItem(String name, int slaves) {
        log.info("Execute [{}] with slaves count {}", name, slaves);
        BulkProduce bulkProduce = getRequestedItem(name);
        if (bulkProduce == null) {
            return;
        }

        bulkProduce.setSlaves(slaves);
        queueHolder.add(bulkProduce);
    }

    public BulkJobQueue getQueue() {
        List<BulkProduce> running = queueHolder.getRunning();
        List<BulkProduce> scheduled = queueHolder.getScheduled();

        BulkJobQueue bulkJobQueue = new BulkJobQueue();
        bulkJobQueue.setRunning(running);
        bulkJobQueue.setScheduled(scheduled);

        return bulkJobQueue;
    }

    public void cancelRunningTasks() {
        List<BulkProduce> runningTasks = queueHolder.getRunning();
        runningTasks.forEach(BulkProduce::forceStop);
    }
}
