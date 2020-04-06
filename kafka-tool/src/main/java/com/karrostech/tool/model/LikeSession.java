package com.karrostech.tool.model;

import java.util.Date;

public class LikeSession {
    private String sessionId;
    private Date date;

    public LikeSession(String sessionId) {
        this.sessionId = sessionId;
        this.date = new Date();
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
