package com.example.teachup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder> {
    private static final int VIEW_TYPE_SENT = 1;
    private static final int VIEW_TYPE_RECEIVED = 2;
    private Context context;
    private List<MessageModel> messageModelList;

    public MessageAdapter (Context context) {
        this.context = context;
        this.messageModelList = new ArrayList<>();
    }

    // Add a new message to the list.
    public void add (MessageModel messageModel) {
        messageModelList.add(messageModel);
        notifyDataSetChanged();
    }

    // Empty the message list.
    public void clear () {
        messageModelList.clear();
        notifyDataSetChanged();
    }

    // Change view type according to message (sent/received).
    @NonNull
    @Override
    public MessageAdapter.MyViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == VIEW_TYPE_SENT) {
            View view = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.message_sent, parent, false);
            return new MyViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.message_received, parent, false);
            return new MyViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder (@NonNull MessageAdapter.MyViewHolder holder, int position) {
        // Assign sent/received message to holder's sent/received message.
        MessageModel messageModel = messageModelList.get(position);
        if (messageModel.getSenderId().equals((FirebaseAuth.getInstance().getUid()))) {
            holder.textViewSentMessage.setText(messageModel.getMessage());
        } else {
            holder.textViewReceivedMessage.setText(messageModel.getMessage());
        }
    }

    // Getters.
    @Override
    public int getItemCount () {
        return messageModelList.size();
    }
    public List <MessageModel> getMessageModelList () {
        return messageModelList;
    }
    public Context getContext() { return context; }

    // Returns the type of message (sent/received) at position index.
    @Override
    public int getItemViewType(int position) {
        if (messageModelList.get(position).getSenderId().equals(FirebaseAuth.getInstance().getUid())) {
            return VIEW_TYPE_SENT;
        } else {
            return VIEW_TYPE_RECEIVED;
        }
    }

    // ViewHolder class to hold the views for each item in the RecyclerView.
    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewSentMessage, textViewReceivedMessage;

        public MyViewHolder (@NonNull View itemView) {
            super(itemView);
            textViewSentMessage = itemView.findViewById(R.id.textViewSentMessage);
            textViewReceivedMessage = itemView.findViewById(R.id.textViewReceivedMessage);
        }
    }
}
