package com.hoangdieuctu.tools.kafkas.controller;

import com.hoangdieuctu.tools.kafkas.service.ConsumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/consume")
public class ConsumeController extends KafkaController {

    @Autowired
    private ConsumeService consumeService;

    @GetMapping
    public String index(Model model) {
        super.setEnvsAndTopics(model);
        return "consume";
    }

    @ResponseBody
    @GetMapping("/topics")
    public List<String> getTopic(@RequestParam("env") String env,
                                 @RequestParam(value = "isFavOnly", required = false, defaultValue = "false") boolean isFavOnly) {
        return super.getTopics(env, isFavOnly);
    }

    @ResponseBody
    @GetMapping("/filter")
    public void applyFilter(@RequestParam("randomId") String randomId, @RequestParam("filter") String filter) {
        consumeService.applyFilter(randomId, filter);
    }
}
