package com.example.teachup;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    FirebaseFirestore db;
    CollectionReference usersCollection;
    ImageView iconGroup;
    RecyclerView recyclerView;
    UserAdapter userAdapter;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the toolbar.
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set up the icon click listener.
        iconGroup = findViewById(R.id.iconGroup);
        // Handle click event to enter the group chat room.
        iconGroup.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ChatGroupActivity.class);
            startActivity(intent);
        });

        // Remove the default app name from the toolbar.
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        String userName = getIntent().getStringExtra("user");

        // Set up the RecyclerView and UserAdapter.
        userAdapter = new UserAdapter(this);
        recyclerView = findViewById(R.id.recycler);
        recyclerView.setAdapter(userAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Set the collection reference to "users".
        db = FirebaseFirestore.getInstance();
        usersCollection = db.collection("users");

        // Get the current user.
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // Update the user list when data changes in the "users" collection.
        usersCollection.addSnapshotListener((queryDocumentSnapshots, item) -> {
            // Error while fetching data from "users"" collection.
            if (item != null) {
                Toast.makeText(MainActivity.this, "Error fetching data: " +
                        item.getMessage(), Toast.LENGTH_SHORT).show();
                return;
            }
            // Check for changes in the "users" collection.
            if (queryDocumentSnapshots != null) {
                // Process document changes.
                for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()) {
                    // Convert Firestore document to UserModel object.
                    UserModel userModel = documentChange.getDocument().toObject(UserModel.class);

                    // Verify that we got a valid user which isn't the current user.
                    if (userModel.getUserID() != null && !userModel.getUserID()
                            .equals(currentUser.getUid())) {
                        // Handle the addition of a new user.
                        if (documentChange.getType() == DocumentChange.Type.ADDED) {
                            userAdapter.add(userModel);
                        }
                    }
                }

                // Update the UI (view)
                userAdapter.notifyDataSetChanged();
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
            startActivity(new Intent(MainActivity.this, SigninActivity.class));
            finish();
            return true;
        }

        return false;
    }
}