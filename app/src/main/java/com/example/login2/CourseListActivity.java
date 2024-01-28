package com.example.login2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.login2.Adapters.StudentCourseAdapter;
import com.example.login2.Adapters.TeacherCourseAdapter;
import com.example.login2.Models.CourseModel;
import com.example.login2.Repositories.CourseRepository;
import com.example.login2.Repositories.EnrollmentsRepository;
import com.example.login2.Utils.Constants;
import com.example.login2.Utils.UserManager;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.Query;

import java.util.List;

public class CourseListActivity extends AppCompatActivity {
    private FloatingActionButton fab;
    private String type;
    private TeacherCourseAdapter teacherCourseAdapter;
    private CourseRepository courseRepository;
    private EnrollmentsRepository enrollmentsRepository;
    StudentCourseAdapter studentCourseAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);
        recyclerView = findViewById(R.id.courseList);
        fab = findViewById(R.id.fab);
        courseRepository = new CourseRepository();
        enrollmentsRepository = new EnrollmentsRepository();

        type = UserManager.getInstance().getUserType();
        if(type.equals(Constants.TYPE_TEACHER)){
            setupTeacherRecycleView();
        } else{
            setupStudentRecycleView();
        }
        setupFab();
    }



    private void setupStudentRecycleView() {
        List<String> courseIds;
        enrollmentsRepository.getUserEnrollments(UserManager.getInstance().getUserId())
                .addOnSuccessListener(ids -> {
                    courseRepository.getCourses(ids).addOnSuccessListener(courses ->{
                        recyclerView.setLayoutManager(new LinearLayoutManager(this));
                        studentCourseAdapter = new StudentCourseAdapter(this,courses);
                        recyclerView.setAdapter(studentCourseAdapter);
                    });
                });
    }

    private void setupTeacherRecycleView() {
        Query query;
        query  = courseRepository.getAllCoursesTaughtBy(UserManager.getInstance().getCurrentUserModel().getUserId());
        FirestoreRecyclerOptions<CourseModel> options = new FirestoreRecyclerOptions.Builder<CourseModel>()
                .setQuery(query, CourseModel.class).build();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        teacherCourseAdapter = new TeacherCourseAdapter(options,this, UserManager.getInstance().getUserType());
        recyclerView.setAdapter(teacherCourseAdapter);
    }

    private void setupFab() {
        fab.setOnClickListener((v -> {
            if (type.equals(Constants.TYPE_TEACHER)) {
                CourseInitFragment dialogFragment = new CourseInitFragment();
                dialogFragment.show(getSupportFragmentManager(), "codeEntryDialog");
            } else {
                InputCourseCodeFragment dialogFragment = new InputCourseCodeFragment();
                dialogFragment.show(getSupportFragmentManager(), "codeEntryDialog");
            }
        }));
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(UserManager.getInstance().getUserType().equals(Constants.TYPE_TEACHER)) {
            teacherCourseAdapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(UserManager.getInstance().getUserType().equals(Constants.TYPE_TEACHER)) {
            teacherCourseAdapter.startListening();
        }
    }
}