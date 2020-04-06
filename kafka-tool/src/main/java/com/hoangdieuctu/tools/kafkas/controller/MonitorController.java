package com.hoangdieuctu.tools.kafkas.controller;

import com.hoangdieuctu.tools.kafkas.model.ConsumerGroupDetail;
import com.hoangdieuctu.tools.kafkas.model.Environment;
import com.hoangdieuctu.tools.kafkas.service.MonitorService;
import com.omarsmak.kafka.consumer.lag.monitoring.client.data.Lag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping({"/monitor", "/consumer-group"})
public class MonitorController extends KafkaController {

    @Autowired
    private MonitorService monitorService;

    @GetMapping
    public String index(Model model) {
        super.setEnvsAndTopics(model);
        return "monitor";
    }

    @GetMapping("/detail")
    public String detail(Model model, @RequestParam("groupId") String groupId, @RequestParam("env") Environment env) {
        model.addAttribute("groupId", groupId);
        model.addAttribute("env", env);
        return "monitor-detail";
    }

    @ResponseBody
    @GetMapping("/consumer-groups/{env}")
    public List<String> getConsumerGroups(@PathVariable("env") Environment env) {
        return monitorService.getConsumerGroups(env);
    }

    @ResponseBody
    @GetMapping("/consumer-groups/{env}/describe/{groupId:.+}")
    public ConsumerGroupDetail getConsumerGroups(@PathVariable("env") Environment env, @PathVariable("groupId") String groupId) {
        return monitorService.describeConsumerGroup(env, groupId);
    }

    @ResponseBody
    @PostMapping("/consumer-groups/{env}/describe")
    public Map<String, ConsumerGroupDetail> getConsumerGroups(@PathVariable("env") Environment env, @RequestBody List<String> groupIds) {
        return monitorService.describeConsumerGroups(env, groupIds);
    }

    @ResponseBody
    @GetMapping("/consumer-lag")
    public List<Lag> getConsumerLag(@RequestParam("env") Environment env,
                                    @RequestParam("groupId") String groupId) {
        return monitorService.getConsumerLags(env, groupId);
    }
}
