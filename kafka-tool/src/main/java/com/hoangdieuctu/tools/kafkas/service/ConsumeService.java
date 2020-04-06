package com.hoangdieuctu.tools.kafkas.service;

import com.hoangdieuctu.tools.kafkas.config.WebsocketConnectionHolder;
import com.hoangdieuctu.tools.kafkas.model.ConsumerKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class ConsumeService {

    @Autowired
    private WebsocketConnectionHolder wsConnectionHolder;

    public void applyFilter(String randomId, String filter) {
        Map<String, ConsumerKey> sessions = wsConnectionHolder.getSessions();

        Optional<ConsumerKey> find = sessions.values().stream().filter(v -> randomId.equalsIgnoreCase(v.getCustomId())).findFirst();
        if (find.isPresent()) {
            ConsumerKey consumerKey = find.get();
            consumerKey.setFilter(filter);
            log.info("Applied filter [{}] for {}", filter, consumerKey);
        }
    }
}
