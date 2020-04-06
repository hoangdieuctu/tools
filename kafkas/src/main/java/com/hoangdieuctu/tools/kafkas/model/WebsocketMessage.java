package com.hoangdieuctu.tools.kafkas.model;

public abstract class WebsocketMessage {
    private MessageType messageType;

    public WebsocketMessage(MessageType messageType) {
        this.messageType = messageType;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }
}
