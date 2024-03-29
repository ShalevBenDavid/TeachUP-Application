package com.example.login2.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login2.Activities.ChatActivity;
import com.example.login2.Activities.ProfileActivity;
import com.example.login2.Models.UserModel;
import com.example.login2.R;

import java.util.ArrayList;
import java.util.List;

public class StudentListAdapter extends RecyclerView.Adapter<StudentListAdapter.StudentListViewHolder> {

    private List<UserModel> students = new ArrayList<>();
    private final Context context;


    public StudentListAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public StudentListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_user_card,parent,false);
        return new StudentListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentListAdapter.StudentListViewHolder holder, int position) {
        UserModel student = students.get(position);
        holder.userEmail.setText(student.getUserEmail());
        holder.userName.setText(student.getUserName());

        // Upon clicking on the profile icon, open profile page.
        holder.profileIcon.setOnClickListener(v ->{
            Intent intent = new Intent(context, ProfileActivity.class);
            intent.putExtra("userModel", student);
            context.startActivity(intent);
        });

        // Upon clicking anywhere else on the card, open chat page.
        holder.itemView.setOnClickListener(v ->{
            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra("name",student.getUserName());
            intent.putExtra("id",student.getUserId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        if(students == null){
            return 0;
        }

        return students.size();
    }

    public void setStudents(List<UserModel> students){
        this.students = students;
        notifyDataSetChanged();
    }

    public static class StudentListViewHolder extends RecyclerView.ViewHolder{
        TextView userEmail,userName;
        ImageView profileIcon;
        public StudentListViewHolder(@NonNull View itemView) {
            super(itemView);
            userEmail = itemView.findViewById(R.id.useremail);
            userName = itemView.findViewById(R.id.username);
            profileIcon = itemView.findViewById(R.id.profile);
        }
    }
}
