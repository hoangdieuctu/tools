package com.hoangdieuctu.tools.klogs.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class CacheService {

    private static Logger logger = LoggerFactory.getLogger(CacheService.class);

    @Autowired
    private CacheManager cacheManager;

    public void flush() {
        logger.info("Flush cache");

        Collection<String> names = cacheManager.getCacheNames();
        names.forEach(n -> {
            Cache cache = cacheManager.getCache(n);
            cache.clear();
        });

    }
}

