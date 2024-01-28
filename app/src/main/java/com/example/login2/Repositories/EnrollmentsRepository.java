package com.example.login2.Repositories;

import android.util.Log;

import com.example.login2.Models.EnrollmentModel;
import com.example.login2.Utils.Constants;
import com.example.login2.Utils.CustomUtils;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class EnrollmentsRepository {
    private final FirebaseFirestore db;

    public EnrollmentsRepository() {
        this.db = FirebaseFirestore.getInstance();
    }

    public void enrollStudent(String courseId,String studentId,EnrollmentCallback callback){
        DocumentReference newEnrollmentRef = db.collection(Constants.ENROLLMENTS_COLLECTION).document();

        newEnrollmentRef.set(new EnrollmentModel(courseId,studentId,true))
                .addOnSuccessListener(aVoid -> callback.onSuccess(newEnrollmentRef.getId()))
                .addOnFailureListener(e -> callback.onError(e.getMessage()));
    }

    public void unenrollStudent(String courseId,String studentId,EnrollmentCallback callback){
        db.collection(Constants.ENROLLMENTS_COLLECTION).whereEqualTo("studentId",studentId).whereEqualTo("courseId",courseId).get()
                .addOnSuccessListener(query->{
                    if(!query.isEmpty()){
                        query.getDocuments().get(0).getReference().delete()
                                .addOnSuccessListener(aVoid -> callback.onSuccess(null))
                                .addOnFailureListener(e -> callback.onError("Error unenrolling student: "+e.getMessage()));
                    }
                }).addOnFailureListener(e -> callback.onError("Error finding enrollment: " + e.getMessage()));
    }

    public Task<List<String>> getUserEnrollments(String userId){
        TaskCompletionSource<List<String>> taskCompletionSource = new TaskCompletionSource<>();
        List<String> courseIds = new ArrayList<>();

        db.collection(Constants.ENROLLMENTS_COLLECTION).whereEqualTo("studentId",userId)
                .get()
                .addOnCompleteListener(task ->{
                    if(task.isSuccessful()){
                        for(DocumentSnapshot documentSnapshot : task.getResult()){
                            String courseId = documentSnapshot.getString("courseId");
                            courseIds.add(courseId);
                        }
                        taskCompletionSource.setResult(courseIds);
                    } else{
                        taskCompletionSource.setException(task.getException());
                    }
                });
        return taskCompletionSource.getTask();
    }

    public void getCourseEnrollments(){

    }

    public Task<Boolean> isEnrolled(String userId,String courseId){
        TaskCompletionSource<Boolean> taskCompletionSource = new TaskCompletionSource<>();

        db.collection(Constants.ENROLLMENTS_COLLECTION).whereEqualTo("studentId",userId)
                .whereEqualTo("courseId",courseId)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        boolean isEnrolled = !task.getResult().isEmpty();
                        taskCompletionSource.setResult(isEnrolled);
                    } else{
                        taskCompletionSource.setException(task.getException());
                    }
                });
        return taskCompletionSource.getTask();
    }

    public interface EnrollmentCallback{
        void onSuccess(String courseId);
        void onError(String error);
    }
}
