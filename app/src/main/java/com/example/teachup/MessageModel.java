package com.example.teachup;

public class MessageModel {
    private String messageId;
    private String senderId;
    private String message;

    // Constructor.
    public MessageModel (String messageId, String senderId, String message) {
        this.messageId = messageId;
        this.senderId = senderId;
        this.message = message;
    }

    // Empty constructor.
    public MessageModel () {
    }

    // Getters.
    public String getMessageId () { return messageId; }
    public void setMessageId (String messageId) { this.messageId = messageId; }
    public String getSenderId () { return senderId; }

    // Setters.
    public void setSenderId (String senderId) { this.senderId = senderId; }
    public String getMessage () { return message; }
    public void setMessage (String message) { this.message = message; }
}
