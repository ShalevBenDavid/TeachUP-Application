package com.example.login2.Adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login2.R;

public class CourseViewHolder extends RecyclerView.ViewHolder {
    TextView courseName, courseTeacher, courseDescription;
    ImageView courseLogo;

    public CourseViewHolder(@NonNull View itemView) {
        super(itemView);
        courseName = itemView.findViewById(R.id.recCourseName);
        courseDescription = itemView.findViewById(R.id.recCourseDescription);
        courseTeacher = itemView.findViewById(R.id.recTeacherName);
        courseLogo = itemView.findViewById(R.id.recCourseLogo);
    }
}

