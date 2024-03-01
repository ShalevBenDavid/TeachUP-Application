package com.example.login2.ViewModels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.login2.Models.MessageModel;
import com.example.login2.Repositories.ChatRepository;
import com.google.firebase.firestore.Query;

import java.util.List;

public class ChatViewModel extends ViewModel {
    private final ChatRepository repository;
    private String chatId;

    public ChatViewModel() {
        repository = new ChatRepository();
    }

    public LiveData<List<MessageModel>> getMessages() {
        return repository.getMessagesLiveData(chatId);
    }

    public void sendMessage(MessageModel message, ChatRepository.ChatCallback callback) {
        repository.sendMessage(message, chatId, callback);
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }
}



