package com.example.teachup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class ChatActivity extends AppCompatActivity {
    String receiverId, receiverName, senderRoom, receiverRoom;
    DatabaseReference senderReference, receiverReference, userReference;
    ImageView sendBtn;
    EditText messageText;
    RecyclerView recyclerView;
    ImageView backButton;
    TextView toolbarTitle;
    MessageAdapter messageAdapter;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Remove the default app name from the toolbar.
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        // On press, navigate back to the previous activity.
        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        userReference = FirebaseDatabase.getInstance().getReference("users");
        receiverId = getIntent().getStringExtra("id");
        receiverName = getIntent().getStringExtra("name");

        toolbarTitle = findViewById(R.id.toolbarTitle);
        toolbarTitle.setText(receiverName);

        if (receiverId != null) {
            senderRoom = FirebaseAuth.getInstance().getUid() + receiverId;
            receiverRoom = receiverId + FirebaseAuth.getInstance().getUid();;
        }

        // Link send button and message text by id.
        sendBtn = findViewById(R.id.sendMessageIcon);
        messageText = findViewById(R.id.messageEdit);

        messageAdapter = new MessageAdapter(this);
        recyclerView = findViewById(R.id.chatRecycler);

        recyclerView.setAdapter(messageAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        senderReference = FirebaseDatabase.getInstance().getReference("chats").child(senderRoom);
        receiverReference = FirebaseDatabase.getInstance().getReference("chats").child(receiverRoom);

        senderReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<MessageModel> messages = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    MessageModel messageModel = dataSnapshot.getValue(MessageModel.class);
                    messages.add(messageModel);
                }

                // Sort messages based on time.
                messages.sort(Comparator.comparingLong(MessageModel::getTime));

                messageAdapter.clear();
                for (MessageModel message : messages) {
                    messageAdapter.add(message);
                }
                messageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // Clicking the send button.
        sendBtn.setOnClickListener(v -> {
            String message = messageText.getText().toString();
            // If there is a message, send it. Otherwise, show error and grey out the button.
            if (message.trim().length() > 0) {
                sendMessage(message);
            } else {
                Toast.makeText(ChatActivity.this, "Message can't be empty",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Add a TextWatcher to the EditText
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
        // Add message model to messages list.
        messageAdapter.add(messageModel);

        senderReference.child(messageId).setValue(messageModel)
                .addOnSuccessListener(unused -> {

                })
                .addOnFailureListener(e -> Toast.makeText(ChatActivity.this, "Failed to send message",
                        Toast.LENGTH_SHORT).show());
        receiverReference.child(messageId).setValue(messageModel);
        // Scroll view to the last message sent.
        recyclerView.scrollToPosition(messageAdapter.getItemCount() - 1);
        // After sending message, set message box to blank.
        messageText.setText("");
    }

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