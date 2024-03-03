package com.example.login2.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login2.Models.MessageModel;
import com.example.login2.R;
import com.example.login2.Utils.UserManager;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder>{
    private static final int VIEW_TYPE_SENT = 1;
    private static final int VIEW_TYPE_RECEIVED = 2;
    private static final int VIEW_TYPE_RECEIVED_GROUP = 3;
    private Context context;
    private List<MessageModel> messages = new ArrayList<>();

    public MessageAdapter(Context context) {
        this.context = context;
    }

    public void setMessages(List<MessageModel> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        MessageModel message = messages.get(position);
        if (message.getSenderId().equals(UserManager.getInstance().getUserId())) {
            holder.bindSentMessage(message);
        } else {
            holder.bindReceivedMessage(message);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (messages.get(position).getSenderId().equals(UserManager.getInstance().getUserId())) {
            return VIEW_TYPE_SENT;
        } else if (messages.get(position).isGroupMessage()) {
            return VIEW_TYPE_RECEIVED_GROUP;
        } else {
            return VIEW_TYPE_RECEIVED;
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }


    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        private final TextView userName;
        private final TextView messageSent;
        private final TextView messagedReceived;
        private TextView messageTime;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.senderName);
            messageSent = itemView.findViewById(R.id.textViewSentMessage);
            messagedReceived = itemView.findViewById(R.id.textViewReceivedMessage);

            messageTime = itemView.findViewById(R.id.messageTime);
        }

        public void bindReceivedMessage(MessageModel messageModel) {
            if (messageModel.isGroupMessage()) {
                userName.setText(messageModel.getSenderName());
            }

            messagedReceived.setText(messageModel.getMessage());
            messageTime.setText(messageModel.getFormattedTime());
        }

        public void bindSentMessage(MessageModel messageModel) {
            messageSent.setText(messageModel.getMessage());
            messageTime.setText(messageModel.getFormattedTime());
        }
    }
}