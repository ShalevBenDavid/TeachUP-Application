package com.example.login2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.login2.Adapters.StudyMaterialAdapter;
import com.example.login2.Models.StudyMaterialModel;
import com.example.login2.Repositories.StudyMaterialRepository;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.Query;

public class StudyMaterialActivity extends AppCompatActivity {
    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private StudyMaterialRepository studyMaterialRepository;
    private StudyMaterialAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_material);
        fab = findViewById(R.id.fab);
        studyMaterialRepository = new StudyMaterialRepository();
        recyclerView = findViewById(R.id.materialRecycler);
        setupFab();
        setupRecycleView();
    }

    private void setupRecycleView() {
        recyclerView.setItemAnimator(null);
        Query query = studyMaterialRepository.getCourseStudyMaterials();
        FirestoreRecyclerOptions<StudyMaterialModel> options = new FirestoreRecyclerOptions.Builder<StudyMaterialModel>().setQuery(query,StudyMaterialModel.class).build();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new StudyMaterialAdapter(options,this);
        recyclerView.setAdapter(adapter);
    }

    private void setupFab() {
        fab.setOnClickListener((v)->{
            UploadStudyMaterial uploadStudyMaterial = new UploadStudyMaterial();
            uploadStudyMaterial.show(getSupportFragmentManager(),"Upload");
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }

}