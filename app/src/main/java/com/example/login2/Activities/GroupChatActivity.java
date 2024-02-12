package com.example.login2.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.login2.Adapters.MessageAdapter;
import com.example.login2.Models.CourseModel;
import com.example.login2.Models.MessageModel;
import com.example.login2.R;
import com.example.login2.Repositories.ChatRepository;
import com.example.login2.Utils.CourseManager;
import com.example.login2.Utils.CustomUtils;
import com.example.login2.Utils.UserManager;
import com.example.login2.ViewModels.ChatViewModel;
import com.example.login2.databinding.ActivityGroupChatBinding;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;


public class GroupChatActivity extends AppCompatActivity {
    private ActivityGroupChatBinding binding;
    private MessageAdapter groupMessageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Defining layout and setting the content view of the activity.
        binding = ActivityGroupChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set up the toolbar.
        setSupportActionBar(binding.toolbar);

        // Remove the default app name from the toolbar.
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        // On press, navigate back to the previous activity.
        binding.backButton.setOnClickListener(view -> {
            Intent intent = new Intent(GroupChatActivity.this, MainChatActivity.class);
            startActivity(intent);
        });

        // Initialize RecyclerView and MessageAdapter.
        FirestoreRecyclerOptions<MessageModel> options = new FirestoreRecyclerOptions.Builder<MessageModel>()
                .setQuery(ChatViewModel.getFromChat(getGroupChatId()),MessageModel.class).build();
        groupMessageAdapter = new MessageAdapter(options,this);
        binding.groupChatRecycler.setAdapter(groupMessageAdapter);
        binding.groupChatRecycler.setLayoutManager(new LinearLayoutManager(this));

        // Clicking the send message button.
        binding.sendGroupMessageIcon.setOnClickListener(v -> {
            String groupMessage = binding.groupMessageEdit.getText().toString();
            // If there is a message, send it. Otherwise, show error.
            if (groupMessage.trim().length() > 0) {
                sendGroupMessage(groupMessage);
            } else {
                Toast.makeText(GroupChatActivity.this, "Message can't be empty",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Monitor the text box and update the send icon accordingly.
        binding.groupMessageEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Check if there's text in the text box and set the send icon accordingly.
                if (charSequence.length() > 0) {
                    binding.sendGroupMessageIcon.setImageResource(R.drawable.send_icon_after);
                } else {
                    binding.sendGroupMessageIcon.setImageResource(R.drawable.send_icon_before);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    private void sendGroupMessage (String groupMessage) {
        // Create a group message model and link to Firebase user.
        MessageModel groupMessageModel = createMessageModel(groupMessage);
        String groupChatId = getGroupChatId();

        // Send group message to the group chat document.
        ChatViewModel.sendToChat(groupMessageModel,
                groupChatId,
                new ChatRepository.ChatCallback() {
            @Override
            public void onSuccess() {
                binding.groupChatRecycler.scrollToPosition(groupMessageAdapter.getItemCount() - 1);

                // After sending group message, clear the message box.
                binding.groupMessageEdit.setText("");
            }

            @Override
            public void onFailure(String error) {
                CustomUtils.showToast(GroupChatActivity.this,error);
            }
        });
    }

    private MessageModel createMessageModel(String message){
        return new MessageModel(UserManager.getInstance().getUserId(),
                UserManager.getInstance().getUserName(),message,
                true);
    }

    private String getGroupChatId(){
        CourseModel courseModel = CourseManager.getInstance().getCurrentCourse();
        return courseModel.getCourseId() + courseModel.getCourseTeacherId();
    }

    // Create options menu in the toolbar.
    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    // Hide the logout menu item from the toolbar.
    @Override
    public boolean onPrepareOptionsMenu (Menu menu) {
        MenuItem logoutItem = menu.findItem(R.id.logout);
        if (logoutItem != null) {
            logoutItem.setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (groupMessageAdapter != null) {
            groupMessageAdapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (groupMessageAdapter != null) {
            groupMessageAdapter.stopListening();
        }
    }
}