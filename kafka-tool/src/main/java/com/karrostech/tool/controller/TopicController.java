package com.karrostech.tool.controller;

import com.karrostech.tool.model.Environment;
import com.karrostech.tool.model.PartitionData;
import com.karrostech.tool.model.TopicConfigValue;
import com.karrostech.tool.model.TopicInfo;
import com.karrostech.tool.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/topic")
public class TopicController extends KafkaController {

    @Autowired
    private TopicService topicService;

    @GetMapping
    public String index(Model model) {
        super.setEnvsAndTopics(model);
        return "topic";
    }

    @GetMapping("/detail")
    public String detail(Model model, @RequestParam("topic") String topic, @RequestParam("env") Environment env) {
        model.addAttribute("topic", topic);
        model.addAttribute("env", env);

        List<TopicConfigValue> configs = topicService.describeTopic(env, topic);
        model.addAttribute("configs", configs);

        List<PartitionData> details = topicService.getTopicDetails(env, topic);
        model.addAttribute("details", details);

        return "topic-detail";
    }

    @GetMapping("/all")
    @ResponseBody
    public List<TopicInfo> getAll(@RequestParam("env") Environment env) {
        return topicService.getAll(env);
    }

    @GetMapping("/describe")
    @ResponseBody
    public List<TopicConfigValue> describeTopic(@RequestParam("env") Environment env, @RequestParam("topic") String topic) {
        return topicService.describeTopic(env, topic);
    }
}
