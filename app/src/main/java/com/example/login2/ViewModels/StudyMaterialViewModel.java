package com.example.login2.ViewModels;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.login2.Models.StudyMaterialModel;
import com.example.login2.Repositories.StorageRepository;
import com.example.login2.Repositories.StudyMaterialRepository;

import java.util.List;

public class StudyMaterialViewModel extends ViewModel {
    private final StudyMaterialRepository repository = new StudyMaterialRepository();;

    public LiveData<List<StudyMaterialModel>> getStudyMaterials(){
        return repository.getCourseStudyMaterials();
    }

    public void addStudyMaterial(StudyMaterialModel studyMaterial, StudyMaterialRepository.FirestoreCallback callback){
        repository.addStudyMaterial(studyMaterial,callback);
    }

    public void deleteStudyMaterial(String materialId, StudyMaterialRepository.FirestoreCallback callback){
        repository.deleteStudyMaterial(materialId, callback);
    }

    public void uploadStudyMaterial(Uri fileUri, String storagePath, StorageRepository.StorageTaskListener listener){
        new StorageRepository().uploadFile(fileUri,storagePath,listener);
    }
}
