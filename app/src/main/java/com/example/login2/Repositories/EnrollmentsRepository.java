package com.example.login2.Repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.login2.Models.EnrollmentModel;
import com.example.login2.Models.UserModel;
import com.example.login2.Utils.Constants;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class EnrollmentsRepository {
    private final FirebaseFirestore db;

    public EnrollmentsRepository() {
        this.db = FirebaseFirestore.getInstance();
    }

    public void enrollStudent(String courseId, String studentId, EnrollmentCallback callback) {
        DocumentReference newEnrollmentRef = db.collection(Constants.ENROLLMENTS_COLLECTION).document();

        newEnrollmentRef.set(new EnrollmentModel(courseId, studentId, true))
                .addOnSuccessListener(aVoid -> callback.onSuccess(null))
                .addOnFailureListener(e -> callback.onError(e.getMessage()));
    }

    public void unenrollStudent(String courseId, String studentId, EnrollmentCallback callback) {
        db.collection(Constants.ENROLLMENTS_COLLECTION).whereEqualTo("studentId", studentId).whereEqualTo("courseId", courseId).get()
                .addOnSuccessListener(query -> {
                    if (!query.isEmpty()) {
                        query.getDocuments().get(0).getReference().delete()
                                .addOnSuccessListener(aVoid -> callback.onSuccess(null))
                                .addOnFailureListener(e -> callback.onError("Error unenrolling student: " + e.getMessage()));
                    }
                }).addOnFailureListener(e -> callback.onError("Error finding enrollment: " + e.getMessage()));
    }

    public Task<List<String>> getUserEnrollments(String userId) {
        TaskCompletionSource<List<String>> taskCompletionSource = new TaskCompletionSource<>();
        List<String> courseIds = new ArrayList<>();

        db.collection(Constants.ENROLLMENTS_COLLECTION).whereEqualTo("studentId", userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot documentSnapshot : task.getResult()) {
                            String courseId = documentSnapshot.getString("courseId");
                            courseIds.add(courseId);
                        }
                        taskCompletionSource.setResult(courseIds);
                    } else {
                        taskCompletionSource.setException(task.getException());
                    }
                });
        return taskCompletionSource.getTask();
    }

    public LiveData<List<UserModel>> getCourseEnrollments(String courseId) {
        MutableLiveData<List<UserModel>> students = new MutableLiveData<>();
        List<UserModel> helperList = new ArrayList<>();

        Query enrollmentQuery = db.collection(Constants.ENROLLMENTS_COLLECTION)
                .whereEqualTo("courseId", courseId);

        enrollmentQuery.get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<DocumentSnapshot> document = queryDocumentSnapshots.getDocuments();
            if (document.isEmpty()) {
                students.postValue(helperList);
                return;
            }

            for (DocumentSnapshot snapshot : document) {
                String studentId = snapshot.getString("studentId");
                db.collection(Constants.USERS_COLLECTION).document(studentId)
                        .get().addOnSuccessListener(studentDoc -> {
                            if (studentDoc.exists()) {
                                helperList.add(studentDoc.toObject(UserModel.class));
                                students.postValue(new ArrayList<>(helperList));
                            }
                        });
            }
        });

        return students;
    }

    public Task<Boolean> isEnrolled(String userId, String courseId) {
        TaskCompletionSource<Boolean> taskCompletionSource = new TaskCompletionSource<>();

        db.collection(Constants.ENROLLMENTS_COLLECTION).whereEqualTo("studentId", userId)
                .whereEqualTo("courseId", courseId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        boolean isEnrolled = !task.getResult().isEmpty();
                        taskCompletionSource.setResult(isEnrolled);
                    } else {
                        taskCompletionSource.setException(task.getException());
                    }
                });
        return taskCompletionSource.getTask();
    }

    public interface EnrollmentCallback {
        void onSuccess(List<UserModel> students);

        void onError(String error);
    }
}
