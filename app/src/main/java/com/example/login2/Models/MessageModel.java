package com.example.login2.Models;

import com.google.type.DateTime;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class MessageModel {
    private String messageId;
    private String senderId;
    private String senderName;
    private String message;
    private LocalDateTime time;
    private boolean isGroupMessage;

    // Constructor.
    public MessageModel (String messageId, String senderId, String senderName, String message, boolean isGroupMessage) {
        this.messageId = messageId;
        this.senderId = senderId;
        this.senderName = senderName;
        this.message = message;
        this.isGroupMessage = isGroupMessage;
        setTime();
    }

    // Empty constructor.
    public MessageModel () {}

    // Getters.
    public String getMessageId () { return messageId; }
    public String getSenderId () { return senderId; }
    public String getSenderName () { return senderName; }
    public String getMessage () { return message; }
    public String getTime () {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return time.format(formatter);
    }
    public boolean isGroupMessage () { return isGroupMessage; }

    // Setters.
    public void setMessageId (String messageId) { this.messageId = messageId; }
    public void setSenderId (String senderId) { this.senderId = senderId; }
    public void setSenderName (String senderName) { this.senderName = senderName; }
    public void setMessage (String message) { this.message = message; }
    private void setTime () {
        this.time = LocalDateTime.now();
    }
    public void setGroupMessage (boolean groupMessage) { isGroupMessage = groupMessage; }
}