package com.example.login2.Repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.login2.Models.MessageModel;
import com.example.login2.Utils.Constants;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class GroupChatRepository {
    private final FirebaseFirestore db;

    public GroupChatRepository() {
        db = FirebaseFirestore.getInstance();
    }

    public Query getGroupChatMessages(String chatId){
        Log.e("chatId",chatId);
        return db.collection(Constants.GROUP_CHAT_REPOSITORY)
                .document(chatId)
                .collection("messages")
                .orderBy("time", Query.Direction.ASCENDING);
    }



    public void sendMessage(MessageModel messageModel,String groupChatId, groupChatCallback callback){

        DocumentReference documentReference = db.collection(Constants.GROUP_CHAT_REPOSITORY)
                .document(groupChatId).collection("messages").document();

        messageModel.setMessageId(documentReference.getId());

        documentReference.set(messageModel).addOnSuccessListener(aVoid ->{
            callback.onSuccess();
        }).addOnFailureListener(e ->{
            callback.onFailure(e.getMessage());
        });
    }


/*    public LiveData<List<MessageModel>> getChatMessages(String courseId) {
        MutableLiveData<List<MessageModel>> messages = new MutableLiveData<>();
        List<MessageModel> helperList = new ArrayList<>();

        Query groupChatQuery = db.collection(Constants.GROUP_CHAT_REPOSITORY)
                .whereEqualTo("courseId", courseId);


        groupChatQuery.get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<DocumentSnapshot> chatDocument = queryDocumentSnapshots.getDocuments();
            if (chatDocument.isEmpty()) {
                messages.postValue(new ArrayList<>(helperList));
                return;
            }

            Query messagesQuery = db.collection(Constants.GROUP_CHAT_REPOSITORY)
                    .document(chatDocument.get(0).getId())
                    .collection("messages")
                    .orderBy("time", Query.Direction.DESCENDING);


            messagesQuery.get().addOnSuccessListener(messageQuerySnapshot ->{
                List<DocumentSnapshot> messagesDocuments = messageQuerySnapshot.getDocuments();
                if(messagesDocuments.isEmpty()){
                    messages.postValue(new ArrayList<>(helperList));
                    return;
                }

                for(DocumentSnapshot messageDoc : messagesDocuments){
                    helperList.add(messageDoc.toObject(MessageModel.class));
                    messages.postValue(new ArrayList<>(helperList));
                }
            });
        });

        return messages;
    }*/

    public interface groupChatCallback{
        void onSuccess();
        void onFailure(String message);
    }
}
