package com.karrostech.tool.service;

import com.karrostech.tool.model.PushMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class GoogleChatService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${google.chat.url}")
    private String googleChatUrl;

    public void sendMsg2ChatChanel(PushMessage msg) {
        log.info("Send message to Google chat chanel");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

        Map<String, String> content = new HashMap<>();
        content.put("text", msg.getContent());

        HttpEntity<Map<String, String>> request = new HttpEntity<>(content, headers);
        String response = restTemplate.postForObject(googleChatUrl, request, String.class);
        log.info("Response: {}", response);
    }
}
