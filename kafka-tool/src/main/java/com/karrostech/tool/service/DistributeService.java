package com.karrostech.tool.service;

import com.karrostech.tool.model.Host;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Slf4j
@Component
public class DistributeService {

    @Autowired
    private SlaveHolder slaveHolder;

    public void registerSlave(String host) {
        log.info("Slave registered: host={}", host);
        slaveHolder.register(host);
    }

    public void unregisterSlave(String host) {
        log.info("Slave unregistered: host={}", host);
        slaveHolder.unregister(host);
    }

    public Set<Host> getSlaves() {
        return slaveHolder.getSlaves();
    }
}
