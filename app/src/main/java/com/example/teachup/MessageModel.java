package com.example.teachup;

public class MessageModel {
    private String messageId;
    private String senderId;
    private String senderName;
    private String message;
    private long time;
    private boolean isGroupMessage;

    // Constructor.
    public MessageModel (String messageId, String senderId, String senderName, String message,
                         Long time, boolean isGroupMessage) {
        this.messageId = messageId;
        this.senderId = senderId;
        this.senderName = senderName;
        this.message = message;
        this.time = time;
        this.isGroupMessage = isGroupMessage;
    }

    // Empty constructor.
    public MessageModel () {}

    // Getters.
    public String getMessageId () { return messageId; }
    public String getSenderId () { return senderId; }
    public String getSenderName () { return senderName; }
    public String getMessage () { return message; }
    public long getTime () { return time; }
    public boolean isGroupMessage () { return isGroupMessage; }

    // Setters.
    public void setMessageId (String messageId) { this.messageId = messageId; }
    public void setSenderId (String senderId) { this.senderId = senderId; }
    public void setSenderName (String senderName) { this.senderName = senderName; }
    public void setMessage (String message) { this.message = message; }
    public void setTime (long time) { this.time = time; }
    public void setGroupMessage (boolean groupMessage) { isGroupMessage = groupMessage; }
}
