
package com.example.login2.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.login2.Adapters.StudentListAdapter;
import com.example.login2.Models.UserModel;
import com.example.login2.R;
import com.example.login2.Utils.CourseManager;
import com.example.login2.ViewModels.StudentListViewModel;
import com.example.login2.databinding.ActivityMainChatBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class MainChatActivity extends AppCompatActivity {
    ActivityMainChatBinding binding;
    StudentListAdapter studentsAdapter;
    List <UserModel> allStudents = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Defining layout and setting the content view of the activity.
        binding = ActivityMainChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set up the toolbar.
        setSupportActionBar(binding.toolbar);

        // Remove the default app name from the toolbar.
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        // Handle click event to enter the group chat room.
        binding.iconGroup.setOnClickListener(v -> startActivity(new Intent(MainChatActivity.this, GroupChatActivity.class)));

        // Set up the RecyclerView and students adapter.
        studentsAdapter = new StudentListAdapter(MainChatActivity.this);
        binding.recycler.setLayoutManager(new LinearLayoutManager(this));
        binding.recycler.setAdapter(studentsAdapter);

        // Add the enrolled course students to the students adapter.
        StudentListViewModel studentListViewModel = new ViewModelProvider(this).get(StudentListViewModel.class);
        studentListViewModel.getEnrolledStudents(CourseManager.getInstance().getCurrentCourse().getCourseId()).observe(this, students -> {
            if (students != null) {
                allStudents = students;
                studentsAdapter.setStudents(students);
            }
        });

        // Listen for changes in the search bar.
        binding.searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    // Filter users from student adapter based on search.
    private void filter(String text) {
        List<UserModel> filteredList = new ArrayList<>();
        for (UserModel student : allStudents) {
            if (student.getUserName().toLowerCase().startsWith(text.toLowerCase())) {
                filteredList.add(student);
            }
        }
        studentsAdapter.setStudents(filteredList);
    }

    // Create options menu in the toolbar.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        return true;
    }

    // Perform a logout and redirect to sign in activity.
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(MainChatActivity.this, WelcomeActivity.class));
            finish();
            return true;
        }

        return false;
    }
}