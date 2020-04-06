package com.karrostech.tool.model;

import java.util.List;

public class BulkJobQueue {

    private List<BulkProduce> running;
    private List<BulkProduce> scheduled;

    public List<BulkProduce> getRunning() {
        return running;
    }

    public void setRunning(List<BulkProduce> running) {
        this.running = running;
    }

    public List<BulkProduce> getScheduled() {
        return scheduled;
    }

    public void setScheduled(List<BulkProduce> scheduled) {
        this.scheduled = scheduled;
    }
}
