package com.hoangdieuctu.tools.kafkas.controller;

import com.hoangdieuctu.tools.kafkas.model.ConsumerKey;
import com.hoangdieuctu.tools.kafkas.model.Environment;
import com.hoangdieuctu.tools.kafkas.model.FavoriteTopicSetting;
import com.hoangdieuctu.tools.kafkas.model.TopicExclusionSetting;
import com.hoangdieuctu.tools.kafkas.service.AdminService;
import com.hoangdieuctu.tools.kafkas.util.DateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping
    public String index() {
        return "admin";
    }

    @ResponseBody
    @GetMapping("/topic-exclusion")
    public TopicExclusionSetting getTopicExclusion() {
        return adminService.getTopicExclusion();
    }

    @ResponseBody
    @PostMapping("/topic-exclusion")
    public void addTopicExclusion(@RequestBody String topic) {
        adminService.addTopicExclusion(topic);
    }

    @ResponseBody
    @DeleteMapping("/topic-exclusion")
    public void removeTopicExclusion(@RequestBody String topic) {
        adminService.removeTopicExclusion(topic);
    }

    @ResponseBody
    @GetMapping("/client-consumers")
    public Map<String, ConsumerKey> getClientConsumers() {
        return adminService.getClientConsumers();
    }

    @ResponseBody
    @PostMapping("/send-custom-message")
    public void sendCustomMessage(@RequestBody String message) {
        adminService.sendCustomMessage(message);
    }

    @ResponseBody
    @PostMapping("/send-custom-message/{sessionId}")
    public void sendCustomMessage(@PathVariable("sessionId") String sessionId, @RequestBody String message) {
        adminService.sendCustomMessage(sessionId, message);
    }

    @ResponseBody
    @GetMapping("/favorite-topics")
    public FavoriteTopicSetting getFavoriteTopicSetting() {
        return adminService.getFavTopics();
    }

    @ResponseBody
    @PostMapping("/favorite-topics/{topics}")
    public void saveFavoriteTopics(@PathVariable(name = "topics") String topics) {
        adminService.saveFavTopic(topics);
    }

    @ResponseBody
    @DeleteMapping("/favorite-topics/{topic}")
    public void deleteFavoriteTopics(@PathVariable(name = "topic") String topic) {
        adminService.removeFavTopic(topic);
    }

    @ResponseBody
    @PostMapping("/produce-folder/{folder}")
    public void addProduceFolder(@PathVariable(name = "folder") String folder) {
        adminService.saveProduceFolder(folder);
    }

    @ResponseBody
    @GetMapping("/produce-folder")
    public List<String> getProduceFolder() {
        return adminService.getAdminStorageFolders();
    }

    @ResponseBody
    @GetMapping(value = "/backup")
    public void backup(HttpServletResponse response) throws IOException {
        StringBuilder builder = new StringBuilder();
        builder.append("kafka-tool-").append(DateTimeUtil.getBaicDate()).append(".zip");

        response.setStatus(HttpServletResponse.SC_OK);
        response.addHeader("Content-Disposition", "attachment; filename=" + builder.toString());

        OutputStream oStream = response.getOutputStream();
        try {
            InputStream iStream = adminService.backup();
            IOUtils.copy(iStream, oStream);

            IOUtils.closeQuietly(iStream);
            oStream.flush();
        } catch (ZipException e) {
            log.error("Could not zip file. {}", e.getMessage());
        } catch (FileNotFoundException e) {
            log.error("Could not load zip file. {}", e.getMessage());
        } finally {
            IOUtils.closeQuietly(oStream);
        }
    }

    @ResponseBody
    @DeleteMapping("/consumer-group")
    public void deleteConsumerGroup(@RequestParam("env") Environment env, @RequestParam("groupId") String groupId) {
        adminService.deleteConsumerGroup(env, groupId);
    }
}
