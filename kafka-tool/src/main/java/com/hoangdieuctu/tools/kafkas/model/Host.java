package com.hoangdieuctu.tools.kafkas.model;

import java.util.Date;
import java.util.Objects;

public class Host {

    private String name;
    private Date lastSync;

    public Host(String name) {
        this.name = name;
        this.lastSync = new Date();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getLastSync() {
        return lastSync;
    }

    public void setLastSync(Date lastSync) {
        this.lastSync = lastSync;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Host host = (Host) o;
        return Objects.equals(name, host.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
