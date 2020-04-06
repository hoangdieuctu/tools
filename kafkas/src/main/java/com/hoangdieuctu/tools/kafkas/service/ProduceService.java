package com.hoangdieuctu.tools.kafkas.service;

import com.google.gson.Gson;
import com.hoangdieuctu.tools.kafkas.constant.Constants;
import com.hoangdieuctu.tools.kafkas.model.*;
import com.hoangdieuctu.tools.kafkas.repository.FileRepository;
import com.hoangdieuctu.tools.kafkas.util.EnvironmentHolder;
import com.hoangdieuctu.tools.kafkas.util.FileNameUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ProduceService {

    private static final Pattern JSON_FILE_PATTERN = Pattern.compile("(.+)_(\\d+)(.json)");

    @Autowired
    private ProduceHistoryHolder historyHolder;

    @Autowired
    private KafkaService kafkaService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private EnvironmentHolder envsHolder;

    public KafkaRecordData sendKafkaMsg(String host, String env, String topic, Integer partition, String message) {
        log.info("Sending message to topic: {}", topic);
        EnvConfig config = envsHolder.getEnv(env);
        KafkaRecordData result = kafkaService.send(config, topic, partition, message);
        try {
            return result;
        } finally {
            ProduceContent content = new ProduceContent();
            content.setEnv(env);
            content.setTopic(topic);
            content.setJson(message);

            ProduceResponse response = new ProduceResponse();
            response.setTimestamp(result.getTimestamp());
            response.setPartition(result.getPartition());
            response.setOffset(result.getOffset());

            ProduceHistory history = new ProduceHistory();
            history.setContent(content);
            history.setResponse(response);
            history.setHost(host);

            historyHolder.add(history);
        }
    }

    public JsonFile saveProduceFile(JsonFile jsonFile) {
        long createdTime = System.currentTimeMillis();

        String folder = jsonFile.getFolder();

        StringBuilder builder = new StringBuilder();
        builder.append(isRootFolder(folder) ? FileNameUtil.getProduceFolder() : FileNameUtil.getProduceFolder(folder));
        builder.append(jsonFile.getName()).append("_").append(createdTime);
        builder.append(".").append(Constants.FILE_EXTENSION);

        String fullFileName = builder.toString();
        jsonFile.setFullName(fullFileName);
        jsonFile.setCreatedTime(createdTime);

        ProduceContent content = jsonFile.getContent();
        String json = new Gson().toJson(content);

        fileRepository.save(jsonFile.getFullName(), json);

        return jsonFile;
    }

    public List<JsonFile> listProduceFiles(String folder) {
        String[] extensions = {Constants.FILE_EXTENSION};
        String jsonFolder = isRootFolder(folder) ? FileNameUtil.getProduceFolder() : FileNameUtil.getProduceFolder(folder);
        List<String> fileNames = fileRepository.listV2(jsonFolder, extensions);

        String prefix = FileNameUtil.getProduceFolder();
        List<String> fileNamesWithPrefix = fileNames.stream()
                .map(f -> f.replace(prefix, Constants.EMPTY_STRING))
                .collect(Collectors.toList());

        List<JsonFile> results = new ArrayList<>();
        fileNamesWithPrefix.forEach(f -> {
            int index = f.indexOf('/');

            String pre = Constants.EMPTY_STRING;
            String suf = f;

            if (index != -1) { // this is root folder
                pre = f.substring(0, index);
                suf = f.substring(index + 1);
            }

            JsonFile jsonFile = convertToJsonFile(suf);
            if (jsonFile != null) {
                jsonFile.setFolder(Constants.EMPTY_STRING.equalsIgnoreCase(pre) ? Constants.ROOT_STORAGE : pre);
                results.add(jsonFile);
            }
        });

        return results.stream().sorted((f, s) -> Long.compare(s.getCreatedTime(), f.getCreatedTime())).collect(Collectors.toList());
    }

    private boolean isRootFolder(String folder) {
        return Constants.ROOT_STORAGE.equals(folder);
    }

    public JsonFile getJsonFile(String folder, String fileName) {
        String path = getFullJsonPath(folder, fileName);

        String json = fileRepository.read(path);
        ProduceContent content = new Gson().fromJson(json, ProduceContent.class);

        JsonFile jsonFile = convertToJsonFile(fileName);
        jsonFile.setContent(content);
        return jsonFile;
    }

    public List<ProduceHistory> getAllProduceHistories() {
        List<ProduceHistory> histories = historyHolder.getAll();
        Collections.reverse(histories);
        return histories;
    }

    public List<ProduceHistory> getHistories(int from, int size) {
        List<ProduceHistory> allItems = getAllProduceHistories();

        int to = from + size;
        if (to > allItems.size()) {
            to = allItems.size();
        }

        List<ProduceHistory> results = allItems.subList(from, to);
        return results;
    }

    public ProduceHistory getProduceHistory(String id) {
        return historyHolder.getById(id);
    }

    private JsonFile convertToJsonFile(String fileName) {
        Matcher matcher = JSON_FILE_PATTERN.matcher(fileName);
        if (matcher.matches()) {
            String name = matcher.group(1);
            String createdTime = matcher.group(2);

            JsonFile jsonFile = new JsonFile();
            jsonFile.setFullName(fileName);
            jsonFile.setName(name);
            jsonFile.setCreatedTime(Long.valueOf(createdTime));
            return jsonFile;
        }

        return null;
    }

    private String getFullJsonPath(String folder, String fileName) {
        StringBuilder builder = new StringBuilder();
        builder.append(isRootFolder(folder) ? FileNameUtil.getProduceFolder() : FileNameUtil.getProduceFolder(folder));
        builder.append(fileName);

        return builder.toString();
    }

    public void deleteJsonFile(String folder, String fileName) {
        String path = getFullJsonPath(folder, fileName);
        fileRepository.delete(path);
    }

    public List<String> getStorageFolders() {
        return adminService.getStorageFolders();
    }

    public List<PartitionData> getTopicDetails(String env, String topic) {
        return kafkaService.getTopicDetails(envsHolder.getEnv(env), topic);
    }
}
