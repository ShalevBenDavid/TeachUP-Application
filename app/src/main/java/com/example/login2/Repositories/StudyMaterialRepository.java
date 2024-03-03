package com.example.login2.Repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.login2.Models.StudyMaterialModel;
import com.example.login2.Utils.Constants;
import com.example.login2.Utils.CourseManager;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StudyMaterialRepository {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final MutableLiveData<List<StudyMaterialModel>> studyMaterialsLiveData = new MutableLiveData<>();
    private final List<StudyMaterialModel> studyMaterials = new ArrayList<>();

    public LiveData<List<StudyMaterialModel>> getCourseStudyMaterials() {
        db.collection(Constants.COURSE_COLLECTION)
                .document(CourseManager.getInstance().getCurrentCourse().getCourseId())
                .collection(Constants.STUDY_MATERIAL)
                .addSnapshotListener((snapshot,e) ->{
                    if(e != null){
                        return;
                    }

                    if(snapshot != null){
                        for(DocumentChange documentChange : snapshot.getDocumentChanges()){
                            studyMaterials.add(documentChange.getDocument().toObject(StudyMaterialModel.class));
                            Log.e("studyMaterial","in repo");
                        }
                    }
                    Log.e("study size",String.valueOf(studyMaterials.size()));
                    studyMaterialsLiveData.postValue(studyMaterials);
                });

        return studyMaterialsLiveData;
    }

    public void addStudyMaterial(StudyMaterialModel studyMaterial, FirestoreCallback callback) {
        db.collection(Constants.COURSE_COLLECTION)
                .document(CourseManager.getInstance().getCurrentCourse().getCourseId())
                .collection(Constants.STUDY_MATERIAL)
                .document()
                .set(studyMaterial).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess();
                    } else {
                        callback.onError(Objects.requireNonNull(task.getException()).getMessage());
                    }
                });
    }

    public void deleteStudyMaterial(String materialId, FirestoreCallback callback) {
        db.collection(Constants.COURSE_COLLECTION)
                .document(CourseManager.getInstance().getCurrentCourse().getCourseId())
                .collection(Constants.STUDY_MATERIAL)
                .document(materialId)
                .delete()
                .addOnCompleteListener(task ->{
                    if(task.isSuccessful()){
                        callback.onSuccess();
                    } else{
                        callback.onError(Objects.requireNonNull(task.getException()).getMessage());
                    }
                });
    }

    public interface FirestoreCallback {
        void onSuccess();

        void onError(String error);
    }
}
