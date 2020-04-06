package com.karrostech.tool.model;

public class CustomMessage extends WebsocketMessage {
    private String sessionId;
    private String message;

    public CustomMessage() {
        super(MessageType.CUSTOM);
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
