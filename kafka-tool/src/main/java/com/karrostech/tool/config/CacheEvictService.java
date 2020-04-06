package com.karrostech.tool.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Slf4j
@Component
public class CacheEvictService {

    @Autowired
    private CacheManager cacheManager;

    @Scheduled(fixedRate = 1000 * 60 * 60)
    public void clearAll() {
        log.info("Flush all cache items");

        Collection<String> names = cacheManager.getCacheNames();
        names.forEach(n -> {
            Cache cache = cacheManager.getCache(n);
            cache.clear();
        });
    }

    public void clear(String key) {
        Cache cache = cacheManager.getCache(key);
        if (cache != null) {
            cache.clear();
        }
    }
}

