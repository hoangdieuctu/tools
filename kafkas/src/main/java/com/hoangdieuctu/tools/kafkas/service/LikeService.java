package com.hoangdieuctu.tools.kafkas.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.gson.Gson;
import com.hoangdieuctu.tools.kafkas.model.IndexData;
import com.hoangdieuctu.tools.kafkas.model.LikeData;
import com.hoangdieuctu.tools.kafkas.model.LikeSession;
import com.hoangdieuctu.tools.kafkas.util.FileNameUtil;
import com.hoangdieuctu.tools.kafkas.repository.FileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class LikeService {

    private int likeCount = 0;
    private LoadingCache<String, LikeSession> cache;

    private static final String INDEX_FILE_NAME = "index.json";

    @Autowired
    private FileRepository fileRepository;

    public LikeService() {
        cache = CacheBuilder.newBuilder()
                .expireAfterWrite(30, TimeUnit.MINUTES)
                .build(new CacheLoader<String, LikeSession>() {
                    @Override
                    public LikeSession load(String key) {
                        return new LikeSession(key);
                    }
                });
    }

    private String getIndexFilePath() {
        String path = new StringBuilder(FileNameUtil.getIndexFolder())
                .append("/").append(INDEX_FILE_NAME)
                .toString();
        return path;
    }

    @PostConstruct
    public void loadLikeCount() {
        String path = getIndexFilePath();
        String content = fileRepository.read(path);
        if (!StringUtils.isEmpty(content)) {
            IndexData indexData = new Gson().fromJson(content, IndexData.class);
            likeCount = indexData.getLikeCount();
        }

        log.info("Loaded like count: {}", likeCount);
    }

    @PreDestroy
    public void storeLikeCount() {
        log.info("Storing like count: {}", likeCount);

        String path = getIndexFilePath();
        IndexData indexData = new IndexData();
        indexData.setLikeCount(likeCount);
        fileRepository.save(path, new Gson().toJson(indexData));
    }

    public synchronized LikeData like(String sessionId) {
        LikeData likeData = new LikeData();
        likeData.setLiked(true);

        LikeSession likeSession = cache.getIfPresent(sessionId);
        if (likeSession != null) {
            likeData.setLikeCount(likeCount);
        } else {
            cache.put(sessionId, new LikeSession(sessionId));
            likeData.setLikeCount(++likeCount);
        }

        return likeData;
    }

    public LikeData getLikeData(String sessionId) {
        LikeSession likeSession = cache.getIfPresent(sessionId);

        LikeData likeData = new LikeData();
        likeData.setLikeCount(likeCount);
        likeData.setLiked(likeSession != null);

        return likeData;
    }
}
