package com.hoangdieuctu.tools.dlogs.controller;

import com.hoangdieuctu.tools.dlogs.service.LogService;
import io.kubernetes.client.openapi.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/log")
public class LogController {

    @Autowired
    private LogService logService;

    @GetMapping("/open")
    public void socket(@RequestParam("pod") String pod) throws ApiException {
        logService.connect("abc", pod);
    }

    @GetMapping("/close")
    public void close() {
        logService.close("abc");
    }
}
