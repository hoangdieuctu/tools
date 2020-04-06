package com.karrostech.tool.service;

import com.karrostech.tool.model.Host;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
public class SlaveHolder {

    private static final int INACTIVE_DURATION_IN_SECOND = 1;

    private final Set<Host> slaves = new HashSet<>();

    @Scheduled(fixedRate = 1000 * 30)
    public void clean() {
        Date current = new Date();

        List<Host> willBeRemoved = new ArrayList<>();
        slaves.forEach(s -> {
            Date lastSync = s.getLastSync();
            if (current.getTime() - lastSync.getTime() > INACTIVE_DURATION_IN_SECOND * 60 * 1000) {
                log.info("Found inactive slave: {}", s.getName());
                willBeRemoved.add(s);
            }
        });

        slaves.removeAll(willBeRemoved);
    }

    public void register(String host) {
        Host newHost = new Host(host);
        slaves.remove(newHost);
        slaves.add(newHost);
    }

    public void unregister(String host) {
        slaves.remove(new Host(host));
    }

    public Set<Host> getSlaves() {
        return slaves;
    }
}
