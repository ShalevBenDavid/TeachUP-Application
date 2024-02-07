package com.example.login2.Repositories;

import com.example.login2.Models.MessageModel;
import com.example.login2.Utils.Constants;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ChatRepository {
    private final FirebaseFirestore db;

    public ChatRepository() {
        db = FirebaseFirestore.getInstance();
    }

    public Query getChatMessages(String chatId){
        return db.collection(Constants.GROUP_CHAT_REPOSITORY)
                .document(chatId)
                .collection("messages")
                .orderBy("time", Query.Direction.ASCENDING);
    }

    public void sendMessage(MessageModel messageModel,String groupChatId, groupChatCallback callback){

        DocumentReference documentReference = db.collection(Constants.GROUP_CHAT_REPOSITORY)
                .document(groupChatId).collection("messages").document();

        messageModel.setMessageId(documentReference.getId());

        documentReference.set(messageModel).addOnSuccessListener(aVoid -> callback.onSuccess())
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    public interface groupChatCallback{
        void onSuccess();
        void onFailure(String message);
    }
}
