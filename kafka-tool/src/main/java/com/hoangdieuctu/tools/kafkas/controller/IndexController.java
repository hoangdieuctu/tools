package com.hoangdieuctu.tools.kafkas.controller;

import com.hoangdieuctu.tools.kafkas.model.LikeData;
import com.hoangdieuctu.tools.kafkas.service.LikeService;
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

    @GetMapping
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
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
