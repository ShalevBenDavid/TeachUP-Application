package com.example.login2.Models;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.google.firebase.Timestamp;


public class MessageModel {
    private String messageId;
    private String senderId;
    private String senderName;
    private String message;
    private Timestamp time;



    private boolean isGroupMessage;
    // Constructor.
    public MessageModel (String senderId, String senderName, String message, boolean isGroupMessage) {
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
    public Timestamp getTime(){
        return time;
    }
    public String getFormatedTime () {
        Date currentDate = time.toDate();

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return sdf.format(currentDate);
    }

    // Setters.
    public void setMessageId (String messageId) { this.messageId = messageId; }
    public void setSenderId (String senderId) { this.senderId = senderId; }
    public void setSenderName (String senderName) { this.senderName = senderName; }
    public void setMessage (String message) { this.message = message; }
    private void setTime () {
        this.time = Timestamp.now();
    }

    public boolean isGroupMessage() {
        return isGroupMessage;
    }

    public void setGroupMessage(boolean groupMessage) {
        isGroupMessage = groupMessage;
    }
}