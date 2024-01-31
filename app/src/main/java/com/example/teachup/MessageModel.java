package com.example.teachup;

public class MessageModel {
    private String messageId;
    private String senderId;
    private String message;
    private long time;

    // Constructor.
    public MessageModel (String messageId, String senderId, String message, Long time) {
        this.messageId = messageId;
        this.senderId = senderId;
        this.message = message;
        this.time = time;
    }

    // Empty constructor.
    public MessageModel () {}

    // Getters.
    public String getMessageId () { return messageId; }
    public String getSenderId () { return senderId; }
    public String getMessage () { return message; }
    public long getTime () { return time; }

    // Setters.
    public void setMessageId (String messageId) { this.messageId = messageId; }
    public void setSenderId (String senderId) { this.senderId = senderId; }
    public void setMessage (String message) { this.message = message; }
    public void setTime (long time) { this.time = time; }
}
