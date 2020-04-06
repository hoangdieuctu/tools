package com.hoangdieuctu.tools.kafkas.controller;

import com.hoangdieuctu.tools.kafkas.constant.Constants;
import com.hoangdieuctu.tools.kafkas.model.*;
import com.hoangdieuctu.tools.kafkas.service.ProduceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/produce")
public class ProduceController extends KafkaController {

    @Autowired
    private ProduceService produceService;

    @GetMapping
    public String index(Model model) {
        super.setEnvsAndTopics(model);

        List<String> folders = produceService.getStorageFolders();
        model.addAttribute("folders", folders);

        return "produce";
    }

    @ResponseBody
    @GetMapping("/topics")
    public List<String> getTopic(@RequestParam("env") String env,
                                 @RequestParam(value = "isFavOnly", required = false, defaultValue = "false") boolean isFavOnly) {
        return super.getTopics(env, isFavOnly);
    }

    @ResponseBody
    @GetMapping("/topic/details")
    public List<PartitionData> getTopicDetail(@RequestParam("env") String env, @RequestParam("topicName") String topicName) {
        return produceService.getTopicDetails(env, topicName);
    }

    @PostMapping
    @ResponseBody
    public KafkaRecordData sendKafkaMsg(@RequestParam("env") String env,
                                        @RequestParam("topic") String topic,
                                        @RequestParam(value = "partition", required = false) Integer partition,
                                        @RequestBody String message,
                                        HttpServletRequest request) {
        String host = request.getRemoteHost();
        return produceService.sendKafkaMsg(host, env, topic, partition < 0 ? null : partition, message);
    }

    @ResponseBody
    @PostMapping("/json")
    public void saveProduceFile(@RequestBody JsonFile jsonFile) {
        produceService.saveProduceFile(jsonFile);
    }

    @ResponseBody
    @GetMapping("/json")
    public List<JsonFile> listProduceFiles(@RequestParam(value = "folder", required = false, defaultValue = Constants.ROOT_STORAGE) String folder) {
        return produceService.listProduceFiles(folder);
    }

    @ResponseBody
    @GetMapping("/json/{folder}/{fileName:.+}")
    public JsonFile getJsonFile(@PathVariable("folder") String folder,
                                @PathVariable("fileName") String fileName) {
        return produceService.getJsonFile(folder, fileName);
    }

    @ResponseBody
    @DeleteMapping("/json/{folder}/{fileName:.+}")
    public void deleteJsonFile(@PathVariable("folder") String folder,
                               @PathVariable("fileName") String fileName) {
        produceService.deleteJsonFile(folder, fileName);
    }

    @ResponseBody
    @GetMapping("/histories")
    public List<ProduceHistory> getProduceHistories(@RequestParam(value = "from", required = false, defaultValue = "0") int from,
                                                    @RequestParam(value = "size", required = false, defaultValue = "50") int size) {
        return produceService.getHistories(from, size);
    }

    @ResponseBody
    @GetMapping("/histories/{id}")
    public ProduceHistory getProduceHistory(@PathVariable("id") String id) {
        return produceService.getProduceHistory(id);
    }

}
