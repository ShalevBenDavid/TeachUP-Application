package com.example.login2.Adapters;

import android.content.Context;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login2.Models.MessageModel;
import com.example.login2.R;
import com.example.login2.Utils.UserManager;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class MessageAdapter extends FirestoreRecyclerAdapter<MessageModel, MessageAdapter.MessageViewHolder> {
    private static final int VIEW_TYPE_SENT = 1;
    private static final int VIEW_TYPE_RECEIVED = 2;
    private static final int VIEW_TYPE_RECEIVED_GROUP = 3;

    private Context context;

    public MessageAdapter(@NonNull FirestoreRecyclerOptions<MessageModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull MessageViewHolder holder, int position, @NonNull MessageModel model) {
        if (model.getSenderId().equals(UserManager.getInstance().getUserId())) {
            holder.bindSentMessage(model);
        } else {
            holder.bindRecieveMessage(model);
        }
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == VIEW_TYPE_SENT) {
            View view = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.message_sent, parent, false);
            return new MessageViewHolder(view);
        }
        if (viewType == VIEW_TYPE_RECEIVED_GROUP) {
            View view = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.group_message_received, parent, false);
            return new MessageViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.message_received, parent, false);
            return new MessageViewHolder(view);
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position).getSenderId().equals(UserManager.getInstance().getUserId())) {
            return VIEW_TYPE_SENT;
        } else if (getItem(position).isGroupMessage()) {
            return VIEW_TYPE_RECEIVED_GROUP;
        } else {
            return VIEW_TYPE_RECEIVED;
        }
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        private final TextView userName;
        private final TextView messageSent;
        private final TextView messagedRecieved;
        private TextView messageTime;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.senderName);
            messageSent = itemView.findViewById(R.id.textViewSentMessage);
            messagedRecieved = itemView.findViewById(R.id.textViewReceivedMessage);

            messageTime = itemView.findViewById(R.id.messageTime);
        }

        public void bindRecieveMessage(MessageModel messageModel) {
            if (messageModel.isGroupMessage()) {
                userName.setText(messageModel.getSenderName());
            }

            messagedRecieved.setText(messageModel.getMessage());
            messageTime.setText(messageModel.getFormatedTime());
        }

        public void bindSentMessage(MessageModel messageModel) {
            messageSent.setText(messageModel.getMessage());
            messageTime.setText(messageModel.getFormatedTime());
        }
    }
}
