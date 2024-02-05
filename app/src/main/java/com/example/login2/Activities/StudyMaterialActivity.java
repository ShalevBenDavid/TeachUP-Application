package com.example.login2.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.login2.Adapters.StudyMaterialAdapter;
import com.example.login2.Models.StudyMaterialModel;
import com.example.login2.R;
import com.example.login2.Repositories.StudyMaterialRepository;
import com.example.login2.Utils.Constants;
import com.example.login2.Utils.UserManager;
import com.example.login2.databinding.ActivityCourseListBinding;
import com.example.login2.databinding.ActivityStudyMaterialBinding;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.Query;

public class StudyMaterialActivity extends AppCompatActivity {
    private ActivityStudyMaterialBinding binding;
    private StudyMaterialRepository studyMaterialRepository;
    private StudyMaterialAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStudyMaterialBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        studyMaterialRepository = new StudyMaterialRepository();
        if(UserManager.getInstance().getUserType().equals(Constants.TYPE_TEACHER)) {
            setupFab();
        }
        setupRecycleView();
    }

    private void setupRecycleView() {
        binding.materialRecycler.setItemAnimator(null);
        Query query = studyMaterialRepository.getCourseStudyMaterials();
        FirestoreRecyclerOptions<StudyMaterialModel> options = new FirestoreRecyclerOptions.Builder<StudyMaterialModel>().setQuery(query,StudyMaterialModel.class).build();
        binding.materialRecycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new StudyMaterialAdapter(options,this);
        binding.materialRecycler.setAdapter(adapter);
    }

    private void setupFab() {
        binding.fab.setVisibility(View.VISIBLE);
        binding.fab.setOnClickListener((v)->{
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