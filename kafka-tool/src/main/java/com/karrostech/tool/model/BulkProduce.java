package com.karrostech.tool.model;

import java.util.List;
import java.util.Objects;

public class BulkProduce {
    private Environment env;
    private String topic;
    private int threadCount;
    private int elementCount;
    private List<String> body;
    private List<String> keys;

    private Long sleep; // in milliseconds
    private String name;

    private int loopCount;
    private int loopSleep; // in seconds

    private int percent = 0; // this only for report, don't add to hashcode and equal functions
    private boolean stopped = false; // this only for force stop job, don't add to hashcode and equal methods
    private int slaves; // this only for distribute job to slaves, don't add to hashcode and equal functions

    public Environment getEnv() {
        return env;
    }

    public void setEnv(Environment env) {
        this.env = env;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public int getThreadCount() {
        return threadCount;
    }

    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
    }

    public int getElementCount() {
        return elementCount;
    }

    public void setElementCount(int elementCount) {
        this.elementCount = elementCount;
    }

    public List<String> getBody() {
        return body;
    }

    public void setBody(List<String> body) {
        this.body = body;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSleep() {
        return sleep == null ? 0 : sleep;
    }

    public void setSleep(Long sleep) {
        this.sleep = sleep;
    }

    public int getLoopCount() {
        return loopCount;
    }

    public void setLoopCount(int loopCount) {
        this.loopCount = loopCount;
    }

    public int getLoopSleep() {
        return loopSleep;
    }

    public void setLoopSleep(int loopSleep) {
        this.loopSleep = loopSleep;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    public boolean isStopped() {
        return stopped;
    }

    public void setStopped(boolean stopped) {
        this.stopped = stopped;
    }

    public void forceStop() {
        this.stopped = true;
    }

    public List<String> getKeys() {
        return keys;
    }

    public void setKeys(List<String> keys) {
        this.keys = keys;
    }

    public int getSlaves() {
        return slaves;
    }

    public void setSlaves(int slaves) {
        this.slaves = slaves;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BulkProduce that = (BulkProduce) o;
        return threadCount == that.threadCount &&
                elementCount == that.elementCount &&
                loopCount == that.loopCount &&
                loopSleep == that.loopSleep &&
                env == that.env &&
                Objects.equals(topic, that.topic) &&
                Objects.equals(body, that.body) &&
                Objects.equals(keys, that.keys) &&
                Objects.equals(sleep, that.sleep) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(env, topic, threadCount, elementCount, body, keys, sleep, name, loopCount, loopSleep);
    }
}
