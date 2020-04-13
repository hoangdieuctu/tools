package com.hoangdieuctu.tools.klogs.controller;

import com.hoangdieuctu.tools.klogs.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/cache")
public class CacheController {

    @Autowired
    private CacheService cacheService;

    @GetMapping("/flush")
    @ResponseBody
    public void flush() {
        cacheService.flush();
    }
}
