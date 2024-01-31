package com.example.teachup;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class ChatActivity extends AppCompatActivity {
    FirebaseFirestore db;
    CollectionReference senderReference, receiverReference;
    String receiverId, receiverName, senderRoom, receiverRoom;
    FirebaseAuth auth;
    ImageView sendBtn;
    EditText messageText;
    RecyclerView recyclerView;
    ImageView backButton;
    Toolbar toolbar;
    TextView toolbarTitle;
    MessageAdapter messageAdapter;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Remove the default app name from the toolbar.
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        // On press, navigate back to the previous activity.
        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(view -> onBackPressed());

        // Initialize UI elements and Firebase references.
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        receiverId = getIntent().getStringExtra("id");
        receiverName = getIntent().getStringExtra("name");

        // Set the receiver's name in the toolbar.
        toolbarTitle = findViewById(R.id.toolbarTitle);
        toolbarTitle.setText(receiverName);

        // Define a different chat room ID for sender/receiver.
        if (receiverId != null) {
            String currentUserID = Objects.requireNonNull(auth.getCurrentUser()).getUid();
            senderRoom = currentUserID + receiverId;
            receiverRoom = receiverId + currentUserID;;
        }

        // Link send button and message text by id.
        sendBtn = findViewById(R.id.sendMessageIcon);
        messageText = findViewById(R.id.messageEdit);

        // Initialize RecyclerView and MessageAdapter.
        messageAdapter = new MessageAdapter(this);
        recyclerView = findViewById(R.id.chatRecycler);
        recyclerView.setAdapter(messageAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Firestore references for sender/receiver rooms.
        senderReference = db.collection("chats").document(senderRoom).
                collection("messages");
        receiverReference = db.collection("chats").document(receiverRoom).
                collection("messages");


        // Listen for changes in sender's room and update messages.
        senderReference.addSnapshotListener((queryDocumentSnapshots, item) -> {
            // Error while fetching data from "messages"" collection.
            if (item != null) {
                Toast.makeText(ChatActivity.this, "Error fetching messages: " +
                        item.getMessage(), Toast.LENGTH_SHORT).show();
                return;
            }
            // Check for changes in the "messages" collection.
            if (queryDocumentSnapshots != null) {
                List<MessageModel> messages = new ArrayList<>();
                // Process document changes.
                for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()) {
                    // Convert Firestore document to messageModel object.
                    MessageModel addedMessage = documentChange.getDocument().toObject(MessageModel.class);
                    // Add up all the messages.
                    messages.add(addedMessage);
                }

                // Sort messages based on time.
                messages.sort(Comparator.comparingLong(MessageModel::getTime));

                // Update the messageAdapter.
                for (MessageModel message : messages) {
                    messageAdapter.add(message);
                }

                // Update the UI (view)
                messageAdapter.notifyDataSetChanged();
            }
        });

        // Clicking the send button.
        sendBtn.setOnClickListener(v -> {
            String message = messageText.getText().toString();
            // If there is a message, send it. Otherwise, show error.
            if (message.trim().length() > 0) {
                sendMessage(message);
            } else {
                Toast.makeText(ChatActivity.this, "Message can't be empty",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Monitor the text box and update the send icon accordingly.
        messageText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Check if there's text in the text box and set the send icon accordingly.
                if (charSequence.length() > 0) {
                    sendBtn.setImageResource(R.drawable.send_icon_after);
                } else {
                    sendBtn.setImageResource(R.drawable.send_icon_before);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    private void sendMessage(String message) {
        // Give the message a random id.
        String messageId = UUID.randomUUID().toString();
        // Create a message model for message and link to firebase user.
        MessageModel messageModel = new MessageModel
                (messageId, FirebaseAuth.getInstance().getUid(), message, System.currentTimeMillis());

        // Send the message to the sender's room in Firestore.
        senderReference.document(messageId).set(messageModel)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // If sending succeeded, clear the message box.
                        messageText.setText("");
                    } else {
                        // If sending failed,show an error message.
                        Toast.makeText(ChatActivity.this,
                                "Failed to send message", Toast.LENGTH_SHORT).show();
                    }
                });
        // Send the message to the receiver's room in Firestore.
        receiverReference.document(messageId).set(messageModel)
                .addOnFailureListener(e -> Toast.makeText(ChatActivity.this,
                        "Failed to send message", Toast.LENGTH_SHORT).show());

        // Scroll to the last message sent.
        recyclerView.scrollToPosition(messageAdapter.getItemCount() - 1);
    }

    // Create options menu in the toolbar
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