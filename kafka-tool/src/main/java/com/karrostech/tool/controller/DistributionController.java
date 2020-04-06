package com.karrostech.tool.controller;

import com.karrostech.tool.model.Host;
import com.karrostech.tool.service.DistributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Set;

@Controller
@RequestMapping("/distribute")
public class DistributionController extends KafkaController {

    @Autowired
    private DistributeService distributeService;

    @ResponseBody
    @GetMapping("/register")
    public void register(@RequestParam("host") String host) {
        distributeService.registerSlave(host);
    }

    @ResponseBody
    @GetMapping("/unregister")
    public void unregister(@RequestParam("host") String host) {
        distributeService.unregisterSlave(host);
    }

    @ResponseBody
    @GetMapping("/slaves")
    public Set<Host> getSlaves() {
        return distributeService.getSlaves();
    }
}
