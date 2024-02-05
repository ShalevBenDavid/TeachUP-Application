
package com.example.login2.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.login2.Adapters.StudentListAdapter;
import com.example.login2.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login2.Utils.CourseManager;
import com.example.login2.ViewModels.StudentListViewModel;
import com.example.login2.databinding.ActivityMainChatBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainChatActivity extends AppCompatActivity {
    ActivityMainChatBinding binding;
    StudentListAdapter studentsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainChatBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // Set up the toolbar.
        setSupportActionBar(binding.toolbar);

        // Set up the icon click listener.
        // Handle click event to enter the group chat room.
        binding.iconGroup.setOnClickListener(v -> {
            startActivity(new Intent(MainChatActivity.this, MainChatActivity.class));
        });

        // Remove the default app name from the toolbar.
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        // Set up the RecyclerView and UserAdapter.
        studentsAdapter = new StudentListAdapter();
        StudentListViewModel studentListViewModel = new ViewModelProvider(this).get(StudentListViewModel.class);
        binding.recycler.setLayoutManager(new LinearLayoutManager(this));
        binding.recycler.setAdapter(studentsAdapter);

        studentListViewModel.getEnrolledStudents(CourseManager.getInstance().getCurrentCourse().getCourseId()).observe(this, students -> {
            if (students != null) {
                studentsAdapter.setStudents(students);
            }
        });
    }

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