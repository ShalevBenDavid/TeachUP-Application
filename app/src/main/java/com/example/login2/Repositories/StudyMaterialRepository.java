package com.example.login2.Repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.login2.Models.StudyMaterialModel;
import com.example.login2.Utils.Constants;
import com.example.login2.Utils.CourseManager;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
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
                .addSnapshotListener((snapshot, e) -> {
                    if (e != null) {
                        return;
                    }

                    if (snapshot != null) {
                        for (DocumentChange documentChange : snapshot.getDocumentChanges()) {
                            StudyMaterialModel material = documentChange.getDocument().toObject(StudyMaterialModel.class);

                            switch(documentChange.getType()){
                                case ADDED:
                                    studyMaterials.add(documentChange.getDocument().toObject(StudyMaterialModel.class));
                                    break;
                                case MODIFIED:
                                    int modifyIndex = studyMaterials.indexOf(material);
                                    if (modifyIndex != -1) {
                                        studyMaterials.set(modifyIndex, material);
                                    }
                                    break;
                                case REMOVED:
                                    studyMaterials.remove(material);
                                    break;
                            }
                        }
                    }
                    studyMaterialsLiveData.postValue(studyMaterials);
                });

        return studyMaterialsLiveData;
    }

    public void addStudyMaterial(StudyMaterialModel studyMaterial, FirestoreCallback callback) {
        DocumentReference newDocRef = db.collection(Constants.COURSE_COLLECTION)
                .document(CourseManager.getInstance().getCurrentCourse().getCourseId())
                .collection(Constants.STUDY_MATERIAL)
                .document();

        studyMaterial.setId(newDocRef.getId());

        newDocRef.set(studyMaterial).addOnCompleteListener(task -> {
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
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess();
                    } else {
                        callback.onError(Objects.requireNonNull(task.getException()).getMessage());
                    }
                });
    }

    public interface FirestoreCallback {
        void onSuccess();

        void onError(String error);
    }
}
