package com.example.login2.Activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.login2.Adapters.MessageAdapter;
import com.example.login2.Models.MessageModel;
import com.example.login2.R;
import com.example.login2.Repositories.ChatRepository;
import com.example.login2.Utils.CustomUtils;
import com.example.login2.Utils.UserManager;
import com.example.login2.ViewModels.ChatViewModel;
import com.example.login2.databinding.ActivityChatBinding;


public class ChatActivity extends AppCompatActivity {
    ActivityChatBinding binding;
    String receiverId, receiverName, chatId;
    MessageAdapter messageAdapter;
    ChatViewModel chatViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Defining layout and setting the content view of the activity.
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set up the toolbar.
        setSupportActionBar(binding.toolbar);

        // Remove the default app name from the toolbar.
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        // On press, navigate back to the previous activity.
        binding.backButton.setOnClickListener(view -> {
            finish();
        });

        // Initialize UI elements and Firebase references.
        receiverId = getIntent().getStringExtra("id");
        receiverName = getIntent().getStringExtra("name");
        chatId = getChatId(UserManager.getInstance().getUserId(), receiverId);

        // Set the receiver's name in the toolbar.
        binding.toolbarTitle.setText(receiverName);

        // Initialize RecyclerView and MessageAdapter.
        messageAdapter = new MessageAdapter( this);
        binding.chatRecycler.setAdapter(messageAdapter);
        binding.chatRecycler.setLayoutManager(new LinearLayoutManager(this));
        chatViewModel = new ViewModelProvider(this).get(ChatViewModel.class);
        chatViewModel.setChatId(getChatId(UserManager.getInstance().getUserId(), receiverId));
        chatViewModel.getMessages().observe(this,messages -> {
            messageAdapter.setMessages(messages);
        });



        // Clicking the send message button.
        binding.sendMessageIcon.setOnClickListener(v -> {
            String message = binding.messageEdit.getText().toString();
            // If there is a message, send it. Otherwise, show error.
            if (message.trim().length() > 0) {
                sendMessage(message);
            } else {
                Toast.makeText(ChatActivity.this, "Message can't be empty",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Monitor the text box and update the send icon accordingly.
        binding.messageEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Check if there's text in the text box and set the send icon accordingly.
                if (charSequence.length() > 0) {
                    binding.sendMessageIcon.setImageResource(R.drawable.send_icon_after);
                } else {
                    binding.sendMessageIcon.setImageResource(R.drawable.send_icon_before);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    private String getChatId(String userId, String receiverId) {
        if (userId.compareTo(receiverId) > 0) {
            return userId + receiverId;
        }
        return receiverId + userId;
    }

    private void sendMessage(String Message) {
        // Create a message model and link to Firebase user.
        MessageModel messageModel = createMessageModel(Message);

        // Send message to the chat document.
        chatViewModel.sendMessage(messageModel,
                new ChatRepository.ChatCallback() {
                    @Override
                    public void onSuccess() {
                        binding.chatRecycler.scrollToPosition(messageAdapter.getItemCount() - 1);

                        // After sending message, clear the message box.
                        binding.messageEdit.setText("");
                    }

                    @Override
                    public void onFailure(String error) {
                        CustomUtils.showToast(ChatActivity.this, error);
                    }
                });
    }

    private MessageModel createMessageModel(String message) {
        return new MessageModel(UserManager.getInstance().getUserId(),
                UserManager.getInstance().getUserName(), message,
                false);
    }

    // Create options menu in the toolbar.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    // Hide the logout menu item from the toolbar.
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem logoutItem = menu.findItem(R.id.logout);
        if (logoutItem != null) {
            logoutItem.setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }
}