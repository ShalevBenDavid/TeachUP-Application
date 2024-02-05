package com.example.login2.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.login2.Activities.CourseActivity;
import com.example.login2.Models.CourseModel;
import com.example.login2.R;
import com.example.login2.Utils.CourseManager;

import java.util.List;

public class StudentCourseAdapter extends RecyclerView.Adapter<CourseViewHolder> {
    Context context;
    List<CourseModel> courses;


    public StudentCourseAdapter(Context context, List<CourseModel> courses) {
        this.context = context;
        this.courses = courses;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_recycle_item, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        CourseModel course = courses.get(position);
        holder.courseName.setText(courses.get(position).getCourseName());
        holder.courseTeacher.setText(courses.get(position).getTeacherName());
        holder.courseDescription.setText(courses.get(position).getCourseDescription());

        Glide.with(holder.itemView.getContext())
                .load(courses.get(position).getCourseLogoUrl())
                .placeholder(R.drawable.course_logo_placeholder)
                .into(holder.courseLogo);

        holder.itemView.setOnClickListener((v) -> {
            CourseManager.getInstance().setCurrentCourse(courses.get(position));
            context.startActivity(new Intent(context, CourseActivity.class));
        });
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    public void addCourse(CourseModel courseModel) {
        courses.add(courseModel);
        new Handler().postDelayed(() -> notifyItemInserted(courses.size() - 1), 500);
    }
}
