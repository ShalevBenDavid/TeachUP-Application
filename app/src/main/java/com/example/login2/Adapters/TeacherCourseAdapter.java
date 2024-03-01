package com.example.login2.Adapters;

import static com.google.firebase.database.DatabaseKt.getSnapshots;

import android.content.Context;
import android.content.Intent;
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

import java.util.ArrayList;
import java.util.List;

public class TeacherCourseAdapter extends RecyclerView.Adapter<CourseViewHolder> {
    private Context context;
    private List<CourseModel> coursesList = new ArrayList<>();


    public TeacherCourseAdapter(Context context){
        this.context = context;
    }

    public void setCourses(List<CourseModel> coursesList){
        this.coursesList = coursesList;
        notifyDataSetChanged();
    }



    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_recycle_item,parent,false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        holder.courseName.setText(coursesList.get(position).getCourseName());
        holder.courseTeacher.setText(coursesList.get(position).getTeacherName());
        holder.courseDescription.setText(coursesList.get(position).getCourseDescription());

        Glide.with(holder.itemView.getContext())
                .load(coursesList.get(position).getCourseLogoUrl())
                .placeholder(R.drawable.course_logo_placeholder)
                .into(holder.courseLogo);

        holder.itemView.setOnClickListener((v)->{
            CourseManager.getInstance().setCurrentCourse(coursesList.get(position));
            context.startActivity(new Intent(context, CourseActivity.class));
        });
    }

    @Override
    public int getItemCount() {
        if(coursesList != null) {
            return coursesList.size();
        }

        return 0;
    }

}
