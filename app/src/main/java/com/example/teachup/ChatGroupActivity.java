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

public class ChatGroupActivity extends AppCompatActivity {
    FirebaseFirestore db;
    CollectionReference groupChatReference;
    ImageView sendBtn;
    EditText MessageText;
    RecyclerView RecyclerView;
    ImageView backButton;
    Toolbar toolbar;
    TextView toolbarTitle;
    MessageAdapter groupMessageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Remove the default app name from the toolbar.
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        // On press, navigate back to the previous activity.
        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(view -> onBackPressed());

        // Initialize UI elements and Firestore reference for group chat
        db = FirebaseFirestore.getInstance();
        groupChatReference = db.collection("groupChats");

        // Set the toolbar title to "Group Chat"
        toolbarTitle = findViewById(R.id.toolbarTitle);
        toolbarTitle.setText("Group Chat");

        // Link send button and message text by id.
        sendBtn = findViewById(R.id.sendGroupMessageIcon);
        MessageText = findViewById(R.id.groupMessageEdit);

        groupMessageAdapter = new MessageAdapter(this);
        RecyclerView = findViewById(R.id.groupChatRecycler);
        RecyclerView.setAdapter(groupMessageAdapter);
        RecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Listen for changes in the group chat and update messages
        groupChatReference.addSnapshotListener((queryDocumentSnapshots, e) -> {
            // Error while fetching data from "groupChats" collection.
            if (e != null) {
                Toast.makeText(ChatGroupActivity.this, "Error fetching group messages",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            // Check for changes in the "groupChats" collection.
            if (queryDocumentSnapshots != null) {
                List<MessageModel> groupMessages = new ArrayList<>();
                // Process document changes.
                for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()) {
                    // Convert Firestore document to messageModel object.
                    MessageModel groupMessageModel = documentChange.getDocument().toObject(MessageModel.class);
                    // Add up all the messages.
                    groupMessages.add(groupMessageModel);
                }

                // Sort messages based on time.
                groupMessages.sort(Comparator.comparingLong(MessageModel::getTime));

                // Update the messageAdapter.
                for (MessageModel message : groupMessages) {
                    groupMessageAdapter.add(message);
                }

                // Update the UI (view)
                groupMessageAdapter.notifyDataSetChanged();
            }
        });

        // Clicking the send group message button
        sendBtn.setOnClickListener(v -> {
            String groupMessage = MessageText.getText().toString();
            // If there is a message, send it. Otherwise, show error and grey out the button.
            if (groupMessage.trim().length() > 0) {
                sendGroupMessage(groupMessage);
            } else {
                Toast.makeText(ChatGroupActivity.this, "Message can't be empty",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Monitor the group text box and update the send icon accordingly
        MessageText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Check if there's text in the group text box and set the send icon accordingly
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

    private void sendGroupMessage(String groupMessage) {
        // Give the message a random id.
        String groupId = groupChatReference.document().getId();

        // Create a group message model and link to Firebase user.
        MessageModel groupMessageModel = new MessageModel (groupId,
                FirebaseAuth.getInstance().getUid(), groupMessage, System.currentTimeMillis());

        // Send group message to the group chat document.
        groupChatReference.document(groupId).set(groupMessageModel)
                .addOnSuccessListener(unused -> {})
                .addOnFailureListener(e -> Toast.makeText(ChatGroupActivity.this,
                        "Failed to send group message", Toast.LENGTH_SHORT).show());

        // Scroll to the last group message sent
        RecyclerView.scrollToPosition(groupMessageAdapter.getItemCount() - 1);

        // After sending group message, clear the group message box
        MessageText.setText("");
    }

    // Create options menu in the toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    // Hide the logout menu item from the toolbar
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem logoutItem = menu.findItem(R.id.logout);
        if (logoutItem != null) {
            logoutItem.setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }
}
