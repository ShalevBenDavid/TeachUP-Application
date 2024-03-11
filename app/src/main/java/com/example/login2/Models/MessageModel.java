package com.example.login2.Models;


import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import lombok.Data;
import lombok.NoArgsConstructor;;

@Data
@NoArgsConstructor
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

    public String getFormattedTime () {
        Date currentDate = time.toDate();

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return sdf.format(currentDate);
    }

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