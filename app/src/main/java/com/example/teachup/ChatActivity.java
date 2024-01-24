package com.example.teachup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
    MessageAdapter messageAdapter;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        userReference = FirebaseDatabase.getInstance().getReference("users");
        receiverId = getIntent().getStringExtra("id");
        receiverName = getIntent().getStringExtra("name");

        getSupportActionBar().setTitle(receiverName);

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
                messages.sort(new Comparator<MessageModel>() {
                    @Override
                    public int compare(MessageModel m1, MessageModel m2) {
                        return Long.compare(m1.getTime(), m2.getTime());
                    }
                });

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
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = messageText.getText().toString();
                // If there is a message, send it. Otherwise, show error and grey out the button.
                if (message.trim().length() > 0) {
                    sendMessage(message);
                } else {
                    Toast.makeText(ChatActivity.this, "Message can't be empty",
                            Toast.LENGTH_SHORT).show();
                }
            }
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
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ChatActivity.this, "Failed to send message",
                                Toast.LENGTH_SHORT).show();
                    }
                });
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // User choose to logout.
        if (item.getItemId() == R.id.logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(ChatActivity.this, SigninActivity.class));
            finish();
            return true;
        }
        return false;
    }
}