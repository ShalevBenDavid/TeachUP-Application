package com.example.login2.Repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.login2.Models.MessageModel;
import com.example.login2.Utils.Constants;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class ChatRepository {
    private MutableLiveData<List<MessageModel>> messagesLiveData = new MutableLiveData<List<MessageModel>>();
    private List<MessageModel> messageList = new ArrayList<>();

    public ChatRepository() {}

    public LiveData<List<MessageModel>> getMessagesLiveData(String chatId){
        listenForDataChanges(chatId);
        return messagesLiveData;
    }

    private void listenForDataChanges(String chatId){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(Constants.CHAT_REPOSITORY)
                .document(chatId)
                .collection("messages")
                .orderBy("time", Query.Direction.ASCENDING)
                .addSnapshotListener((snapshot, e) ->{
                    if(e != null){
                        return;
                    }

                    for(DocumentChange documentChange : snapshot.getDocumentChanges()){
                        MessageModel newMessage = documentChange.getDocument().toObject(MessageModel.class);
                        messageList.add(newMessage);
                    }

                    messagesLiveData.postValue(messageList);
                });
    }
    public void sendMessage(MessageModel messageModel, String ChatId, ChatCallback callback) {
        DocumentReference documentReference = FirebaseFirestore.getInstance().collection(Constants.CHAT_REPOSITORY)
                .document(ChatId).collection("messages").document();

        messageModel.setMessageId(documentReference.getId());

        documentReference.set(messageModel).addOnSuccessListener(aVoid -> callback.onSuccess())
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    public interface ChatCallback {
        void onSuccess();
        void onFailure(String message);
    }
}
