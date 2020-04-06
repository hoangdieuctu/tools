package com.karrostech.tool.controller;

import com.karrostech.tool.service.LogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
@RequestMapping("/logs")
public class LogController {

    @Autowired
    private LogService logService;

    @GetMapping
    public String index(Model model) {
        String logs = logService.getLogs();
        model.addAttribute("logs", logs);

        return "log";
    }

    @ResponseBody
    @GetMapping(value = "/content", produces = "text/html")
    public String getLogs() {
        return logService.getLogs();
    }
}
