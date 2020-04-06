package com.hoangdieuctu.tools.kafkas.service;

import com.google.common.collect.EvictingQueue;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hoangdieuctu.tools.kafkas.model.ProduceHistory;
import com.hoangdieuctu.tools.kafkas.util.FileNameUtil;
import com.hoangdieuctu.tools.kafkas.exception.KafkaToolException;
import com.hoangdieuctu.tools.kafkas.repository.FileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class ProduceHistoryHolder {

    private static final String HISTORY_FILE_NAME = "history.json";

    @Value("${produce.history.items}")
    private int historySize;

    @Autowired
    private FileRepository fileRepository;

    private EvictingQueue<ProduceHistory> histories;

    @PostConstruct
    public void init() {
        log.info("Init evicting queue for produce history: {}", historySize);
        histories = EvictingQueue.create(historySize);

        log.info("Loading produce history");
        String path = getHistoryFilePath();
        String json = fileRepository.read(path);
        if (json != null && !json.isEmpty()) {
            Type type = new TypeToken<ArrayList<ProduceHistory>>() {
            }.getType();
            List<ProduceHistory> items = new Gson().fromJson(json, type);
            if (!CollectionUtils.isEmpty(items)) {
                items.forEach(i -> histories.add(i));
            }
        }
    }

    @PreDestroy
    public void destroy() {
        log.info("Saving produce histories: {}", histories.size());

        List<ProduceHistory> items = getAll();
        String json = new Gson().toJson(items);
        String path = getHistoryFilePath();

        fileRepository.save(path, json);
        log.info("Saved produce histories");
    }

    public void add(ProduceHistory history) {
        histories.add(history);
    }

    public List<ProduceHistory> getAll() {
        List<ProduceHistory> results = new ArrayList<>(histories.size());
        histories.forEach(h -> results.add(h));
        return results;
    }

    public ProduceHistory getById(String id) {
        Optional<ProduceHistory> optHistory = histories.stream().filter(h -> h.getId().equalsIgnoreCase(id)).findFirst();
        if (optHistory.isPresent()) {
            return optHistory.get();
        }

        throw new KafkaToolException("History not found");
    }

    private String getHistoryFilePath() {
        StringBuilder builder = new StringBuilder();
        builder.append(FileNameUtil.getProduceFolder());
        builder.append(HISTORY_FILE_NAME);
        return builder.toString();
    }
}
