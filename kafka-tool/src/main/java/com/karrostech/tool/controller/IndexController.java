package com.karrostech.tool.controller;

import com.karrostech.tool.config.CacheEvictService;
import com.karrostech.tool.model.LikeData;
import com.karrostech.tool.service.AvlEventService;
import com.karrostech.tool.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping({"/", "/index"})
public class IndexController {

    @Autowired
    private LikeService likeService;

    @Autowired
    private CacheEvictService cacheEvictService;

    @Autowired
    private AvlEventService avlEventService;

    @GetMapping
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @ResponseBody
    @GetMapping("/cache/flush")
    public void flushCache() {
        cacheEvictService.clearAll();
    }

    @ResponseBody
    @GetMapping("/notification/check")
    public void notificationCheck() {
        avlEventService.sendAvlEvents();
    }

    @ResponseBody
    @PostMapping("/like")
    public LikeData like(HttpServletRequest request) {
        String sessionId = request.getRequestedSessionId();
        return likeService.like(sessionId);
    }

    @ResponseBody
    @GetMapping("/like")
    public LikeData getLikeData(HttpServletRequest request) {
        String sessionId = request.getRequestedSessionId();
        return likeService.getLikeData(sessionId);
    }
}
