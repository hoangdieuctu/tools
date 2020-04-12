package com.hoangdieuctu.tools.klogs.controller;

import com.hoangdieuctu.tools.klogs.service.PodService;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.models.V1Pod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping({"/", "/index"})
public class IndexController {

    @Autowired
    private PodService podService;

    @GetMapping
    public String index(Model model) throws ApiException {
        List<V1Pod> pods = podService.getRunningDefaultPods();
        List<String> names = pods.stream().map(pod -> pod.getMetadata().getName()).collect(Collectors.toList());
        model.addAttribute("pods", names);
        return "index";
    }
}
