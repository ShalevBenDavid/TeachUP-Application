package com.example.login2.Activities;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.login2.Adapters.StudentCourseAdapter;
import com.example.login2.Adapters.TeacherCourseAdapter;
import com.example.login2.Models.CourseModel;
import com.example.login2.R;
import com.example.login2.Repositories.CourseRepository;
import com.example.login2.Repositories.FirebaseAuthRepository;
import com.example.login2.Utils.Constants;
import com.example.login2.Utils.UserManager;
import com.example.login2.ViewModels.CourseListViewModel;
import com.example.login2.databinding.ActivityCourseListBinding;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

import java.util.List;

public class CourseListActivity extends AppCompatActivity {
    private ActivityCourseListBinding binding;
    private String userType;
    private TeacherCourseAdapter teacherCourseAdapter;
    private CourseRepository courseRepository;
    private StudentCourseAdapter studentCourseAdapter;
    private CourseListViewModel courseListViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCourseListBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        userType = UserManager.getInstance().getUserType();

        courseRepository = new CourseRepository();
        courseListViewModel = new ViewModelProvider(this).get(CourseListViewModel.class);
        if(userType.equals(Constants.TYPE_TEACHER)){
            setupTeacherRecyclerView();
        } else{
            observeStudentCourseData();
        }
        observeStudentCourseData();
        setupFab();

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                FirebaseAuthRepository auth = new FirebaseAuthRepository();
                auth.logout();
                startActivity(new Intent(CourseListActivity.this, WelcomeActivity.class));
                finish();
            }
        });
    }

    private void observeStudentCourseData() {
        courseListViewModel.getCourses(userType).observe(this, this::setupStudentRecyclerView);
    }


    private void setupStudentRecyclerView(List<CourseModel> courses) {
        binding.courseList.setLayoutManager(new LinearLayoutManager(this));
        studentCourseAdapter = new StudentCourseAdapter(this, courses);
        binding.courseList.setAdapter(studentCourseAdapter);
    }

    private void setupTeacherRecyclerView() {
        Query query = courseRepository.getAllCoursesTaughtBy(UserManager.getInstance().getCurrentUserModel().getUserId());
        FirestoreRecyclerOptions<CourseModel> options = new FirestoreRecyclerOptions.Builder<CourseModel>()
                .setQuery(query, CourseModel.class).build();

        binding.courseList.setLayoutManager(new LinearLayoutManager(this));
        teacherCourseAdapter = new TeacherCourseAdapter(options, this);
        binding.courseList.setAdapter(teacherCourseAdapter);
    }


    private void setupFab() {
        binding.fab.setOnClickListener((v -> {
            if (userType.equals(Constants.TYPE_TEACHER)) {
                CourseInitFragment dialogFragment = new CourseInitFragment();
                dialogFragment.show(getSupportFragmentManager(), "codeEntryDialog");
            } else {
                InputCourseCodeFragment dialogFragment = new InputCourseCodeFragment();
                dialogFragment.show(getSupportFragmentManager(), "codeEntryDialog");
            }
        }));
    }

    public StudentCourseAdapter getStudentCourseAdapter() {
        return studentCourseAdapter;
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (UserManager.getInstance().getUserType().equals(Constants.TYPE_TEACHER)) {
            teacherCourseAdapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (UserManager.getInstance().getUserType().equals(Constants.TYPE_TEACHER)) {
            teacherCourseAdapter.stopListening();
        }
    }
}