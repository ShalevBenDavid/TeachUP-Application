package com.example.login2.ViewModels;

import androidx.lifecycle.ViewModel;

import com.example.login2.Models.MessageModel;
import com.example.login2.Repositories.ChatRepository;
import com.google.firebase.firestore.Query;

public class ChatViewModel extends ViewModel {
    private static final ChatRepository repository = new ChatRepository();

    public static Query getFromChat (String chatId) {
        return repository.getChatMessages(chatId);
    }

    public static void sendToChat (MessageModel messageModel, String ChatId, ChatRepository.ChatCallback callback) {
        repository.sendMessage(messageModel, ChatId, callback);
    }
}
