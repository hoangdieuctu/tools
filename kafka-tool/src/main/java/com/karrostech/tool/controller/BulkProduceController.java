package com.karrostech.tool.controller;

import com.karrostech.tool.model.BulkConfigSetting;
import com.karrostech.tool.model.BulkJobQueue;
import com.karrostech.tool.model.BulkProduce;
import com.karrostech.tool.service.AdminService;
import com.karrostech.tool.service.BulkProduceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/bulk-produce")
public class BulkProduceController extends KafkaController {

    @Autowired
    private BulkProduceService bulkProduceService;

    @Autowired
    private AdminService adminService;

    @GetMapping
    public String index(Model model) {
        super.setEnvsAndTopics(model);

        BulkConfigSetting bulkConfig = adminService.getBulkConfig();
        model.addAttribute("threads", bulkConfig.getThreads());
        model.addAttribute("elements", bulkConfig.getElements());

        return "bulk-produce";
    }

    @ResponseBody
    @PostMapping("/request")
    public void createBulkRequest(@RequestBody BulkProduce bulkProduce) {
        bulkProduceService.storeRequest(bulkProduce);
    }

    @ResponseBody
    @GetMapping("/request")
    public List<String> getRequestedItems() {
        return bulkProduceService.getRequestedItems();
    }

    @ResponseBody
    @GetMapping("/request/{name}")
    public BulkProduce getRequestedItem(@PathVariable("name") String name) {
        return bulkProduceService.getRequestedItem(name);
    }

    @ResponseBody
    @DeleteMapping("/request/{name}")
    public void deleteRequestedItem(@PathVariable("name") String name) {
        bulkProduceService.deleteRequestedItem(name);
    }

    @ResponseBody
    @PostMapping("/request/{name}/execute")
    public void executeRequestedItem(@PathVariable("name") String name, @RequestParam("slaves") int slaves) {
        bulkProduceService.executeRequestedItem(name, slaves);
    }

    @ResponseBody
    @GetMapping("/queue")
    public BulkJobQueue getQueue() {
        return bulkProduceService.getQueue();
    }

    @ResponseBody
    @DeleteMapping("/queue/running")
    public void cancelRunningTasks() {
        bulkProduceService.cancelRunningTasks();
    }
}
