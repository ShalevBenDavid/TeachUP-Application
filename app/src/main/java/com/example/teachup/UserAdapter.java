package com.example.teachup;

import android.content.Context;
import android.content.Intent;
import android.view.ContentInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {
    private Context context;
    private List<UserModel> userModelList;

    public UserAdapter (Context context) {
        this.context = context;
        this.userModelList = new ArrayList<>();
    }

    // Add a new user to the list.
    public void add (UserModel userModel) {
        userModelList.add(userModel);
    }

    // Empty the user list.
    public void clear () {
        userModelList.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.MyViewHolder holder, int position) {
        // Assign user's name/email to holder's name/email.
        UserModel userModel = userModelList.get(position);
        holder.name.setText(userModel.getUserName());
        holder.email.setText(userModel.getUserEmail());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("id", userModel.getUserID());
                intent.putExtra("name", userModel.getUserName());
                context.startActivity(intent);
            }
        });
    }

    // Return user list length.
    @Override
    public int getItemCount() {
        return userModelList.size();
    }

    // Return user list.
    public List <UserModel> getUserModelList() {
        return userModelList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView name, email;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.username);
            email = itemView.findViewById(R.id.useremail);
        }
    }
}
