package com.example.login2.Repositories;

import com.example.login2.Models.StudyMaterialModel;
import com.example.login2.Utils.Constants;
import com.example.login2.Utils.CourseManager;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import java.util.List;
import java.util.Objects;

public class StudyMaterialRepository {
    private final FirebaseFirestore db;

    public StudyMaterialRepository(){
        this.db = FirebaseFirestore.getInstance();
    }

    public Query getCourseStudyMaterials(){
        return db.collection(Constants.COURSE_COLLECTION).document(CourseManager.getInstance().getCurrentCourse().getCourseId())
                .collection(Constants.STUDY_MATERIAL);
    }

    public void addStudyMaterial(StudyMaterialModel studyMaterial,FirestoreCallback callback){
        db.collection(Constants.COURSE_COLLECTION)
                .document(CourseManager.getInstance().getCurrentCourse().getCourseId())
                .collection(Constants.STUDY_MATERIAL)
                .document()
                .set(studyMaterial).addOnCompleteListener(task ->{
                    if(task.isSuccessful()){
                        callback.onSuccess(null);
                    } else{
                        callback.onError(Objects.requireNonNull(task.getException()).getMessage());
                    }
                });
    }

    public interface FirestoreCallback{
        void onSuccess(List<StudyMaterialModel> studyMaterials);
        void onError(String error);
    }
}
