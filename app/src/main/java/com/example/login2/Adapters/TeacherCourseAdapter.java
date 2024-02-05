package com.example.login2.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.login2.Activities.CourseActivity;
import com.example.login2.Models.CourseModel;
import com.example.login2.R;
import com.example.login2.Utils.CourseManager;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class TeacherCourseAdapter extends FirestoreRecyclerAdapter<CourseModel, CourseViewHolder> {
    private Context context;
    public TeacherCourseAdapter(@NonNull FirestoreRecyclerOptions<CourseModel> options,
                                Context context) {
        super(options);
        this.context= context;
    }


    @Override
    protected void onBindViewHolder(@NonNull CourseViewHolder holder, int position, @NonNull CourseModel model) {
        holder.courseName.setText(model.getCourseName());
        holder.courseTeacher.setText(model.getTeacherName());
        holder.courseDescription.setText(model.getCourseDescription());

        Glide.with(holder.itemView.getContext())
                .load(model.getCourseLogoUrl())
                .placeholder(R.drawable.course_logo_placeholder)
                .into(holder.courseLogo);

        holder.itemView.setOnClickListener((v)->{
            CourseManager.getInstance().setCurrentCourse(getSnapshots().getSnapshot(position).toObject(CourseModel.class));
            context.startActivity(new Intent(context, CourseActivity.class));
        });
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_recycle_item,parent,false);
        return new CourseViewHolder(view);
    }

}
