package com.karrostech.tool.service;

import com.google.gson.Gson;
import com.karrostech.tool.config.CacheEvictService;
import com.karrostech.tool.config.WebsocketConnectionHolder;
import com.karrostech.tool.constant.Constants;
import com.karrostech.tool.messaging.WebsocketMessageSender;
import com.karrostech.tool.model.*;
import com.karrostech.tool.repository.FileRepository;
import com.karrostech.tool.repository.KafkaRepository;
import com.karrostech.tool.util.FileNameUtil;
import lombok.extern.slf4j.Slf4j;
import net.lingala.zip4j.exception.ZipException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
public class AdminService {

    private static final String SETTING_FILE_NAME = "topic-exclusion.json";
    private static final String FAV_TOPIC_FILE_NAME = "topic-favorite.json";
    private static final String BULK_CONFIG_FILE_NAME = "bulk-config.json";
    private static final String PRODUCE_FOLDER_CONFIG_FILE_NAME = "produce-folder-config.json";

    private static final String ZIP_FILE_NAME = "kafka-tool.zip";

    @Autowired
    private CacheEvictService cacheEvictService;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private KafkaRepository kafkaRepository;

    @Autowired
    private WebsocketConnectionHolder wsConnectionHolder;

    @Autowired
    private WebsocketMessageSender wsMessageSender;

    private String getTopicExclusionSettingPath() {
        String path = new StringBuilder(FileNameUtil.getSettingFolder())
                .append("/").append(SETTING_FILE_NAME)
                .toString();
        return path;
    }

    private String getFavTopicsPath() {
        String path = new StringBuilder(FileNameUtil.getSettingFolder())
                .append("/").append(FAV_TOPIC_FILE_NAME)
                .toString();
        return path;
    }

    private String getBulkConfigPath() {
        String path = new StringBuilder(FileNameUtil.getSettingFolder())
                .append("/").append(BULK_CONFIG_FILE_NAME)
                .toString();
        return path;
    }

    private String getProduceFolderConfigPath() {
        String path = new StringBuilder(FileNameUtil.getSettingFolder())
                .append("/").append(PRODUCE_FOLDER_CONFIG_FILE_NAME)
                .toString();
        return path;
    }

    public void addTopicExclusion(String topic) {
        TopicExclusionSetting topicExclusion = getTopicExclusion();
        List<String> topics = topicExclusion.getTopics();
        if (!topics.contains(topic)) {
            topics.add(topic);

            saveTopicExclusion(topicExclusion);
        }
    }

    public void removeTopicExclusion(String topic) {
        TopicExclusionSetting topicExclusion = getTopicExclusion();
        List<String> topics = topicExclusion.getTopics();
        topics.remove(topic);

        saveTopicExclusion(topicExclusion);
    }

    public void saveTopicExclusion(TopicExclusionSetting topicExclusionSetting) {
        String path = getTopicExclusionSettingPath();
        String content = new Gson().toJson(topicExclusionSetting);
        fileRepository.save(path, content);
    }

    public TopicExclusionSetting getTopicExclusion() {
        String path = getTopicExclusionSettingPath();

        String setting = fileRepository.read(path);
        if (StringUtils.isEmpty(setting)) {
            return new TopicExclusionSetting();
        }

        return new Gson().fromJson(setting, TopicExclusionSetting.class);
    }

    @Cacheable(Constants.CACHEABLE_TOPICS_EXCLUSION)
    public List<String> getTopicsExclusion() {
        TopicExclusionSetting exclusion = getTopicExclusion();
        return exclusion.getTopics();
    }

    public Map<String, ConsumerKey> getClientConsumers() {
        return wsConnectionHolder.getSessions();
    }

    public void sendCustomMessage(String content) {
        Map<String, ConsumerKey> sessions = wsConnectionHolder.getSessions();
        Set<String> keys = sessions.keySet();
        keys.parallelStream().forEach(sessionId -> sendCustomMessage(sessionId, content));
    }

    public void sendCustomMessage(String sessionId, String content) {
        ConsumerKey key = wsConnectionHolder.getConsumerKey(sessionId);
        if (key == null) {
            return;
        }

        CustomMessage message = new CustomMessage();
        message.setSessionId(sessionId);
        message.setMessage(content);

        wsMessageSender.send(key, message);
    }

