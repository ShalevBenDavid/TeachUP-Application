package com.example.login2.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.login2.Adapters.StudyMaterialAdapter;
import com.example.login2.Utils.Constants;
import com.example.login2.Utils.UserManager;
import com.example.login2.ViewModels.StudyMaterialViewModel;
import com.example.login2.databinding.ActivityStudyMaterialBinding;

public class StudyMaterialActivity extends AppCompatActivity {
    private ActivityStudyMaterialBinding binding;
    private StudyMaterialViewModel studyMaterialViewModel;
    private StudyMaterialAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStudyMaterialBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        studyMaterialViewModel = new ViewModelProvider(this).get(StudyMaterialViewModel.class);

        if(UserManager.getInstance().getUserType().equals(Constants.TYPE_TEACHER)) {
            setupFab();
        }
        setupRecycleView();
    }

    private void setupRecycleView() {
        binding.materialRecycler.setItemAnimator(null);
        binding.materialRecycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new StudyMaterialAdapter(this,studyMaterialViewModel);
        binding.materialRecycler.setAdapter(adapter);

        studyMaterialViewModel.getStudyMaterials().observe(this,studyMaterials -> adapter.setStudyMaterials(studyMaterials));

    }

    private void setupFab() {
        binding.fab.setVisibility(View.VISIBLE);
        binding.fab.setOnClickListener((v)-> startActivity(new Intent(StudyMaterialActivity.this, UploadMaterialActivity.class)));
    }


}