    public FavoriteTopicSetting getFavTopics() {
        String path = getFavTopicsPath();

        String favTopics = fileRepository.read(path);
        if (StringUtils.isEmpty(favTopics)) {
            return new FavoriteTopicSetting();
        }

        return new Gson().fromJson(favTopics, FavoriteTopicSetting.class);
    }

    public void saveFavTopic(String requestTopics) {
        FavoriteTopicSetting favTopics = getFavTopics();
        List<String> topics = favTopics.getTopics();

        String[] items = requestTopics.split(",");
        for (String topic : items) {
            if (!topics.contains(topic)) {
                topics.add(topic);
            }
        }
        saveFavTopicToFile(favTopics);
    }

    public void removeFavTopic(String topic) {
        FavoriteTopicSetting favTopics = getFavTopics();
        List<String> topics = favTopics.getTopics();
        if (topics.contains(topic)) {
            topics.remove(topic);
            saveFavTopicToFile(favTopics);
        }
    }

    public void saveFavTopicToFile(FavoriteTopicSetting favTopic) {
        String path = getFavTopicsPath();
        String content = new Gson().toJson(favTopic);
        fileRepository.save(path, content);
    }

    @Cacheable(Constants.CACHEABLE_FAVORITE_TOPICS)
    public List<String> getFavTopicsSetting() {
        return getFavTopics().getTopics();
    }

    public boolean updateBulkConfig(BulkConfig bulkConfig) {
        if (!bulkConfig.validate()) {
            return false;
        }

        BulkConfigSetting bulkConfigSetting = new BulkConfigSetting();
        bulkConfigSetting.setElements(bulkConfig.getElementList());
        bulkConfigSetting.setThreads(bulkConfig.getThreadList());

        fileRepository.save(getBulkConfigPath(), new Gson().toJson(bulkConfigSetting));

        return true;
    }

    public BulkConfigSetting getBulkConfig() {
        String path = getBulkConfigPath();
        String content = fileRepository.read(path);

        return new Gson().fromJson(content, BulkConfigSetting.class);
    }

    public void saveProduceFolder(String folder) {
        ProduceFolderSetting produceFolder = getProduceFolderSetting();
        List<String> folders = produceFolder.getFolders();
        if (folders.contains(folder)) {
            return;
        }
        folders.add(folder);

        String path = getProduceFolderConfigPath();
        String content = new Gson().toJson(produceFolder);
        fileRepository.save(path, content);
    }

    @Cacheable(Constants.CACHEABLE_PRODUCE_FOLDER)
    public List<String> getStorageFolders() {
        return getAdminStorageFolders();
    }

    public List<String> getAdminStorageFolders() {
        List<String> folders = getProduceFolderSetting().getFolders();
        folders.add(0, Constants.ROOT_STORAGE);

        return folders;
    }

    private ProduceFolderSetting getProduceFolderSetting() {
        String path = getProduceFolderConfigPath();

        String produceFolders = fileRepository.read(path);
        if (StringUtils.isEmpty(produceFolders)) {
            return new ProduceFolderSetting();
        }

        return new Gson().fromJson(produceFolders, ProduceFolderSetting.class);
    }

    public InputStream backup() throws ZipException, FileNotFoundException {
        log.info("Backing up system");

        StringBuilder builder = new StringBuilder();
        builder.append(Constants.DOT_STRING)
                .append(Constants.SPLASH_STRING)
                .append(FileNameUtil.getRootFolder());

        fileRepository.zipFolder(ZIP_FILE_NAME, builder.toString());

        return new FileInputStream(ZIP_FILE_NAME);
    }

    public void deleteConsumerGroup(Environment env, String groupId) {
        log.info("Delete consumer group, env={}, groupId={}", env, groupId);
        kafkaRepository.deleteConsumerGroups(env, Collections.singleton(groupId));

        log.info("Evicting cache key: {}", Constants.CACHEABLE_CONSUMER_GROUPS);
        cacheEvictService.clear(Constants.CACHEABLE_CONSUMER_GROUPS);
    }
}
